package com.weiyankeji.zhongmei.ui.mmodel.payment;

import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;

import java.io.Serializable;

/**
 * @aythor: zff
 * time: 2017/9/10
 */

public class ForgetPasswordRequest extends BaseRequest implements Serializable {

    public ForgetPasswordRequest(String mobile, String verify_code, String new_password) {
        this.mobile = mobile;
        this.verify_code = verify_code;
        this.new_password = new_password;
    }

    String mobile;
    String verify_code;
    String new_password;
}
