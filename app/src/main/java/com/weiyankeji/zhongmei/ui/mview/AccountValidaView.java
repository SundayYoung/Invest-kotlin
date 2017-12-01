package com.weiyankeji.zhongmei.ui.mview;

/**
 * Created by caiwancheng on 2017/9/4.
 */

public interface AccountValidaView extends BaseView {
    /**
     * 短信验证码请求失败
     */
    void requestCodeFailure(int code, String msg);


    /**
     * 提交手机号和验证码之类
     */
    void validaRequest();

    /**
     * 提交手机号和验证码之类
     */
    void validaRequestFailure(int code, String msg);

    /**
     * 验证短信码请求成功
     */
    void requestCodeSuccessful();
}
