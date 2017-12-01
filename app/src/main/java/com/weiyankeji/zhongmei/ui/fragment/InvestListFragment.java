package com.weiyankeji.zhongmei.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.weiyankeji.library.customview.adapter.BaseRecycleViewAdapter;
import com.weiyankeji.library.customview.swipetoloadlayout.OnLoadMoreListener;
import com.weiyankeji.library.customview.swipetoloadlayout.OnRefreshListener;
import com.weiyankeji.library.customview.swipetoloadlayout.SwipeToLoadLayout;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.adapter.InvestListAdapter;
import com.weiyankeji.zhongmei.ui.customview.CostomDecoration;
import com.weiyankeji.zhongmei.ui.customview.TwitterRefreshHeaderView;
import com.weiyankeji.zhongmei.ui.mmodel.bean.InvestDetailBean;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestListRequest;
import com.weiyankeji.zhongmei.ui.mpresenter.InvestListPresenter;
import com.weiyankeji.zhongmei.ui.mview.InvestListView;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.StatisticalUtils;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 投资列表
 * Created by liuhaiyang on 2017/8/22.
 */

public class InvestListFragment extends BaseMvpFragment<InvestListView, InvestListPresenter>
        implements InvestListView, InvestListAdapter.OnItemClickListener, InvestListAdapter.OnItemChildClickListener,
        TabLayout.OnTabSelectedListener, OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.tl_choose)
    TabLayout mTlTab;

    @BindView(R.id.swipe_to_load_layout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    @BindView(R.id.swipe_target)
    RecyclerView mRvList;
    private InvestListAdapter mListAdapter;

    private int mSkuType;
    private int mPage = 1;
    private int mItemCount;
    //上页最后一条sku_id
    private String mStartId;
    private InvestListRequest mRequest;
    private List<InvestDetailBean> mResponList;

    @Override
    public int setContentLayout() {
        return R.layout.fragment_invest_list;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        String title;
        if (bundle != null) {
            title = bundle.getString(ConstantUtils.KEY_TITLE);
            mSkuType = bundle.getInt(ConstantUtils.KEY_TYPE);
        } else {
            title = getString(R.string.invest_best_regular);
            mSkuType = ConstantUtils.INVEST_SKU_TYPE_STIPULATE;
        }
        setTitle(title, true);

        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setRefreshFinalDragOffset(TwitterRefreshHeaderView.REFRESH_MAX_DRAG_OFFSET);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);

        mListAdapter = new InvestListAdapter();
        mRvList.setLayoutManager(new LinearLayoutManager(mContext));
        mRvList.setAdapter(mListAdapter);
        mRvList.addItemDecoration(new CostomDecoration(mContext, R.dimen.margin_big, R.dimen.margin_big));
        mListAdapter.setOnItemClickListener(this);
        mListAdapter.setOnItemChildClickListener(this);

        loadData(true);

        if (mSkuType == ConstantUtils.INVEST_SKU_TYPE_CUSTOM) {
            mTlTab.setVisibility(View.GONE);
        } else {
            StatisticalUtils.trackCustomKVEvent(StatisticalUtils.LABLE_REGULAR_MORE);
            mTlTab.setVisibility(View.VISIBLE);
            mTlTab.setOnTabSelectedListener(this);
        }

        mResponList = new ArrayList<>();

        CommonUtils.setIndicator(mContext, mTlTab, 30);

    }


    @Override
    public InvestListPresenter initPresenter() {
        return new InvestListPresenter();
    }


    @Override
    public void showLoading() {
        setLoading(true);
    }

    @Override
    public void hideLoading() {
        setLoading(false);
        if (mSwipeToLoadLayout.isRefreshing()) {
            mSwipeToLoadLayout.setRefreshing(false);
        }
        if (mSwipeToLoadLayout.isLoadingMore()) {
            mSwipeToLoadLayout.setLoadingMore(false);
        }
    }

    @Override
    public void setListData(List<InvestDetailBean> response) {
        if (mListAdapter != null) {
            mResponList.addAll(response);
            mListAdapter.setNewData(mResponList);
            mStartId = mResponList.get(mResponList.size() - 1).sku_id;

            if (response.size() < ConstantUtils.PAGE_SIZE) {
                mSwipeToLoadLayout.setLoadMoreEnabled(false);
            } else {
                mSwipeToLoadLayout.setLoadMoreEnabled(true);
            }
        }

    }

    private void loadData(boolean isShow) {
        if (mRequest == null) {
            mRequest = new InvestListRequest();
        }
        mRequest.page = mPage;
        mRequest.sku_type = mSkuType;
        mRequest.start_id = mStartId;
        mPresenter.loadData(mRequest, isShow);
    }

    /**
     * list item click
     */
    @Override
    public void onItemClick(BaseRecycleViewAdapter adapter, View view, int position) {
        InvestDetailBean child = (InvestDetailBean) adapter.getItem(position);
        if (child == null) {
            return;
        }
        Bundle parm = new Bundle();
        parm.putString(ConstantUtils.KEY_ID, child.sku_id);
        startFragment(InvestProductDetailFragment.class, parm);
    }

    /**
     * 立即投资
     */
    @Override
    public void onItemChildClick(BaseRecycleViewAdapter adapter, View view, int position) {
        onItemClick(adapter, view, position);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mRequest.sort_rule = 1;
        switch (tab.getPosition()) {
            case 0:
                mRequest.sort_field = "";
                break;
            case 1:
                mRequest.sort_field = InvestListRequest.FIELD_RATE;
                break;
            case 2:
                mRequest.sort_field = InvestListRequest.FIELD_PERIOD;
                if (tab.getCustomView() != null) {
                    ImageView imageView = tab.getCustomView().findViewById(R.id.tabIcon);
                    imageView.setBackgroundResource(R.drawable.finance_icon_reorder_selected_down);
                    mItemCount += 1;
                }
                break;
            default:
                break;
        }

        setLoading(true);
        onRefresh();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        if (tab.getPosition() == 2 && tab.getCustomView() != null) {
            mItemCount = 0;
            ImageView imageView = tab.getCustomView().findViewById(R.id.tabIcon);
            imageView.setBackgroundResource(R.drawable.finance_icon_reorder_normal);
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        if (tab.getPosition() == 2 && tab.getCustomView() != null) {
            mItemCount += 1;
            ImageView imageView = tab.getCustomView().findViewById(R.id.tabIcon);
            if (mItemCount % 2 == 0) { //升
                mRequest.sort_rule = 0;
                imageView.setBackgroundResource(R.drawable.finance_icon_reorder_selected_up);
            } else {
                mRequest.sort_rule = 1;
                imageView.setBackgroundResource(R.drawable.finance_icon_reorder_selected_down);
            }
            onRefresh();
        }
    }

    @Override
    public void onRefresh() {
        mRvList.scrollToPosition(0);
        mPage = 1;
        mStartId = "";
        if (mResponList != null) {
            mResponList.clear();
        }
        loadData(false);
    }

    @Override
    public void onLoadMore() {
        mPage++;
        loadData(false);
    }
}
