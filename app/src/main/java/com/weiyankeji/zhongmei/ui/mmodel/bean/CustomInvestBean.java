package com.weiyankeji.zhongmei.ui.mmodel.bean;

import java.io.Serializable;

/**
 * @aythor: lilei
 * time: 2017/8/30  下午7:42
 * function:
 */

public class CustomInvestBean implements Serializable {
    /**
     * invest_id : integer,投资记录ID
     * sku_id : integer,标的ID
     * sku_title : string,标的名称
     * sku_rate : integer,年化收益（万分之一）
     * invest_amount : integer,投资金额（单位：分）
     * sku_status : integer,标的状态
     * start_interest_time : string,起息时间
     * back_time : string,回款时间
     * back_amount : integer,已回款金额,本金+利息（单位：分）
     * should_back : integer,待回款金额,本金+利息（单位：分）
     * invest_fail_msg : string,投资失败原因
     * end_interest_time : integer,止息时间
     * invest_protocol : string,投资协议
     * status : integer,标的状态
     * actual_principal : string,实收本息
     * actual_interest : string,实收利息
     * wait_principal : string,待收本息
     * wait_interest : string,待收利息
     */

    public String invest_id;
    public String sku_id;
    public String sku_title;
    public int sku_rate;
    public long invest_amount;
    public String sku_status;
    public int status;
    public long start_interest_time;

    public long back_time;
    public long back_amount;
    public long should_back;
    public String invest_fail_msg;
    public long end_interest_time;
    public String invest_protocol;

    public long actual_principal;
    public long actual_interest;
    public long wait_principal;
    public long wait_interest;


}
