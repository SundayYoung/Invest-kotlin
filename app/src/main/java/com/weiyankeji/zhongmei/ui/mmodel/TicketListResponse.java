package com.weiyankeji.zhongmei.ui.mmodel;

import java.io.Serializable;

/**
 * Created by liuhaiyang on 2017/8/21.
 * 投资首页 出参
 */

public class TicketListResponse implements Serializable {
    /**
     * market_card_id : 123124
     * market_card_title : 500
     * sku_type : 使用描述
     * market_card_type : 2
     * due_time : 1503645477889
     */

    public int market_card_id;
    public String market_card_title;
    public String sku_type;
    public long market_card_value;
    public long low_money;
    public int sku_duration;
    public int market_card_status;
    public int market_card_type; //0、加息券，1、代金券
    public long due_time;
}
