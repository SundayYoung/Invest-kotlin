package com.weiyankeji.zhongmei.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weiyankeji.library.customview.swipetoloadlayout.OnRefreshListener;
import com.weiyankeji.library.customview.swipetoloadlayout.SwipeToLoadLayout;
import com.weiyankeji.library.utils.ExtendUtil;
import com.weiyankeji.library.utils.ImageLoaderUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.activity.FragmentContainerActivity;
import com.weiyankeji.zhongmei.ui.customview.ErrorPageView;
import com.weiyankeji.zhongmei.ui.customview.TwitterRefreshHeaderView;
import com.weiyankeji.zhongmei.ui.mmodel.bean.InvestTagsBean;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestWithIdRequest;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestDetailResponse;
import com.weiyankeji.zhongmei.ui.mpresenter.InvestDetailPresenter;
import com.weiyankeji.zhongmei.ui.mview.InvestDetailView;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;
import com.weiyankeji.zhongmei.utils.MoneyFormatUtils;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.UserUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 投资 项目详情
 * Created by liuhaiyang on 2017/8/22.
 */

public class InvestProductDetailFragment extends BaseMvpFragment<InvestDetailView, InvestDetailPresenter> implements InvestDetailView, OnRefreshListener {

    private static final int INTENT_REQUEST = 2000;

    @BindView(R.id.stl_detail)
    SwipeToLoadLayout mSwipeToLoadLayout;

    @BindView(R.id.tv_detail_rate)
    TextView mTvDetailRate;
    @BindView(R.id.ll_detail_tags)
    LinearLayout mLlTags;
    @BindView(R.id.tv_detail_rate_tip)
    TextView mTvDetailRateTip;
    //项目介绍 内容
    @BindView(R.id.tv_detail_product_name)
    TextView mTvProductName;
    //项目金额
    @BindView(R.id.tv_detail_total_money)
    TextView mTvDetailTotalMoney;

    @BindView(R.id.tv_detail_repay_type)
    TextView mTvDetailRepayType;
    @BindView(R.id.tv_detail_start_money)
    TextView mTvDetailStartMoney;
    @BindView(R.id.tv_detail_sec_title)
    TextView mTvDetailSecTitle;
    //募集结束时间
    @BindView(R.id.tv_invest_detail_raise_state)
    TextView mTvEndState;
    @BindView(R.id.tv_invest_detail_raise_time_one)
    TextView mTvEndTimeOne;
    @BindView(R.id.tv_invest_detail_raise_time_one_unit)
    TextView mTvEndTimeOneUnit;
    @BindView(R.id.tv_invest_detail_raise_time_two)
    TextView mTvEndTimeTwo;
    @BindView(R.id.tv_invest_detail_raise_time_two_unit)
    TextView mTvEndTimeTwoUnit;

    //项目期限
    @BindView(R.id.tv_detail_end_time_value)
    TextView mTvTimeEnd;
    @BindView(R.id.tv_detail_sec_desc)
    TextView mTvDetailSecDesc;
    @BindView(R.id.iv_detail_sec_icon)
    ImageView mIvDetailSecIcon;
    @BindView(R.id.tv_detail_more_introduce)
    TextView mTvDetailMoreIntroduce;
    @BindView(R.id.tv_detail_more_wind)
    TextView mTvDetailMoreWind;
    @BindView(R.id.tv_detail_more_warn)
    TextView mTvDetailMoreWarn;
    @BindView(R.id.tv_detail_more_note)
    TextView mTvDetailMoreNote;
    @BindView(R.id.tv_last_money)
    TextView mTvDetailInvestGo;
    //立即投资
    @BindView(R.id.ll_left)
    LinearLayout mLlInvestLeft;
    @BindView(R.id.bt_invest)
    Button mBtInvest;

    private InvestDetailResponse mResponse;

    private String mSkuId;

