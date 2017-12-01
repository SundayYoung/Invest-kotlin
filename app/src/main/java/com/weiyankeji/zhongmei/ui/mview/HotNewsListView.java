package com.weiyankeji.zhongmei.ui.mview;

import com.weiyankeji.zhongmei.ui.mmodel.HotNewsResponse;


import java.util.List;

/**
 * Created by zff on 2017/9/7.
 */

public interface HotNewsListView extends BaseView {


    /**
     * 加载热点新闻列表
     */
    void loadNotices(List<HotNewsResponse> itemBeans);
    void showLoadFial(int code, String msg);

}
