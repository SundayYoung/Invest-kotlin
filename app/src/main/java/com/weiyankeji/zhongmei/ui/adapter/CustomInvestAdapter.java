package com.weiyankeji.zhongmei.ui.adapter;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.weiyankeji.library.customview.adapter.BaseRecycleViewAdapter;
import com.weiyankeji.library.customview.adapter.BaseViewHolder;
import com.weiyankeji.library.utils.TimeUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.mmodel.bean.CustomInvestBean;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.MoneyFormatUtils;

import java.util.List;

/**
 * @aythor: lilei
 * time: 2017/8/31  下午4:01
 * function:
 */

public class CustomInvestAdapter extends BaseRecycleViewAdapter<CustomInvestBean, BaseViewHolder> {

    int mTypeTAg; //定期、定制type
    Activity mActivity;
    int mType;    //投资中、已结束type
    String mComP;

    public CustomInvestAdapter(@LayoutRes int layoutResId, @Nullable List<CustomInvestBean> data, int mTypeTAg, Activity mActivity, int mType) {
        super(layoutResId, data);
        this.mTypeTAg = mTypeTAg;
        this.mActivity = mActivity;
        this.mType = mType;
        mComP = mActivity.getString(R.string.yuan);
    }

    @Override
    protected void convert(BaseViewHolder helper, CustomInvestBean item) {
        /*起息时间  tv_stime
        * 结束时间  tv_otime
        * 投资本金  tv_haved_invert
        * 代收利息  tv_must_invert
        * 代收本息  tv_invert
        * */
        helper.setText(R.id.tv_title, item.sku_title);
        helper.setText(R.id.tv_type, item.sku_status);
        helper.setText(R.id.tv_haved_invert, MoneyFormatUtils.decimalFormatRuleOne(item.invest_amount));
        helper.setText(R.id.tv_stime, TimeUtil.getCostomFormatTime(item.start_interest_time, ConstantUtils.TIME_FORMAT_POINT));
        helper.setText(R.id.tv_otime, TimeUtil.getCostomFormatTime(item.end_interest_time, ConstantUtils.TIME_FORMAT_POINT));
        switch (mTypeTAg) {
            case ConstantUtils.STYPE_INVESTMENT:
                helper.setText(R.id.tv_invest_money_start, mContext.getString(R.string.custom_prin_inter_received));
                helper.setText(R.id.textView_must, mContext.getString(R.string.mine_interest_received_assets));
                helper.setTextColor(R.id.tv_type, ContextCompat.getColor(mActivity, R.color.blue_00));
                helper.setText(R.id.tv_invert, MoneyFormatUtils.decimalFormatRuleOne(item.wait_principal) + mComP);
                helper.setText(R.id.tv_must_invert, MoneyFormatUtils.decimalFormatRuleOne(item.wait_interest));
                break;
            case ConstantUtils.STYPE_ISOVER:
                helper.setText(R.id.tv_invest_money_start, mContext.getString(R.string.custom_net_prin_inter_received));
                helper.setText(R.id.textView_must, mContext.getString(R.string.custom_net_original_invest));
                helper.setTextColor(R.id.tv_type, ContextCompat.getColor(mActivity, R.color.gray_8a));
                helper.setText(R.id.tv_invert, MoneyFormatUtils.decimalFormatRuleOne(item.actual_principal) + mComP);
                helper.setText(R.id.tv_must_invert, MoneyFormatUtils.decimalFormatRuleOne(item.actual_interest));
                break;
            default:
                break;
        }
        if (item.status == ConstantUtils.INVESTMENT_IN_INVESTMENT) {
            helper.setText(R.id.tv_otime, mContext.getString(R.string.money_seat));
            helper.setText(R.id.tv_must_invert, mContext.getString(R.string.money_seat));
        } else {
            helper.setText(R.id.tv_otime, item.end_interest_time == 0 ? mContext.getString(R.string.time_invalid) : TimeUtil.getCostomFormatTime(item.end_interest_time, ConstantUtils.TIME_FORMAT_POINT));
        }

        if (item.invest_fail_msg != null && !item.invest_fail_msg.isEmpty()) {
            helper.getView(R.id.ll_failure).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_failure, item.invest_fail_msg);
        } else {
            helper.getView(R.id.ll_failure).setVisibility(View.GONE);
        }

    }

}
