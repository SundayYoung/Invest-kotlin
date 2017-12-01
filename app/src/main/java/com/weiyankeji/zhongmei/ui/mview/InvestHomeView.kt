package com.weiyankeji.zhongmei.ui.mview

import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestHomeResponse

/**
 * Created by liuhaiyang on 2017/8/2.
 */

interface InvestHomeView : BaseView {

    fun setData(response: InvestHomeResponse)
    fun netError(code: Int, msg: String)
}
