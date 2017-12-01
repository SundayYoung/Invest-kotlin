package com.weiyankeji.zhongmei.ui.mview;


/**
 * Created by zff on 2017/9/5.
 */

public interface ChangePaymentPasswordView extends BaseView {

    /**
     * 短信验证码请求失败
     */
    void requestCodeFail();

    /**
     * 上次手机号和验证码之类
     */
    void validaRequest();

    /**
     * 验证短信码请求成功
     */
    void requestCodeSuccess();


}