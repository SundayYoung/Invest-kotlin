package com.weiyankeji.zhongmei.ui.mmodel;

import com.weiyankeji.zhongmei.ui.mmodel.bean.BackPlanScheduleBean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @aythor: lilei
 * time: 2017/9/10  下午5:02
 * function:
 */

public class BackPlanResponse implements Serializable {
    public long invest_amount;
    public long prospective_earnings;
    public ArrayList<BackPlanScheduleBean> schedule;
}
