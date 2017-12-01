package com.weiyankeji.zhongmei.ui.mmodel.mine;

import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;

/**
 * Created by caiwancheng on 2017/9/11.
 */

public class ChangeLoginPwRequest extends BaseRequest {
    private String old_password;
    private String new_password;

    public ChangeLoginPwRequest(String oldPw, String newPw) {
        this.old_password = oldPw;
        this.new_password = newPw;
    }
}
