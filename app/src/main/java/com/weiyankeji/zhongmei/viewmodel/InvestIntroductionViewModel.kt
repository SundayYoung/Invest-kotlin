package com.weiyankeji.zhongmei.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.weiyankeji.zhongmei.application.ZMApplication
import com.weiyankeji.zhongmei.data.remote.InvestIntroductDataSource
import com.weiyankeji.zhongmei.di.component.DaggerAppComponent
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestProductIntroductionResponse
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestWithIdRequest
import com.weiyankeji.zhongmei.ui.repository.InvestIntroductRepository
import javax.inject.Inject

/**
 * Created by liuhaiyang on 2017/12/2.
 */
class InvestIntroductionViewModel : ViewModel() {

    @Inject
    lateinit var mInvestRepository: InvestIntroductRepository

    private var mInvestResponse: LiveData<List<InvestProductIntroductionResponse>>? = null

    init {
        DaggerAppComponent.builder().build().inject(this)
//        ZMApplication.mAppComponent.inject(this)
    }

    fun loadData(url: String, request: InvestWithIdRequest): LiveData<List<InvestProductIntroductionResponse>>? {
        if (mInvestResponse == null) {
            mInvestResponse = MutableLiveData<List<InvestProductIntroductionResponse>>()
            mInvestResponse = mInvestRepository.getData(url, request)
        }
        return mInvestResponse
    }

}