    @Override
    public int setContentLayout() {
        return R.layout.fragment_invest_detail;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        Bundle bde = getActivity().getIntent().getBundleExtra(FragmentContainerActivity.STAG_FRAGMENT_ARGUMENTS);
        if (bde != null) {
            mSkuId = bde.getString(ConstantUtils.KEY_ID);
        }

        setTitle(R.string.invest_detail_title, true);
        setLoading(true);
        mPresenter.loadData(new InvestWithIdRequest(mSkuId));

        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setRefreshFinalDragOffset(TwitterRefreshHeaderView.REFRESH_MAX_DRAG_OFFSET);

        getErrorPageView().setOnErrorFunctionClickListener(new ErrorPageView.OnErrorFunctionClickListener() {
            @Override
            public void onClick() {
                mPresenter.loadData(new InvestWithIdRequest(mSkuId));
            }
        });
    }


    @Override
    public InvestDetailPresenter initPresenter() {
        return new InvestDetailPresenter();
    }


    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
        if (mSwipeToLoadLayout.isRefreshing()) {
            mSwipeToLoadLayout.setRefreshing(false);
        }
        setLoading(false);
    }

    @OnClick({R.id.tv_detail_more_introduce, R.id.tv_detail_more_wind, R.id.tv_detail_more_warn,
            R.id.tv_detail_more_note, R.id.bt_invest, R.id.rl_safe})
    public void onViewClicked(View view) {
        Bundle parm = new Bundle();
        parm.putString(ConstantUtils.KEY_ID, mSkuId);
        switch (view.getId()) {
            case R.id.rl_safe:
                parm.putString(ConstantUtils.KEY_TITLE, getString(R.string.invest_detail_security));
                startFragment(InvestProductIntroductionFragment.class, parm);
                break;
            case R.id.tv_detail_more_introduce:
                parm.putString(ConstantUtils.KEY_TITLE, getString(R.string.invest_detail_introduction));
                parm.putSerializable(ConstantUtils.KEY_DATA, mResponse);
                startFragment(InvestProductIntroductionFragment.class, parm);
                break;
            case R.id.tv_detail_more_wind:
                parm.putString(ConstantUtils.KEY_TITLE, getString(R.string.invest_detail_wind_contro));
                startFragment(InvestProductIntroductionFragment.class, parm);
                break;
            case R.id.tv_detail_more_warn:
                parm.putString(ConstantUtils.KEY_TITLE, getString(R.string.invest_detail_warn));
                startFragment(InvestProductIntroductionFragment.class, parm);
                break;
            case R.id.tv_detail_more_note:
                parm.putString(ConstantUtils.KEY_ID, mSkuId);
                startFragment(InvestRecordFragment.class, parm);
                break;
            case R.id.bt_invest:
                if (!UserUtils.isUserLogin()) {
                    CommonUtils.intentLoginFragment(this, -1);
                    return;
                }
                parm.putSerializable(ConstantUtils.KEY_DATA, mResponse);
                startFragmentForResult(InvestMentFragment.class, parm, INTENT_REQUEST);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST && resultCode == ConstantUtils.INTENT_RESPONSE) {
            onRefresh();
        }
    }

    @Override
    public void setData(InvestDetailResponse response) {
        mResponse = response;
        //title
        mTvProductName.setText(response.sku_title);
        //rate
        String rate = MoneyFormatUtils.yieldPointDouble(response.sku_rate);
        int hikeLength = 0;
        if (response.is_hike_rate == 1 && response.hike_rate != 0) {
            String hikeRate = getString(R.string.invest_list_hike_rate, MoneyFormatUtils.yieldPointRuleOne(response.hike_rate));
            hikeLength = hikeRate.length();
            rate = rate + hikeRate;
        }
        mTvDetailRate.setText(CommonUtils.changeStringSize(mContext, rate, 14, rate.length() - hikeLength, rate.length(), false));
        //标签
        if (!ExtendUtil.listIsNullOrEmpty(response.sku_tags)) {
            for (InvestTagsBean tagsBean : response.sku_tags) {
                tagsBean.color = getString(R.string.common_color_withe);
            }
            CommonUtils.buildLabelIcons(mContext, mLlTags, response.sku_tags, R.color.tran, 12);
        }
        //项目金额
        mTvDetailTotalMoney.setText(getString(R.string.common_double_text, MoneyFormatUtils.unitRule(response.sku_amount), MoneyFormatUtils.getMoneyUnit(response.sku_amount)));
        //还款方式
        mTvDetailRepayType.setText(response.repay_type);
        //起投金额
        mTvDetailStartMoney.setText(getString(R.string.common_yuan, MoneyFormatUtils.lFormatYuanNoPoint(response.min_invest_amount)));
        //项目期限
        mTvTimeEnd.setText(response.sku_period);
        //安全保障
        mTvDetailSecTitle.setText(response.security_name);
        mTvDetailSecDesc.setText(response.security_intro);
        ImageLoaderUtil.loadImageFromUrl(mContext, response.security_logo, mIvDetailSecIcon);
        //剩余可投
        if (response.sku_status == ConstantUtils.SKU_TYPE_ING) {
            String strLast = MoneyFormatUtils.decimalFormatRuleOne(response.c_sku_amount);
            mTvDetailInvestGo.setText(getString(R.string.common_yuan, strLast));
            //募集剩余时间 单位：秒
            if (mPresenter.isLastTimeMoreThanADay(response.remain_raise_time)) {
                mTvEndTimeOne.setText(mPresenter.getDayText(response.remain_raise_time));
                mTvEndTimeOneUnit.setText(getString(R.string.common_unit_time_day));
                mTvEndTimeTwo.setText(mPresenter.getHourText(response.remain_raise_time));
                mTvEndTimeTwoUnit.setText(getString(R.string.common_unit_time_hour));
            } else {
                mTvEndTimeOne.setText(mPresenter.getHourText(response.remain_raise_time));
                mTvEndTimeOneUnit.setText(getString(R.string.common_unit_time_hour));
                mTvEndTimeTwo.setText(mPresenter.getMinuteText(response.remain_raise_time));
                mTvEndTimeTwoUnit.setText(getString(R.string.common_unit_time_minute));
            }
        } else {
            mLlInvestLeft.setVisibility(View.GONE);
            mBtInvest.setText(response.sku_status == ConstantUtils.SKU_TYPE_END ? getString(R.string.invest_home_collect_over) : getString(R.string.invest_detail_sellout));
            mBtInvest.setBackgroundResource(R.drawable.shape_round_button_bg_gray_e1);
            mBtInvest.setTextColor(ContextCompat.getColor(mContext, R.color.gray_c2));
            mBtInvest.setClickable(false);
        }

        //募集时间状态
        mTvEndState.setText(response.sku_status == ConstantUtils.SKU_TYPE_ING ? getString(R.string.invest_detail_fund_key) : getString(R.string.invest_home_collect_over));
        mTvEndState.setAlpha(response.sku_status == ConstantUtils.SKU_TYPE_ING ? 1 : 0.8f);
        mTvEndTimeOne.setVisibility(response.sku_status == ConstantUtils.SKU_TYPE_ING ? View.VISIBLE : View.GONE);
        mTvEndTimeOneUnit.setVisibility(response.sku_status == ConstantUtils.SKU_TYPE_ING ? View.VISIBLE : View.GONE);
        mTvEndTimeTwo.setVisibility(response.sku_status == ConstantUtils.SKU_TYPE_ING ? View.VISIBLE : View.GONE);
        mTvEndTimeTwoUnit.setVisibility(response.sku_status == ConstantUtils.SKU_TYPE_ING ? View.VISIBLE : View.GONE);

    }

    @Override
    public void netError(int code, String msg) {
        if (!ErrorMsgUtils.showNetErrorPageOrLogin(this, code, msg)) {
            ToastUtil.showShortToast(ZMApplication.getZMApplication(), msg);
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.loadData(new InvestWithIdRequest(mSkuId));
    }
}
