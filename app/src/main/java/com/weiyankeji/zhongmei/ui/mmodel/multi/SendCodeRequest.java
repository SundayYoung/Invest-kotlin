package com.weiyankeji.zhongmei.ui.mmodel.multi;

import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;

import java.io.Serializable;

/**
 * @aythor: lilei
 * time: 2017/9/7  上午11:31
 * function:
 */

public class SendCodeRequest extends BaseRequest implements Serializable {

    /**
     * mobile : string,手机号
     * verify_type: int , 0注册、1忘记密码 2充值 3修改绑定手机
     */
    public SendCodeRequest(String mobile, int verify_type) {
        this.mobile = mobile;
        this.verify_type = verify_type;
    }

    String mobile;
    int verify_type;
}
