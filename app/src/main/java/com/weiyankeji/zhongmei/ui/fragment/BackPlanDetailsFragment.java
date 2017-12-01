package com.weiyankeji.zhongmei.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.adapter.BackPlanAdapter;
import com.weiyankeji.zhongmei.ui.customview.ErrorPageView;
import com.weiyankeji.zhongmei.ui.mmodel.BackPlanResponse;
import com.weiyankeji.zhongmei.ui.mmodel.bean.BackPlanScheduleBean;
import com.weiyankeji.zhongmei.ui.mmodel.bean.CustomInvestBean;
import com.weiyankeji.zhongmei.ui.mmodel.mine.BackPlanRequest;
import com.weiyankeji.zhongmei.ui.mpresenter.BackPlanPresenter;
import com.weiyankeji.zhongmei.ui.mview.BackPlanView;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.MoneyFormatUtils;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;

/**
 * @aythor: lilei
 * time: 2017/9/10  下午4:10
 * function:
 */

public class BackPlanDetailsFragment extends BaseMvpFragment<BackPlanView, BackPlanPresenter> implements BackPlanView {
    @BindString(R.string.custom_details_back_plan)
    String mTitle;

    @BindView(R.id.rlv_recycler)
    RecyclerView mRlvRecycle;
    @BindView(R.id.tv_custom_prinvipal)
    TextView mTvCustomPrin;
    @BindView(R.id.tv_interest_received)
    TextView mTvInterest;

    BackPlanPresenter mBackPlanPresenter;
    CustomInvestBean mCustomInvetBean;
    BackPlanAdapter mBackPlanAdapter;
    ArrayList<BackPlanScheduleBean> mList = new ArrayList<>();
    BackPlanRequest mBackPlanRequest;


    @Override
    public int setContentLayout() {
        return R.layout.fragment_backplan_details;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        setTitle(mTitle);
        addBackListener();
        mCustomInvetBean = (CustomInvestBean) bundle.getSerializable(ConstantUtils.SKEY_CUSTOM_DETAILS);
        setRecycler();
    }

    public void setRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRlvRecycle.setLayoutManager(layoutManager);
        mBackPlanAdapter = new BackPlanAdapter(getActivity(), R.layout.item_backplan_details, mList);
        mBackPlanRequest = new BackPlanRequest(mCustomInvetBean.invest_id);
        mBackPlanPresenter.loadData(mBackPlanRequest, this);
        mRlvRecycle.setAdapter(mBackPlanAdapter);
        getErrorPageView().setOnErrorFunctionClickListener(new ErrorPageView.OnErrorFunctionClickListener() {
            @Override
            public void onClick() {
                mBackPlanPresenter.loadData(mBackPlanRequest, BackPlanDetailsFragment.this);
            }
        });


    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
    @Override
    public void setData(BackPlanResponse backPlanResponse) {
        mTvCustomPrin.setText(MoneyFormatUtils.decimalFormatRuleOne(backPlanResponse.invest_amount));
        mTvInterest.setText(MoneyFormatUtils.decimalFormatRuleOne(backPlanResponse.prospective_earnings));
        mList.addAll(backPlanResponse.schedule);
        mBackPlanAdapter.notifyDataSetChanged();
    }

    @Override
    public BackPlanPresenter initPresenter() {
        mBackPlanPresenter = new BackPlanPresenter();
        return mBackPlanPresenter;
    }
}
