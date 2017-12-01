package com.weiyankeji.zhongmei.ui.mmodel.bean;

import java.io.Serializable;

/**
 * 账号信息
 * Created by liuhaiyang on 2017/9/4.
 */

public class AccountInfoBean implements Serializable {
    public long available; //可以余额
    public long total; //总金额
    public long charge_freeze; //"integer,充值冻结金额（单位：分）",
    public int status; // "integer,账户状态（0、待激活，1、激活，-1、冻结）",
    public int is_verified; // "integer,实名认证状态（0、未实名，1、已实名）",
    public int is_bind_card; //integer,是否绑卡（0、未绑卡，1、已绑卡）",
    public int has_trade_passwd; // "integer,是否设置交易密码（0、未设置，1、已设置）",
    public long invest_freeze; // "integer,投资冻结金额（单位：分）",
    public long withdraw_freeze; // "integer,体现冻结金额（单位：分）"

}
