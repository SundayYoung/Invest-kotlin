package com.weiyankeji.zhongmei.data.remote

import com.weiyankeji.library.net.client.RestApiProvider
import com.weiyankeji.zhongmei.api.RestService
import com.weiyankeji.zhongmei.application.ZMApplication
import com.weiyankeji.zhongmei.utils.net.SignInterceptor
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by liuhaiyang on 2017/12/2.
 */
@Singleton
class BaseRemoteDataSource {
    fun getBaseService() : RestService = RestApiProvider.getInstance().withInterceptor(SignInterceptor(ZMApplication.getZMContext())).builder().getApiService(RestService::class.java)
}