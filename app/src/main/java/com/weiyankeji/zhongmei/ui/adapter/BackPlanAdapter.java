package com.weiyankeji.zhongmei.ui.adapter;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.weiyankeji.library.customview.adapter.BaseRecycleViewAdapter;
import com.weiyankeji.library.customview.adapter.BaseViewHolder;
import com.weiyankeji.library.utils.TimeUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.mmodel.bean.BackPlanScheduleBean;
import com.weiyankeji.zhongmei.utils.MoneyFormatUtils;

import java.util.List;

/**
 * @aythor: lilei
 * time: 2017/9/10  下午7:01
 * function:
 */

public class BackPlanAdapter extends BaseRecycleViewAdapter<BackPlanScheduleBean, BaseViewHolder> {

    Activity mActivity;

    public BackPlanAdapter(Activity mActivity, @LayoutRes int layoutResId, @Nullable List<BackPlanScheduleBean> data) {
        super(layoutResId, data);
        this.mActivity = mActivity;
    }


    @Override
    protected void convert(BaseViewHolder helper, BackPlanScheduleBean item) {
        String mPriod = mActivity.getString(R.string.custom_details_rule_period, item.period + "");
        helper.setText(R.id.tv_phase, mPriod);
        helper.setText(R.id.tv_start_time, TimeUtil.getYearMonthDayFormatStr(item.back_time));
        helper.setText(R.id.tv_pay_money, MoneyFormatUtils.decimalFormatRuleOne(item.back_total_amount));
        helper.setText(R.id.tv_pay_type, mActivity.getString(R.string.custom_details_rule_amount));
        helper.getView(R.id.iv_line).setVisibility(helper.getLayoutPosition() == 0 ? View.GONE : View.VISIBLE);
    }
}
