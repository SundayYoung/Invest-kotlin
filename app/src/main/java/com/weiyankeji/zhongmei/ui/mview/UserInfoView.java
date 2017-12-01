package com.weiyankeji.zhongmei.ui.mview;


import com.weiyankeji.zhongmei.ui.mmodel.bean.User;

/**
 * @aythor: lilei
 * time: 2017/9/1  下午4:51
 * function:
 */

public interface UserInfoView extends BaseView {
    void setData(User mUser);
    void onSuccess();
    void onFailure(int mCode, String msg);
}
