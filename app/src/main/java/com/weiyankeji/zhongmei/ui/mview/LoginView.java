package com.weiyankeji.zhongmei.ui.mview;

import com.weiyankeji.zhongmei.ui.mmodel.bean.User;

/**
 * @aythor: lilei
 * time: 2017/8/30  上午11:41
 * function:
 */

public interface LoginView extends BaseView {
    void setData(User mUser);

    void onFailure(int code, String msg);
}
