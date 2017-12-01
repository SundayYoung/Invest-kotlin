package com.weiyankeji.zhongmei.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weiyankeji.library.log.LogUtils;
import com.weiyankeji.library.utils.TimeUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.activity.FragmentContainerActivity;
import com.weiyankeji.zhongmei.ui.mmodel.bean.CustomInvestBean;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.MoneyFormatUtils;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @aythor: lilei
 * time: 2017/8/25  下午7:11
 * function:
 */

public class CustomDetailsFragment extends BaseFragment {
    @BindString(R.string.custom_details_title)
    String mTitle;
    @BindView(R.id.tv_custom_prinvipal)
    TextView mTvCustomPp;
    @BindView(R.id.tv_interest_received)
    TextView mTvInterest;
    @BindView(R.id.tv_custom_accumulated)
    TextView mTvCustomAl;
    @BindView(R.id.rl_regular)
    RelativeLayout mRlRegular;
    @BindView(R.id.rl_invest_agreement)
    RelativeLayout mRlAgreen;
    @BindView(R.id.rl_friend_remind)
    RelativeLayout mRlFriendRemind;
    @BindView(R.id.iv_image_margin)
    ImageView mIvMargin;

    @BindView(R.id.tv_regular)
    TextView mTvRegular;
    @BindView(R.id.tv_expect)
    TextView mTvExpect;
    @BindView(R.id.tv_begin)
    TextView mTvBegin;
    @BindView(R.id.tv_over)
    TextView mTvOver;
    @BindView(R.id.tv_back_accout)
    TextView mTvBackAccout;


    @BindView(R.id.tv_prinvipal_title)
    TextView mTvPriTitle;
    @BindView(R.id.tv__accumulated_title)
    TextView mTvAccTitle;

    int mTypeTag;


    CustomInvestBean mCustonInvestBean;

    @Override
    public int setContentLayout() {
        return R.layout.fragment_custom_details;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        setTitle(mTitle);
        addBackListener();
        setDataForBundle(bundle);

    }

    /**
     * 填充数据
     *
     * @param bundle
     */
    public void setDataForBundle(Bundle bundle) {

        mCustonInvestBean = (CustomInvestBean) bundle.getSerializable(ConstantUtils.SKEY_CUSTOM_DETAILS);
        mTypeTag = bundle.getInt(CustomInvestmentFragment.STYPE);
        int status = mCustonInvestBean.status;
        switch (mTypeTag) {
            case ConstantUtils.STYPE_INVESTMENT:
                mTvPriTitle.setText(getString(R.string.custom_prin_inter_received_blanck));
                mTvAccTitle.setText(getString(R.string.mine_interest_received));
                mTvCustomPp.setText(MoneyFormatUtils.decimalFormatRuleOne(mCustonInvestBean.wait_principal));
                mTvCustomAl.setText(MoneyFormatUtils.decimalFormatRuleOne(mCustonInvestBean.wait_interest));
                break;
            case ConstantUtils.STYPE_ISOVER:
                mTvPriTitle.setText(getString(R.string.custom_net_prin_inter_received_blanck));
                mTvAccTitle.setText(getString(R.string.custom_net_original_invest_blanck));
                mTvCustomPp.setText(MoneyFormatUtils.decimalFormatRuleOne(mCustonInvestBean.actual_principal));
                mTvCustomAl.setText(MoneyFormatUtils.decimalFormatRuleOne(mCustonInvestBean.actual_interest));
                break;
            default:
                break;
        }
        mTvInterest.setText(MoneyFormatUtils.decimalFormatRuleOne(mCustonInvestBean.invest_amount));
        mTvRegular.setText(mCustonInvestBean.sku_title);
        mTvExpect.setText(MoneyFormatUtils.yieldPointDouble(mCustonInvestBean.sku_rate));
        mTvBegin.setText(TimeUtil.getCostomFormatTime(mCustonInvestBean.start_interest_time, ConstantUtils.TIME_FORMAT_DEX));
        mTvOver.setText(TimeUtil.getCostomFormatTime(mCustonInvestBean.end_interest_time, ConstantUtils.TIME_FORMAT_DEX));
        mTvBackAccout.setText(TimeUtil.getCostomFormatTime(mCustonInvestBean.back_time, ConstantUtils.TIME_FORMAT_DEX));
        mRlFriendRemind.setVisibility(View.GONE);

        switch (status) {
            case ConstantUtils.INVESTMENT_IN_INVESTMENT:
                mTvCustomAl.setText(getString(R.string.money_seat));
                mRlRegular.setVisibility(View.GONE);
                mIvMargin.setVisibility(View.GONE);
                mRlAgreen.setVisibility(View.GONE);
                mTvOver.setText(getString(R.string.money_seat));
                mTvBackAccout.setText(getString(R.string.money_seat));
                mRlFriendRemind.setVisibility(View.VISIBLE);
                mRlFriendRemind.setVisibility(View.GONE);
                break;
            case ConstantUtils.ISOVER_FAILURE:
                mRlRegular.setVisibility(View.GONE);
                mIvMargin.setVisibility(View.GONE);
                mRlAgreen.setVisibility(View.GONE);
                break;
            case ConstantUtils.INVESTMENT_BACK_PAYMENT_3:
            case ConstantUtils.INVESTMENT_BACK_PAYMENT_4:
            case ConstantUtils.INVESTMENT_BACK_PAYMENT_5:
                if (mCustonInvestBean.end_interest_time == 0) {
                    mTvOver.setText(getString(R.string.time_invalid));
                    mTvBackAccout.setText(getString(R.string.time_invalid));

                }
                break;
            default:
                break;
        }
        mTvBegin.setText(mCustonInvestBean.start_interest_time == 0 ? getString(R.string.time_invalid) : TimeUtil.getCostomFormatTime(mCustonInvestBean.start_interest_time, ConstantUtils.TIME_FORMAT_DEX));
        mTvOver.setText(mCustonInvestBean.end_interest_time == 0 ? getString(R.string.time_invalid) : TimeUtil.getCostomFormatTime(mCustonInvestBean.end_interest_time, ConstantUtils.TIME_FORMAT_DEX));
        mTvBackAccout.setText(mCustonInvestBean.back_time == 0 ? getString(R.string.time_invalid) : TimeUtil.getCostomFormatTime(mCustonInvestBean.back_time, ConstantUtils.TIME_FORMAT_DEX));
    }

    @OnClick({R.id.rl_regular, R.id.rl_invest_agreement})
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        Intent intent;
        switch (view.getId()) {
            case R.id.rl_regular:
                bundle.putSerializable(ConstantUtils.SKEY_CUSTOM_DETAILS, mCustonInvestBean);
                intent = FragmentContainerActivity.getFragmentContainerActivityIntent(getActivity(), BackPlanDetailsFragment.class, bundle);
                startActivity(intent);
                break;
            case R.id.rl_invest_agreement:
                CommonUtils.intentWebView(CustomDetailsFragment.this, getString(R.string.custom_details_invest_agreement), mCustonInvestBean.invest_protocol);
                break;
            default:
                break;
        }
    }
}
