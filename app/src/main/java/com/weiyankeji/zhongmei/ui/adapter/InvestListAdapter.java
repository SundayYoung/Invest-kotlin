package com.weiyankeji.zhongmei.ui.adapter;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weiyankeji.library.customview.adapter.BaseRecycleViewAdapter;
import com.weiyankeji.library.customview.adapter.BaseViewHolder;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.mmodel.bean.InvestDetailBean;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.MoneyFormatUtils;
import com.weiyankeji.zhongmei.utils.CommonUtils;

/**
 * Created by liuhaiyang on 2017/8/23.
 */

public class InvestListAdapter extends BaseRecycleViewAdapter<InvestDetailBean, BaseViewHolder> {

    public InvestListAdapter() {
        super(R.layout.item_invest_home_child);
    }
    @Override
    protected void convert(BaseViewHolder holder, InvestDetailBean child) {
        LinearLayout mLlParent = holder.getView(R.id.ll_item_parent);
        mLlParent.setDividerDrawable(null);

        String limit = mContext.getString(R.string.common_double_text,
                CommonUtils.getPeriodTypeText(child.sku_type), child.sku_period);
        holder.setText(R.id.tv_child_days, limit);

        TextView tvRate = holder.getView(R.id.tv_child_rate);
        String rate = MoneyFormatUtils.yieldPointDouble(child.sku_rate);
        int sIndex;
        //add 1.1 加息
        if (child.is_hike_rate == 1 && child.hike_rate != 0) {
            String hike_rate = mContext.getString(R.string.invest_list_hike_rate, MoneyFormatUtils.yieldPointRuleOne(child.hike_rate));
            rate = rate + hike_rate;
            sIndex = rate.length() - (hike_rate.length() + 1);
        } else {
            sIndex = rate.length() - 1;
        }
        tvRate.setText(CommonUtils.changeStringSize(mContext, rate, 18, sIndex, rate.length(), false));

        Button btInvest = holder.getView(R.id.bt_invest);
        holder.addOnClickListener(R.id.bt_invest);
        ImageView ivSellOut = holder.getView(R.id.iv_invest_sell_out);

        if (child.sku_status == ConstantUtils.SKU_TYPE_ING) {
            CommonUtils.buildLabelIcons(mContext, (LinearLayout) holder.getView(R.id.ll_tag), child.sku_tags);
        } else {
            CommonUtils.buildLabelIcons(mContext, (LinearLayout) holder.getView(R.id.ll_tag), child.sku_tags, ContextCompat.getColor(mContext, R.color.gray_8a));
        }
        tvRate.setTextColor(child.sku_status == ConstantUtils.SKU_TYPE_ING ? ContextCompat.getColor(mContext, R.color.red_ff) : ContextCompat.getColor(mContext, R.color.gray_8a));
        btInvest.setVisibility(child.sku_status == ConstantUtils.SKU_TYPE_ING ? View.VISIBLE : View.GONE);
        ivSellOut.setVisibility(child.sku_status == ConstantUtils.SKU_TYPE_ING ? View.GONE : View.VISIBLE);
        ivSellOut.setBackgroundResource(child.sku_status == ConstantUtils.SKU_TYPE_END ? R.drawable.common_icon_invest_end : R.drawable.common_icon_invest_sale_out);
    }
}
