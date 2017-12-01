package com.weiyankeji.zhongmei.ui.mmodel.bean;

import java.io.Serializable;

/**
 * @aythor: lilei
 * time: 2017/9/10  下午5:00
 * function:
 */

public class BackPlanScheduleBean implements Serializable {

    /**
     * id : integer,记录ID
     * period : string,期数
     * back_time : string,回款时间
     * back_amount : long,回款金额
     */

    public int id;
    public int period;
    public long back_time;
    public long back_amount;
    public long back_total_amount;
    public long back_interest;


}
