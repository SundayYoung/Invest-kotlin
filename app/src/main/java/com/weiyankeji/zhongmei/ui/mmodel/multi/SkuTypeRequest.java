package com.weiyankeji.zhongmei.ui.mmodel.multi;

import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;

import java.io.Serializable;

/**
 * @aythor: lilei
 * time: 2017/9/8  下午9:01
 * function:
 */

public class SkuTypeRequest extends BaseRequest implements Serializable {

    int sku_type;

    public SkuTypeRequest(int sku_type) {
        this.sku_type = sku_type;
    }

}
