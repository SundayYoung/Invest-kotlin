package com.weiyankeji.zhongmei.ui.mview;

import com.weiyankeji.zhongmei.ui.mmodel.PlatformNoticeResponse;

import java.util.List;

/**
 * Created by zff on 2017/9/1.
 */

public interface PlatformNoticeListView extends BaseView {


    /**
     * 加载公告列表
     */
    void loadNotices(List<PlatformNoticeResponse> itemBeans);
    void showLoadFial(int code, String msg);
}
