package com.weiyankeji.zhongmei.ui.mview;

import com.weiyankeji.zhongmei.ui.mmodel.mine.AuthNameResponse;

/**
 * Created by caiwancheng on 2017/9/1.
 */

public interface AuthNameView extends BaseView {

    void respone(AuthNameResponse respone);

    void requestFailure(int code, String msg);
}
