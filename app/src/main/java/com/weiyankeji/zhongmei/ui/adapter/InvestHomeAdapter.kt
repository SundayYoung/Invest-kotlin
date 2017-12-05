package com.weiyankeji.zhongmei.ui.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.weiyankeji.zhongmei.R
import com.weiyankeji.zhongmei.ui.mmodel.bean.InvestDetailBean
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestHomeSkuList
import com.weiyankeji.zhongmei.utils.CommonUtils
import com.weiyankeji.zhongmei.utils.ConstantUtils
import com.weiyankeji.zhongmei.utils.MoneyFormatUtils

import butterknife.BindView
import butterknife.ButterKnife

/**
 * Created by liuhaiyang on 2017/8/21.
 */
class InvestHomeAdapter(private val mContext: Context, private val mListener: OnItemButtonClickListener?) : BaseExpandableListAdapter(), View.OnClickListener {

    private var mList: List<InvestHomeSkuList>? = null

    fun updateAdapter(list: List<InvestHomeSkuList>) {
        mList = list
        notifyDataSetChanged()
    }

    override fun getGroupCount(): Int {
        return if (mList == null) 0 else mList!!.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        if (mList == null) {
            return 0
        }
        val child = mList!![groupPosition]
        return if (child.list == null) 0 else child.list.size
    }

    override fun getGroup(groupPosition: Int): InvestHomeSkuList? {
        return if (groupPosition < 0 || groupPosition >= groupCount) {
            null
        } else mList!![groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): InvestDetailBean? {
        val groupList = getGroup(groupPosition)
        return if (groupList == null || groupList.list == null || childPosition < 0 || childPosition >= getChildrenCount(groupPosition)) {
            null
        } else groupList.list[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var cView = convertView
        val groupViewHolder: GroupViewHolder
        if (cView == null || cView.tag !is GroupViewHolder) {
            cView = LayoutInflater.from(mContext).inflate(R.layout.item_invest_home_group, parent, false)
            groupViewHolder = GroupViewHolder(cView)
            groupViewHolder.mTvTitle = cView.findViewById(R.id.tv_module_title)
            cView!!.tag = groupViewHolder
        } else {
            groupViewHolder = cView.tag as GroupViewHolder
        }

        val groupList = getGroup(groupPosition) ?: return cView
        groupViewHolder.mTvTitle!!.text = groupList.title

        return cView
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var cView = convertView
        val childViewHolder: ChildViewHolder
        if (cView == null) {
            cView = LayoutInflater.from(mContext).inflate(R.layout.item_invest_home_child, parent, false)
            childViewHolder = ChildViewHolder(cView)
            childViewHolder.mLlTag = cView.findViewById(R.id.ll_tag)
            childViewHolder.mTvDate = cView.findViewById(R.id.tv_child_days)
            childViewHolder.mTvRate = cView.findViewById(R.id.tv_child_rate)
            childViewHolder.mBtInvest = cView.findViewById(R.id.bt_invest)
            childViewHolder.mIvSellOut = cView.findViewById(R.id.iv_invest_sell_out)
            cView!!.tag = childViewHolder
        } else {
            childViewHolder = cView.tag as ChildViewHolder
        }

        val child = getChild(groupPosition, childPosition) ?: return cView

        val limit = mContext.getString(R.string.common_double_text,
                CommonUtils.getPeriodTypeText(child.sku_type), child.sku_period)
        childViewHolder.mTvDate!!.text = limit

        var rate = MoneyFormatUtils.yieldPointDouble(child.sku_rate)
        val sIndex: Int
        //add 1.1 加息
        if (child.is_hike_rate == 1 && child.hike_rate != 0) {
            val hike_rate = mContext.getString(R.string.invest_list_hike_rate, MoneyFormatUtils.yieldPointRuleOne(child.hike_rate))
            rate = rate + hike_rate
            sIndex = rate.length - (hike_rate.length + 1)
        } else {
            sIndex = rate.length - 1
        }
        childViewHolder.mTvRate!!.text = CommonUtils.changeStringSize(mContext, rate, 18, sIndex, rate.length, false)

        val btInvest = childViewHolder.mBtInvest
        btInvest!!.tag = child
        btInvest.setOnClickListener(this)
        val ivSellOut = childViewHolder.mIvSellOut

        if (child.sku_status == ConstantUtils.SKU_TYPE_ING) {
            CommonUtils.buildLabelIcons(mContext, childViewHolder.mLlTag, child.sku_tags)
        } else {
            CommonUtils.buildLabelIcons(mContext, childViewHolder.mLlTag, child.sku_tags, ContextCompat.getColor(mContext, R.color.gray_8a))
        }
        childViewHolder.mTvRate!!.setTextColor(if (child.sku_status == ConstantUtils.SKU_TYPE_ING) ContextCompat.getColor(mContext, R.color.red_ff) else ContextCompat.getColor(mContext, R.color.gray_8a))

        btInvest.visibility = if (child.sku_status == ConstantUtils.SKU_TYPE_ING) View.VISIBLE else View.GONE
        ivSellOut!!.visibility = if (child.sku_status == ConstantUtils.SKU_TYPE_ING) View.GONE else View.VISIBLE
        ivSellOut.setBackgroundResource(if (child.sku_status == ConstantUtils.SKU_TYPE_END) R.drawable.common_icon_invest_end else R.drawable.common_icon_invest_sale_out)

        return cView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun onClick(view: View) {

        if (view is Button && mListener != null) {
            val o = view.getTag()
            val itemList = o as InvestDetailBean
            mListener.onItemButtonClick(itemList)
        }
    }

    internal class GroupViewHolder(view: View) {
//        @BindView(R.id.tv_module_title)
        var mTvTitle: TextView? = null
    }

    internal class ChildViewHolder(view: View) {
//        @BindView(R.id.ll_tag)
        var mLlTag: LinearLayout? = null
//        @BindView(R.id.tv_child_days)
        var mTvDate: TextView? = null
//        @BindView(R.id.tv_child_rate)
        var mTvRate: TextView? = null
//        @BindView(R.id.bt_invest)
        var mBtInvest: Button? = null
//        @BindView(R.id.iv_invest_sell_out)
        var mIvSellOut: ImageView? = null

//        init {
//            ButterKnife.bind(this, view)
//        }
    }

    interface OnItemButtonClickListener {
        fun onItemButtonClick(itemList: InvestDetailBean)
    }

}
