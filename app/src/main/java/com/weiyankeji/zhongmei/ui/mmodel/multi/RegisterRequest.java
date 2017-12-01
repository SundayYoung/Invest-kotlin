package com.weiyankeji.zhongmei.ui.mmodel.multi;

import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;

import java.io.Serializable;

/**
 * @aythor: lilei
 * time: 2017/9/7  下午3:30
 * function:
 */

public class RegisterRequest extends BaseRequest implements Serializable {

    /**
     * mobile : string,手机号
     * verify_code: int , 0注册、1忘记密码 2充值 3修改绑定手机
     * password : 密码（8-20位字母，数字组合）
     * inviter : 邀请人手机号
     */
    public RegisterRequest(String mobile, String verify_code, String password, String inviter) {
        this.mobile = mobile;
        this.verify_code = verify_code;
        this.password = password;
        this.inviter = inviter;
    }

    String mobile;
    String verify_code;
    String password;
    String inviter;
}
