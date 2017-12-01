package com.weiyankeji.zhongmei.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.weiyankeji.library.customview.adapter.BaseRecycleViewAdapter;
import com.weiyankeji.library.customview.swipetoloadlayout.OnLoadMoreListener;
import com.weiyankeji.library.customview.swipetoloadlayout.OnRefreshListener;
import com.weiyankeji.library.customview.swipetoloadlayout.SwipeToLoadLayout;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.customview.RecyclerViewDecoration;
import com.weiyankeji.zhongmei.ui.mpresenter.BasePresenter;

import butterknife.BindView;

/**
 * Created by caiwancheng on 2017/7/17.
 */

public abstract class BaseListFragment<V, P extends BasePresenter<V>> extends BaseMvpFragment implements OnRefreshListener, OnLoadMoreListener {

    /**
     * 刷新回调
     */
    public abstract void onRefreshCallback();

    /**
     * 加载回调
     */
    public abstract void onLoadCallback();

    /**
     * 数据初始化
     *
     * @param savedInstanceState
     */
    public abstract void initData(Bundle savedInstanceState);

    /**
     * 设置Adapter
     *
     * @return
     */
    public abstract BaseRecycleViewAdapter getAdapter();

    @BindView(R.id.swipe_to_load_layout)
    SwipeToLoadLayout mSwipeRefresh;
    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;

    private BaseRecycleViewAdapter mAdapter;

    public RecyclerViewDecoration mRecyclerViewDecoration;

    @Override
    public int setContentLayout() {
        return R.layout.include_load_refresh;
    }

    @Override
    public void finishCreateView(Bundle savedInstanceState) {
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setOnLoadMoreListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerViewDecoration = new RecyclerViewDecoration(mContext);
        mRecyclerView.addItemDecoration(mRecyclerViewDecoration);

        initData(savedInstanceState);

        mAdapter = getAdapter();
        if (mAdapter != null) {
            mRecyclerView.setAdapter(mAdapter);
        }

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                        mSwipeRefresh.setLoadingMore(true);
                    }
                    super.onScrollStateChanged(recyclerView, newState);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        onRefreshCallback();
    }

    @Override
    public void onLoadMore() {
        onLoadCallback();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopRefresh();
        stopLoadMore();
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * 停止刷新
     */
    public void stopRefresh() {
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    /**
     * 停止加载
     */
    public void stopLoadMore() {
        if (mSwipeRefresh.isLoadingMore()) {
            mSwipeRefresh.setLoadingMore(false);
        }
    }

    /**
     * 设置无法刷新
     */
    public void setCannotRefresh() {
        mSwipeRefresh.setRefreshEnabled(false);
    }

    /**
     * 设置无法加载
     */
    public void setCannotLoad() {
        mSwipeRefresh.setLoadMoreEnabled(false);
    }
}