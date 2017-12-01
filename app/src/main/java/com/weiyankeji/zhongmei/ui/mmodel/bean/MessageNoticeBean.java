package com.weiyankeji.zhongmei.ui.mmodel.bean;

import java.io.Serializable;

/**
 * @aythor: lilei
 * time: 2017/9/11  下午3:36
 * function:
 */

public class MessageNoticeBean implements Serializable {

    /**
     * datetime : long,最新消息时间
     */

    public long datetime;

    public MessageNoticeBean(long datetime) {
        this.datetime = datetime;
    }
}
