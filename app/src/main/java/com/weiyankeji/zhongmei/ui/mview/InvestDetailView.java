package com.weiyankeji.zhongmei.ui.mview;

import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestDetailResponse;

/**
 * Created by liuhaiyang on 2017/8/28.
 */

public interface InvestDetailView extends BaseView {

    void setData(InvestDetailResponse response);
    void netError(int code, String msg);
}
