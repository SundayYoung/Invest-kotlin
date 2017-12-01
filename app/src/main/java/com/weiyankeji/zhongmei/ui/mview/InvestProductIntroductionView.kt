package com.weiyankeji.zhongmei.ui.mview

import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestProductIntroductionResponse

/**
 * Created by liuhaiyang on 2017/8/29.
 */

interface InvestProductIntroductionView : BaseView {

    fun setData(response: List<InvestProductIntroductionResponse>)
}
