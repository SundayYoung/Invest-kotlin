package com.weiyankeji.zhongmei.ui.mmodel.mine;

import com.weiyankeji.zhongmei.ui.mmodel.bean.InviteTotalBean;

import java.io.Serializable;
import java.util.List;

/**
 * 我的邀请 累计邀请 出参
 * Created by liuhaiyang on 2017/9/27.
 */

public class InviteResponse implements Serializable {
    public int invite_num; //累计邀请人数
    public int total_bonus; //累计赚取收益
    public List<InviteTotalBean> invitee_list;
}
