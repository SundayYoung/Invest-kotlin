package com.weiyankeji.zhongmei.ui.mview;

import com.weiyankeji.zhongmei.ui.mmodel.MineResponse;

/**
 * @aythor: lilei
 * time: 2017/8/29  上午10:03
 * function:
 */

public interface MineView extends BaseView {
    void setData(MineResponse mineResponse);

    void failureToken(int code, String msg);
}
