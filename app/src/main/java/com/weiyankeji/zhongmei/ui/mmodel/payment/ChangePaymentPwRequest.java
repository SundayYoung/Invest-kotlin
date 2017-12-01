package com.weiyankeji.zhongmei.ui.mmodel.payment;

import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;

/**
 * Created by zff on 2017/9/8.
 */

public class ChangePaymentPwRequest extends BaseRequest {

    public ChangePaymentPwRequest(String old_password, String new_password) {
        this.old_password = old_password;
        this.new_password = new_password;
    }

    public String old_password;
    public String new_password;
}
