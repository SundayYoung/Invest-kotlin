package com.weiyankeji.zhongmei.ui.mview;

import com.weiyankeji.zhongmei.ui.mmodel.PlatformNoticeDetailResponse;

/**
 * Created by zff on 2017/9/1.
 */

public interface PlatformNoticeDetailView extends BaseView {
    /**
     * 加载公告详情
     */
    void loadNotices(PlatformNoticeDetailResponse itemBean);

}
