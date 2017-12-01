package com.weiyankeji.zhongmei.ui.mview;


/**
 * @aythor: lilei
 * time: 2017/9/4  下午5:07
 * function:
 */

public interface RegisterView extends BaseView {

    void loadCodeSuccess();

    void loadCodeFailure(int code, String msg);

    void registSuccess();

    void registFailure(int code, String msg);

}
