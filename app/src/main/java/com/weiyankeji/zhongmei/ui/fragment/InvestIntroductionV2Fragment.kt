package com.weiyankeji.zhongmei.ui.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.weiyankeji.zhongmei.R
import com.weiyankeji.zhongmei.api.UrlConfig
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestDetailResponse
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestWithIdRequest
import com.weiyankeji.zhongmei.utils.ConstantUtils
import com.weiyankeji.zhongmei.viewmodel.InvestIntroductionViewModel

/**
 * Created by liuhaiyang on 2017/12/2.
 */
class InvestIntroductionV2Fragment : Fragment() {

    private var mInvestViewModel : InvestIntroductionViewModel? = null

    private var mSkuId: String? = null
    private var mTitle: String? = null
    private var mDetailResponse: InvestDetailResponse? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_invest_product_introduction, container, false)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)

        mInvestViewModel = ViewModelProviders.of(this).get(InvestIntroductionViewModel::class.java)

        mSkuId = bundle?.getString(ConstantUtils.KEY_ID)
        mTitle = bundle?.getString(ConstantUtils.KEY_TITLE)
        mDetailResponse = bundle?.getSerializable(ConstantUtils.KEY_DATA) as InvestDetailResponse?


        loadData()
    }


    fun loadData() {
        mInvestViewModel?.loadData(UrlConfig.INVEST_SECURITY, InvestWithIdRequest("936421690772287488"))?.observe(this, Observer { resList ->
            println("LLLL " + resList?.get(0)?.content)
        })
    }

}