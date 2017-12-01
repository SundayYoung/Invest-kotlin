package com.weiyankeji.zhongmei.ui.mmodel.bean;


import java.io.Serializable;
import java.util.List;

/**
 * 标的内容
 * Created by liuhaiyang on 2017/8/21.
 */

public class InvestDetailBean implements Serializable {

    public String sku_id;
    public String sku_title;
    public long invested_amount; //已投金额 单位：分
    public int sku_type; //标的类型（1、定制，2、定期，3、网贷
    public long sku_amount; //标的金额，单位：分
    public int sku_rate; //年化收益（万分之一）
    public String sku_period; //标的时长
    public long min_invest_amount; //起投金额
    public int sku_period_type; //标的时长单位（1、天，2、月）
    public int sku_status; //标的状态 2：募集中-立即投资 3：募集结束-募集结束 4：458-抢光了
    public String repay_type; //还款方式
    public long c_sku_amount; //第一个是起投金额
    public long remain_raise_time; //募集结束时间 单位 秒
    public List<InvestTagsBean> sku_tags;
    //add 1.1
    public int is_hike_rate; //是否额外加息（0:否；1:是）
    public int hike_rate; //额外加息年化收益
}
