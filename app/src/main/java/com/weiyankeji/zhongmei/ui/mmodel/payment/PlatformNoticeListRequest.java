package com.weiyankeji.zhongmei.ui.mmodel.payment;

import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;

/**
 * Created by zff on 2017/9/10.
 */

public class PlatformNoticeListRequest extends BaseRequest {
    public PlatformNoticeListRequest(int page, int page_size) {
        this.page = page;
        this.page_size = page_size;
    }

    int page;
    int page_size;

}
