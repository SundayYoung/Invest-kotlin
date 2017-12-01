package com.weiyankeji.zhongmei.ui.mmodel.multi;

import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;

import java.io.Serializable;

/**
 * @aythor: lilei
 * time: 2017/9/7  下午4:43
 * function:
 */

public class LoginRequest extends BaseRequest implements Serializable {

    /**
     * mobile : string,手机号
     * password : 密码（8-20位字母，数字组合）
     */
    public LoginRequest(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

    String mobile;
    String password;
}
