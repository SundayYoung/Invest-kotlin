package com.weiyankeji.zhongmei.ui.fragment;


import android.os.Bundle;

import com.weiyankeji.library.customview.adapter.BaseRecycleViewAdapter;
import com.weiyankeji.library.customview.adapter.BaseViewHolder;
import com.weiyankeji.library.utils.ExtendUtil;
import com.weiyankeji.library.utils.TimeUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.customview.CostomDecoration;
import com.weiyankeji.zhongmei.ui.customview.ErrorPageView;
import com.weiyankeji.zhongmei.ui.mmodel.bean.InvestRecordResponse;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestRecordRequest;
import com.weiyankeji.zhongmei.ui.mpresenter.InvestRecordPresenter;
import com.weiyankeji.zhongmei.ui.mview.InvestRecordView;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;
import com.weiyankeji.zhongmei.utils.MoneyFormatUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 投资记录
 */
public class InvestRecordFragment extends BaseListFragment<InvestRecordView, InvestRecordPresenter> implements InvestRecordView {

    private CusAdapter mAdapter;
    private List<InvestRecordResponse> mItemBeans = new ArrayList<>();
    private InvestRecordPresenter mRecordPresenter;

    private InvestRecordRequest mRequest;
    private String mSkuId;
    private int mPage = 1;

    @Override
    public void onRefreshCallback() {
        mItemBeans.clear();
        mPage = 1;
        loadData();
    }

    @Override
    public void onLoadCallback() {
        mPage++;
        loadData();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mSkuId = savedInstanceState.getString(ConstantUtils.KEY_ID);
        }
        setTitle(getString(R.string.invest_detail_note), true);
        mAdapter = new CusAdapter();
        mRecordPresenter = ((InvestRecordPresenter) mPresenter);
        getRecyclerView().removeItemDecoration(mRecyclerViewDecoration);
        mRecyclerView.addItemDecoration(new CostomDecoration(mContext, R.dimen.margin_big, R.dimen.margin_big));

        setLoading(true);
        loadData();
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
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
        stopRefresh();
        stopLoadMore();
        setLoading(false);
    }


    @Override
    public InvestRecordPresenter initPresenter() {
        return new InvestRecordPresenter();
    }

    @Override
    public void setData(List<InvestRecordResponse> responses) {
        if (ExtendUtil.listIsNullOrEmpty(responses)) {
            setErrorPageView(R.drawable.common_img_norecord, getString(R.string.invest_record_empty_tip), getString(R.string.invest_record_go), new ErrorPageView.OnErrorFunctionClickListener() {
                @Override
                public void onClick() {
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                }
            });
            return;
        }
        mItemBeans.addAll(responses);
        mAdapter.notifyDataSetChanged();
        if (responses.size() < ConstantUtils.PAGE_SIZE) {
            setCannotLoad();
        }
    }

    @Override
    public void responseFail(int code, String msg) {
        if (!ErrorMsgUtils.showNetErrorPageOrLogin(this, code, msg)) {
            ToastUtil.showShortToast(ZMApplication.getZMApplication(), msg);
        }
    }

    private void loadData() {
        if (mRequest == null) {
            mRequest = new InvestRecordRequest();
        }
        mRequest.sku_id = mSkuId;
        mRequest.page = mPage;
        mRecordPresenter.loadData(mRequest);
    }

    private class CusAdapter extends BaseRecycleViewAdapter<InvestRecordResponse, BaseViewHolder> {

        CusAdapter() {
            super(R.layout.item_invest_record, mItemBeans);
        }

        @Override
        protected void convert(BaseViewHolder helper, InvestRecordResponse item) {
            helper.setText(R.id.item_invest_record_phone, item.investor);
            helper.setText(R.id.item_invest_record_money, getString(R.string.common_yuan, MoneyFormatUtils.decimalFormatRuleOne(item.invest_amount)));
            helper.setText(R.id.item_invest_record_time, TimeUtil.getAllFormatStr(item.invest_time));
        }
    }
}