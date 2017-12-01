package com.weiyankeji.zhongmei.ui.mmodel.bean;

import java.io.Serializable;

/**
 * Created by liuhaiyang on 2017/9/27.
 */

public class InviteRewardBean implements Serializable {
    public long date; //日期
    public int status; //状态0 未知 1 待发放 2 已发放
    public int bonus; //赚取奖励
}
