package com.weiyankeji.zhongmei.ui.mmodel;

/**
 * 用户信息
 * Created by caiwancheng on 2017/8/29.
 */

public class UserInfoResponse {

    /**
     "avatar": "string,头像",
     "nickname": "string,用户昵称",
     "realname": "string,真实姓名（脱敏）",
     "idcard_no": "string,身份证号（脱敏）",
     "mobile": "string,手机号（脱敏）",
     "email": "string,邮箱",
     "idcard_auth": "string,实名认真状态（0、未实名，1、已实名）"
     */

    public String avatar;
    public String nickname;
    public String realname;
    public String idcard_no;
    public String mobile;
    public String email;
    public int idcard_auth;

}
