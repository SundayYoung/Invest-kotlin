package com.weiyankeji.zhongmei.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.mmodel.bean.InvestDetailBean;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestHomeSkuList;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.MoneyFormatUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuhaiyang on 2017/8/21.
 */
public class InvestHomeAdapter extends BaseExpandableListAdapter implements View.OnClickListener {

    private Context mContext;

    private List<InvestHomeSkuList> mList;

    private OnItemButtonClickListener mListener;

    public InvestHomeAdapter(Context context, OnItemButtonClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void updateAdapter(List<InvestHomeSkuList> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mList == null) {
            return 0;
        }
        InvestHomeSkuList child = mList.get(groupPosition);
        return child == null || child.list == null ? 0 : child.list.size();
    }

    @Override
    public InvestHomeSkuList getGroup(int groupPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            return null;
        }
        return mList.get(groupPosition);
    }

    @Override
    public InvestDetailBean getChild(int groupPosition, int childPosition) {
        InvestHomeSkuList groupList = getGroup(groupPosition);
        if (groupList == null || groupList.list == null || childPosition < 0 || childPosition >= getChildrenCount(groupPosition)) {
            return null;
        }
        return groupList.list.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null || !(convertView.getTag() instanceof GroupViewHolder)) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_invest_home_group, parent, false);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        InvestHomeSkuList groupList = getGroup(groupPosition);
        if (groupList == null) {
            return convertView;
        }
        groupViewHolder.mTvTitle.setText(groupList.title);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_invest_home_child, parent, false);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        InvestDetailBean child = getChild(groupPosition, childPosition);
        if (child == null) {
            return convertView;
        }

        String limit = mContext.getString(R.string.common_double_text,
                CommonUtils.getPeriodTypeText(child.sku_type), child.sku_period);
        childViewHolder.mTvDate.setText(limit);

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
        childViewHolder.mTvRate.setText(CommonUtils.changeStringSize(mContext, rate, 18, sIndex, rate.length(), false));

        Button btInvest = childViewHolder.mBtInvest;
        btInvest.setTag(child);
        btInvest.setOnClickListener(this);
        ImageView ivSellOut = childViewHolder.mIvSellOut;

        if (child.sku_status == ConstantUtils.SKU_TYPE_ING) {
            CommonUtils.buildLabelIcons(mContext, childViewHolder.mLlTag, child.sku_tags);
        } else {
            CommonUtils.buildLabelIcons(mContext, childViewHolder.mLlTag, child.sku_tags, ContextCompat.getColor(mContext, R.color.gray_8a));
        }
        childViewHolder.mTvRate.setTextColor(child.sku_status == ConstantUtils.SKU_TYPE_ING ? ContextCompat.getColor(mContext, R.color.red_ff) : ContextCompat.getColor(mContext, R.color.gray_8a));

        btInvest.setVisibility(child.sku_status == ConstantUtils.SKU_TYPE_ING ? View.VISIBLE : View.GONE);
        ivSellOut.setVisibility(child.sku_status == ConstantUtils.SKU_TYPE_ING ? View.GONE : View.VISIBLE);
        ivSellOut.setBackgroundResource(child.sku_status == ConstantUtils.SKU_TYPE_END ? R.drawable.common_icon_invest_end : R.drawable.common_icon_invest_sale_out);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onClick(View view) {

        if (view instanceof Button && mListener != null) {
            Object o = view.getTag();
            InvestDetailBean itemList = (InvestDetailBean) o;
            mListener.onItemButtonClick(itemList);
        }
    }

    static class GroupViewHolder {
        @BindView(R.id.tv_module_title)
        TextView mTvTitle;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        @BindView(R.id.ll_tag)
        LinearLayout mLlTag;
        @BindView(R.id.tv_child_days)
        TextView mTvDate;
        @BindView(R.id.tv_child_rate)
        TextView mTvRate;
        @BindView(R.id.bt_invest)
        Button mBtInvest;
        @BindView(R.id.iv_invest_sell_out)
        ImageView mIvSellOut;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemButtonClickListener {
        void onItemButtonClick(InvestDetailBean itemList);
    }

}
