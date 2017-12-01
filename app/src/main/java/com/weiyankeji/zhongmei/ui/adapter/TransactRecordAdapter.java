package com.weiyankeji.zhongmei.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.weiyankeji.library.customview.adapter.BaseRecycleViewAdapter;
import com.weiyankeji.library.customview.adapter.BaseViewHolder;
import com.weiyankeji.library.utils.TimeUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.mmodel.mine.TradeRecordResponse;
import com.weiyankeji.zhongmei.utils.MoneyFormatUtils;

import java.util.List;


/**
 * Created by caiwancheng on 2017/8/2.
 */

public class TransactRecordAdapter extends BaseRecycleViewAdapter<TradeRecordResponse, BaseViewHolder> {

    private int mOrangeColor;
    private int mGreenColor;

    public TransactRecordAdapter(Context context, List<TradeRecordResponse> beans) {
        super(R.layout.item_transact_record, beans);
        mOrangeColor = ContextCompat.getColor(context, R.color.red_ff);
        mGreenColor = ContextCompat.getColor(context, R.color.green_3b);
    }

    @Override
    protected void convert(BaseViewHolder helper, TradeRecordResponse item) {
        if (item != null) {
            helper.setText(R.id.tv_transact_record_title, item.title);
            helper.setText(R.id.tv_transact_record_id, mContext.getString(R.string.transact_id) + item.id);
            helper.setText(R.id.tv_transact_record_time, TimeUtil.getAllFormatStr(item.datetime));

            TextView tvAmount = helper.getView(R.id.tv_transact_record_money);
            if (item.amount < 0) {
                tvAmount.setTextColor(mGreenColor);
                tvAmount.setText(MoneyFormatUtils.decimalFormatRuleOne(item.amount));
            } else {
                tvAmount.setTextColor(mOrangeColor);
                tvAmount.setText("+" + MoneyFormatUtils.decimalFormatRuleOne(item.amount));
            }


        }
    }
}
