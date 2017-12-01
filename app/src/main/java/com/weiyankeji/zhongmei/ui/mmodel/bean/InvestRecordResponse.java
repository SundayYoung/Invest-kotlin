package com.weiyankeji.zhongmei.ui.mmodel.bean;

import java.io.Serializable;

/**
 * Created by caiwancheng on 2017/7/28.
 */

public class InvestRecordResponse implements Serializable {
    /**
     * 投资人 手机号
     */
    public String investor;
    /**
     * 投资金额
     */
    public long invest_amount;
    /**
     * 投资时间
     */
    public long invest_time;

}
