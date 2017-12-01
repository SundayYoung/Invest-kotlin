package com.weiyankeji.zhongmei.ui.mmodel.bean;

import java.io.Serializable;

/**
 * @aythor: lilei
 * time: 2017/8/24  下午3:38
 * function:
 */

public class User implements Serializable {
    /**
     * username : string,用户名
     * mobile : string,手机号
     * token : string,身份标识
     * avatar : string,用户头像
     * <p>
     * available : string,可用余额
     * <p>
     * validate_type : 0、1、2、4  开通支付账户
     * dz_auth : 0、1  认证账户
     */
    public String token;
    public String username;
    public String mobile;
    public String avatar;
    public String realname;
    public int validate_type;
    public int dz_auth;
    public String uuid;
    public String qr_code;
    public String invite_link;


    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public String getInvite_link() {
        return invite_link;
    }

    public void setInvite_link(String invite_link) {
        this.invite_link = invite_link;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getValidate_type() {
        return validate_type;
    }

    public void setValidate_type(int validate_type) {
        this.validate_type = validate_type;
    }

    public int getDz_auth() {
        return dz_auth;
    }

    public void setDz_auth(int dz_auth) {
        this.dz_auth = dz_auth;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }
}
