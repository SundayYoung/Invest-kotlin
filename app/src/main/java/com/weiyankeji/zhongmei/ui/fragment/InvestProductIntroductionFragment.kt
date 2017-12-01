package com.weiyankeji.zhongmei.ui.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

import com.weiyankeji.library.utils.DensityUtil
import com.weiyankeji.library.utils.ExtendUtil
import com.weiyankeji.library.utils.StringUtil
import com.weiyankeji.zhongmei.R
import com.weiyankeji.zhongmei.api.UrlConfig
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestDetailResponse
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestProductIntroductionResponse
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestWithIdRequest
import com.weiyankeji.zhongmei.ui.mpresenter.InvestProductIntroductionPresenter
import com.weiyankeji.zhongmei.ui.mview.InvestProductIntroductionView
import com.weiyankeji.zhongmei.utils.CommonUtils
import com.weiyankeji.zhongmei.utils.ConstantUtils
import kotlinx.android.synthetic.main.fragment_invest_product_introduction.*

/**
 * 投资 项目介绍 风控措施 风险提示 安全保障
 * Created by liuhaiyang on 2017/8/29.
 */

class InvestProductIntroductionFragment : BaseMvpFragment<InvestProductIntroductionView, InvestProductIntroductionPresenter>(), InvestProductIntroductionView {

    private var mSkuId: String? = null
    private var mTitle: String? = null
    private var mDetailResponse: InvestDetailResponse? = null

    override fun setContentLayout() = R.layout.fragment_invest_product_introduction

    override fun finishCreateView(bundle: Bundle?) {

        if (bundle != null) {

            mSkuId = bundle.getString(ConstantUtils.KEY_ID)
            mTitle = bundle.getString(ConstantUtils.KEY_TITLE)
            mDetailResponse = bundle.getSerializable(ConstantUtils.KEY_DATA) as InvestDetailResponse?

            setTitle(mTitle, true)

            setLoading(true)
            //项目介绍 风控措施 风险提示 安全保障
            when(mTitle) {
                getString(R.string.invest_detail_introduction) -> loadData(UrlConfig.INVEST_INTRODUCTION)
                getString(R.string.invest_detail_wind_contro) -> loadData(UrlConfig.INVEST_CONTROL)
                getString(R.string.invest_detail_warn) -> loadData(UrlConfig.INVEST_TIP)
                getString(R.string.invest_detail_security) -> loadData(UrlConfig.INVEST_SECURITY)
            }
        }
    }


    override fun initPresenter(): InvestProductIntroductionPresenter = InvestProductIntroductionPresenter()


    override fun showLoading() {}

    override fun hideLoading() {
        setLoading(false)
    }

    override fun setData(response: List<InvestProductIntroductionResponse>) {
        for (res in response.listIterator()) {

            if (mTitle == getString(R.string.invest_detail_security) || mTitle == getString(R.string.invest_detail_warn)) {
                mLlContent!!.addView(getContent(res.content, 0 , false))
                break
            } else {
                mLlContent!!.addView(getTitle(res.title))
                mLlContent!!.addView(getContent(res.content, 0 , false))
            }
        }

        if (!StringUtil.isNullOrEmpty(mTitle) && mTitle == getString(R.string.invest_detail_introduction) && mDetailResponse != null && !ExtendUtil.listIsNullOrEmpty(mDetailResponse!!.agreement)) {
            val view = View.inflate(mContext, R.layout.view_invest_introduce_foot, mLlContent)
            //金融资产收益权转让协议
            val llPro = view.findViewById<LinearLayout>(R.id.ll_protocol)
            var i = 0
            val size = mDetailResponse!!.agreement.size
            while (i < size) {
                val pro = mDetailResponse!!.agreement[i]
                if (pro == null) {
                    i++
                    continue
                }
                llPro.addView(getContent(pro.title, i, true))
                i++
            }
        }
    }

    private fun loadData(url: String) {
        mPresenter.loadData(url, InvestWithIdRequest(mSkuId))
    }

    private fun getContent(content: String, tag: Int, isProtocol: Boolean): TextView {
        val tvContent = TextView(mContext)
        tvContent.setTextColor(ContextCompat.getColor(mContext, if (isProtocol) R.color.blue_00 else R.color.gray_8a))
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        tvContent.text = content
        tvContent.setLineSpacing(0f, 1.4f)
        val contentParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        contentParams.setMargins(0, DensityUtil.dp2px(mContext, 10f), 0, if (isProtocol) 0 else DensityUtil.dp2px(mContext, 30f))
        tvContent.layoutParams = contentParams
        //Is only protocol can be click
        if (isProtocol) {
            tvContent.tag = tag
            tvContent.setOnClickListener { v ->
                val pos = v.tag as Int
                if (mDetailResponse != null && !ExtendUtil.listIsNullOrEmpty(mDetailResponse!!.agreement)) {
                    val url = mDetailResponse!!.agreement[pos].link
                    CommonUtils.intentWebView(this@InvestProductIntroductionFragment, getString(R.string.custom_details_invest_agreement), url)
                }
            }
        }

        return tvContent
    }

    private fun getTitle(title: String): TextView {
        val tvTitle = TextView(mContext)
        tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.black_2d))
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
        tvTitle.text = title
        tvTitle.typeface = Typeface.DEFAULT_BOLD
        val titleParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        tvTitle.layoutParams = titleParams
        return tvTitle
    }
}
