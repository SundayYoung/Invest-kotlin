package com.weiyankeji.zhongmei.ui.mview;

/**
 * Created by zff on 2017/9/5.
 */

public interface ChangePaymentPasswordSecondView extends BaseView {

    /**
     * 显示提交新密码成功
     */
    void showCommitSuccess();
    void showCommitFail(String msg,int code);

}
