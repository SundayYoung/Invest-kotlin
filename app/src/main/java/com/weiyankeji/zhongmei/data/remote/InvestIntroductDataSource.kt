package com.weiyankeji.zhongmei.data.remote

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.weiyankeji.library.net.client.BaseServerResponse
import com.weiyankeji.library.net.client.RestBaseCallBack
import com.weiyankeji.library.utils.ExtendUtil
import com.weiyankeji.zhongmei.api.RestService
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestProductIntroductionResponse
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestWithIdRequest
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by liuhaiyang on 2017/12/2.
 * @Inject constructor(private var mBaseDataSource: BaseRemoteDataSource? = BaseRemoteDataSource())
 */
@Singleton
class InvestIntroductDataSource @Inject constructor(private val mBaseDataSource: BaseRemoteDataSource) {

    fun loadData(url: String, request: InvestWithIdRequest): LiveData<List<InvestProductIntroductionResponse>> {
        val mutableLiveData = MutableLiveData<List<InvestProductIntroductionResponse>>()
        val mRestService: RestService? = mBaseDataSource.getBaseService()
        val mCall: Call<BaseServerResponse>? = mRestService?.postData(url, request)
        mCall?.enqueue(object : RestBaseCallBack<List<InvestProductIntroductionResponse>>() {
            override fun onResponse(response: List<InvestProductIntroductionResponse>?) {
                if (ExtendUtil.listIsNullOrEmpty(response)) {
                    return
                }
                mutableLiveData.value = response
            }

            override fun onFailure(error: Throwable?, code: Int, msg: String) {

            }
        })
        return mutableLiveData
    }
}