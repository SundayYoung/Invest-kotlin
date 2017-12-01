package com.weiyankeji.zhongmei.ui.mview;

import com.weiyankeji.zhongmei.ui.mmodel.bean.CustomInvestBean;

import java.util.ArrayList;

/**
 * @aythor: lilei
 * time: 2017/8/30  下午7:39
 * function:
 */

public interface CustomInvestView extends BaseView {
    void setData(ArrayList<CustomInvestBean> list);
}
