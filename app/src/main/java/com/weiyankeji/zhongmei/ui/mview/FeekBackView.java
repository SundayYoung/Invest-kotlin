package com.weiyankeji.zhongmei.ui.mview;

/**
 * Created by zff on 2017/8/31.
 */

public interface FeekBackView extends BaseView {
    /**
     * 显示提交成功
     */
    void showCommitSuccess(String response);

    void showCommitFail(String msg);
}
