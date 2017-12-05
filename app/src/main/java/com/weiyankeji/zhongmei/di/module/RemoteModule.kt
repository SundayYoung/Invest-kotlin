package com.weiyankeji.zhongmei.di.module

import com.weiyankeji.zhongmei.data.remote.BaseRemoteDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by liuhaiyang on 2017/12/4.
 */
@Module
class RemoteModule {
    @Provides
    @Singleton
    fun provideBaseRemoteDataSource(): BaseRemoteDataSource = BaseRemoteDataSource()
}