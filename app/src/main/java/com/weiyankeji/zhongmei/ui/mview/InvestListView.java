package com.weiyankeji.zhongmei.ui.mview;

import com.weiyankeji.zhongmei.ui.mmodel.bean.InvestDetailBean;

import java.util.List;

/**
 * Created by liuhaiyang on 2017/8/2.
 */

public interface InvestListView extends BaseView {

    void setListData(List<InvestDetailBean> response);
}
