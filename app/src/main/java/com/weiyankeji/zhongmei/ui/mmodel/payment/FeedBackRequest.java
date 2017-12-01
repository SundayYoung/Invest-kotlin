package com.weiyankeji.zhongmei.ui.mmodel.payment;

import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;

/**
 * Created by zff on 2017/9/7.
 */

public class FeedBackRequest extends BaseRequest {

    public FeedBackRequest(String content, String contact) {
        this.content = content;
        this.contact = contact;

    }

    public String content;
    public String contact;
}
