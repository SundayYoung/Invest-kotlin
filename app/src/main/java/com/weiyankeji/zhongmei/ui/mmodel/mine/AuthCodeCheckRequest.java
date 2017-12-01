package com.weiyankeji.zhongmei.ui.mmodel.mine;

import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;

/**
 * Created by caiwancheng on 2017/9/11.
 */

public class AuthCodeCheckRequest extends BaseRequest {

    private String sku_auth_code;

    public AuthCodeCheckRequest(String code) {
        this.sku_auth_code = code;
    }

}
