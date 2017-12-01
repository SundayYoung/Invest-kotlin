package com.weiyankeji.zhongmei.ui.mmodel.invest;

import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;

/**
 * 投资 详情 详情介绍 风控 等 入参 只有ID
 * Created by liuhaiyang on 2017/9/7.
 */

public final class InvestWithIdRequest extends BaseRequest {
    String sku_id;

    public InvestWithIdRequest(String id) {
        this.sku_id = id;
    }
}
