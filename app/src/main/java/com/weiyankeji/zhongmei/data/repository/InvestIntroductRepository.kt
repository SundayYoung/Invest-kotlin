package com.weiyankeji.zhongmei.ui.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.weiyankeji.library.net.client.BaseServerResponse
import com.weiyankeji.library.net.client.RestApiProvider
import com.weiyankeji.library.net.client.RestBaseCallBack
import com.weiyankeji.library.utils.ExtendUtil
import com.weiyankeji.zhongmei.api.RestService
import com.weiyankeji.zhongmei.application.ZMApplication
import com.weiyankeji.zhongmei.data.remote.BaseRemoteDataSource
import com.weiyankeji.zhongmei.data.remote.InvestIntroductDataSource
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestProductIntroductionResponse
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestWithIdRequest
import com.weiyankeji.zhongmei.utils.net.SignInterceptor
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by liuhaiyang on 2017/12/2.
 */
@Singleton
class InvestIntroductRepository @Inject constructor(private val mRemoteDataSource: InvestIntroductDataSource) {

    fun getData(url: String, request: InvestWithIdRequest): LiveData<List<InvestProductIntroductionResponse>> {
        return mRemoteDataSource.loadData(url, request)
    }
}