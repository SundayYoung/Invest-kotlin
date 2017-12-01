package com.weiyankeji.zhongmei.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;

import com.weiyankeji.library.customview.adapter.BaseRecycleViewAdapter;
import com.weiyankeji.library.utils.ExtendUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.adapter.TransactRecordAdapter;
import com.weiyankeji.zhongmei.ui.customview.CostomDecoration;
import com.weiyankeji.zhongmei.ui.customview.ErrorPageView;
import com.weiyankeji.zhongmei.ui.mmodel.mine.TradeRecordResponse;
import com.weiyankeji.zhongmei.ui.mpresenter.TradeRecordPresenter;
import com.weiyankeji.zhongmei.ui.mview.TradeRecordView;
import com.weiyankeji.zhongmei.utils.ConstantUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易记录
 * Created by caiwancheng on 2017/8/21.
 */

public class TransactRecordFragment extends BaseListFragment<TradeRecordView, TradeRecordPresenter> implements TradeRecordView {

    private List<TradeRecordResponse> mItemBeans = new ArrayList<>();

    private TransactRecordAdapter mAdapter;

    private TradeRecordPresenter mTradePresenter;

    @Override
    public void onRefreshCallback() {
        mItemBeans.clear();
        mTradePresenter.loadRefresh();
    }

    @Override
    public void onLoadCallback() {
        mTradePresenter.loadMore();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        setTitle(getString(R.string.transact_record), true);
        setCannotRefresh();

        mAdapter = new TransactRecordAdapter(getActivity(), mItemBeans);
        mTradePresenter = ((TradeRecordPresenter) mPresenter);
        mTradePresenter.loadMore();
        setLoading(true);
        getErrorPageView().setOnErrorFunctionClickListener(new ErrorPageView.OnErrorFunctionClickListener() {
            @Override
            public void onClick() {
                mTradePresenter.loadRefresh();
            }
        });

        getRecyclerView().removeItemDecoration(mRecyclerViewDecoration);
        getRecyclerView().addItemDecoration(new CostomDecoration(mContext, R.dimen.margin_big, 0));
    }


    @Override
    public BaseRecycleViewAdapter getAdapter() {
        return mAdapter;
    }


    @Override
    public TradeRecordPresenter initPresenter() {
        return new TradeRecordPresenter(this);
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
        setLoading(false);
        stopRefresh();
        stopLoadMore();
    }

    @Override
    public void loadRecords(List<TradeRecordResponse> itemBeans) {
        showContentView(true);
        if (!ExtendUtil.listIsNullOrEmpty(itemBeans)) {
            if (itemBeans.size() < ConstantUtils.PAGE_SIZE) {
                setCannotLoad();
            }
            mItemBeans.addAll(itemBeans);
            mAdapter.notifyDataSetChanged();
        } else {
            setErrorPageView(R.drawable.common_img_norecord, getString(R.string.no_transact_record), null, null);
            setCannotLoad();
        }
        stopLoadMore();
    }

}
