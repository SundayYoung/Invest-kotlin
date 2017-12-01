package com.weiyankeji.zhongmei.ui.mview;

/**
 * Created by caiwancheng on 2017/9/1.
 */

public interface AuthCodeCheckView extends BaseView {

    void respone();

    void responeFail(int code, String msg);
}
