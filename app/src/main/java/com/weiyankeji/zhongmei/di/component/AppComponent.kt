package com.weiyankeji.zhongmei.di.component

import com.weiyankeji.zhongmei.di.module.RemoteModule
import com.weiyankeji.zhongmei.viewmodel.InvestIntroductionViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Created by liuhaiyang on 2017/12/2.
 */
@Component(modules = arrayOf(RemoteModule::class))
@Singleton interface AppComponent {
    fun inject(currencyViewModel: InvestIntroductionViewModel)
}