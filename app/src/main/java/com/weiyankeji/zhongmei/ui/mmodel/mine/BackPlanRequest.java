package com.weiyankeji.zhongmei.ui.mmodel.mine;

import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;

/**
 * @aythor: lilei
 * time: 2017/9/10  下午4:55
 * function:
 */

public class BackPlanRequest extends BaseRequest {
    /**
     * 回款计划
     */
    String invest_id;

    public BackPlanRequest(String invest_id) {
        this.invest_id = invest_id;
    }
}
