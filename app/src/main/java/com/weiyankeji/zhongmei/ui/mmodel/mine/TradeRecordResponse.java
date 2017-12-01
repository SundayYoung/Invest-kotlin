package com.weiyankeji.zhongmei.ui.mmodel.mine;

/**
 * 交易记录
 * Created by caiwancheng on 2017/8/25.
 */

public class TradeRecordResponse {


    /**
     * id : 0
     * type : 1
     * title : 提现
     * datetime : 1503645477889
     * format_amount : +23455
     * amount : 23455000
     *     {
     "id": 28.0,
     "trade_type": 2.0,
     "title": "提现",
     "datetime": "2017-09-08 17:13:07",
     "amount": 100.0,
     "format_amount": "-100"
     }
     */

    public int id;
    public int type;
    public String title;
    public long datetime;
    public String format_amount;
    public long amount;
}
