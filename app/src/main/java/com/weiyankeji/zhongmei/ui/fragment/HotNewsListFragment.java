package com.weiyankeji.zhongmei.ui.fragment;


import android.os.Bundle;
import android.view.View;

import com.weiyankeji.library.customview.adapter.BaseRecycleViewAdapter;
import com.weiyankeji.library.utils.ExtendUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.adapter.HotNewsListAdapter;
import com.weiyankeji.zhongmei.ui.customview.CostomDecoration;
import com.weiyankeji.zhongmei.ui.customview.ErrorPageView;
import com.weiyankeji.zhongmei.ui.mmodel.HotNewsResponse;
import com.weiyankeji.zhongmei.ui.mpresenter.HotNewsListPresenter;
import com.weiyankeji.zhongmei.ui.mview.HotNewsListView;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 热点新闻列表页
 * Created by zff on 2017/9/7.
 */

public class HotNewsListFragment extends BaseListFragment<HotNewsListView, HotNewsListPresenter>
        implements HotNewsListView {


    private List<HotNewsResponse> mItemBeans = new ArrayList<>();

    private HotNewsListAdapter mAdapter;

    private HotNewsListPresenter mHotNewsListPresenter;

    private int mPage = 1;

    @Override
    public void onRefreshCallback() {
        mPage = 1;
        if (mItemBeans != null) {
            mItemBeans.clear();
        }
        mHotNewsListPresenter.loadData(false, mPage);
    }


    @Override
    public void onLoadCallback() {
        mPage++;
        mHotNewsListPresenter.loadData(false, mPage);
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        setTitle(getString(R.string.hot_new_title), true);
        getRecyclerView().removeItemDecoration(mRecyclerViewDecoration);
        CostomDecoration decoration = new CostomDecoration(mContext, R.dimen.margin_big, R.dimen.margin_big);
        mRecyclerView.addItemDecoration(decoration);
        mAdapter = new HotNewsListAdapter(getActivity(), mItemBeans);
        mAdapter.setOnItemClickListener(new BaseRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecycleViewAdapter adapter, View view, int position) {
                HotNewsResponse response = (HotNewsResponse) adapter.getItem(position);
                CommonUtils.intentWebView(HotNewsListFragment.this, null, response.link_url);
            }
        });
        mHotNewsListPresenter = (HotNewsListPresenter) mPresenter;
        mHotNewsListPresenter.loadData(true, mPage);

        getErrorPageView().setOnErrorFunctionClickListener(new ErrorPageView.OnErrorFunctionClickListener() {
            @Override
            public void onClick() {
                setLoading(true);
                onRefreshCallback();
            }
        });
    }

    @Override
    public BaseRecycleViewAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public HotNewsListPresenter initPresenter() {
        return new HotNewsListPresenter();
    }

    @Override
    public void hideLoading() {
        stopRefresh();
        stopLoadMore();
        setLoading(false);
    }


    @Override
    public void showLoading() {
        setLoading(true);
    }

    @Override
    public void loadNotices(List<HotNewsResponse> itemBeans) {
        showErrorPage(false);
        showErrorPageView(itemBeans, 0, null);
        if (!ExtendUtil.listIsNullOrEmpty(itemBeans)) {
            if (itemBeans.size() < ConstantUtils.PAGE_SIZE) {
                setCannotLoad();
            } else {
                mSwipeRefresh.setLoadMoreEnabled(true);
            }
            mItemBeans.addAll(itemBeans);
            mAdapter.notifyDataSetChanged();
        } else {
            setCannotLoad();

        }
    }

    @Override
    public void showLoadFial(int code, String msg) {
        if (!ErrorMsgUtils.showNetErrorPageOrLogin(this, code, msg)) {
            ToastUtil.showShortToast(ZMApplication.getZMContext(), msg);
        }
    }

    private void showErrorPageView(List<HotNewsResponse> itemBeans, int code, String msg) {

        if (!ErrorMsgUtils.showNetErrorPageOrLogin(this, code, msg)) {
            if (ExtendUtil.listIsNullOrEmpty(itemBeans) && mItemBeans.isEmpty()) {
                setErrorPageView(R.drawable.common_img_norecord, getString(R.string.empty_error), null, null);
                return;
            }
        }
    }


}
