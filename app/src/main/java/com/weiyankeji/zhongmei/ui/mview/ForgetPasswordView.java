package com.weiyankeji.zhongmei.ui.mview;


/**
 * Created by zff on 2017/9/4.
 */

public interface ForgetPasswordView extends BaseView {
    /**
     * 短信验证码请求成功
     */
    void requestCodeSuccess();

    /**
     * 短信验证码请求失败
     */
    void requestCodeFail(String msg);

    /**
     * 显示成功提交
     */
    void showCommitSuccess();

    /**
     * 忘记密码提交失败
     */
    void showCommitFail(String msg);
}
