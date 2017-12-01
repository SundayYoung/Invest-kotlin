package com.weiyankeji.zhongmei.ui.mmodel.payment;

import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;

/**
 * Created by zff on 2017/9/11.
 */

public class ChangePaymentPasswordVertiyRequest extends BaseRequest {


    public ChangePaymentPasswordVertiyRequest(String phone, String verify_code, String verify_type) {
        this.phone = phone;
        this.verify_code = verify_code;
        this.verify_type = verify_type;
    }

    String phone;
    String verify_code;
    String verify_type;

}
