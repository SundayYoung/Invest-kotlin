package com.weiyankeji.zhongmei.ui.mmodel.mine;

import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;

/**
 * Created by caiwancheng on 2017/9/8.
 */

public class AuthNameRequest extends BaseRequest{
    public String realname;
    public String idcard_no;

    public AuthNameRequest(String realname, String idcard_no) {
        this.realname = realname;
        this.idcard_no = idcard_no;
    }

}
