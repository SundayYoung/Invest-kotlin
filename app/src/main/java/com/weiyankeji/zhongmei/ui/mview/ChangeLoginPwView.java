package com.weiyankeji.zhongmei.ui.mview;

/**
 * Created by caiwancheng on 2017/9/1.
 */

public interface ChangeLoginPwView extends BaseView {

    void changeLoginPwResponse();

    void changeFailure(int code, String msg);

}
