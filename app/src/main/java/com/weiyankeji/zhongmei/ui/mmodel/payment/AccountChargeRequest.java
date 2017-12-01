package com.weiyankeji.zhongmei.ui.mmodel.payment;

import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;

/**
 * Created by zff on 2017/9/12.
 */

public class AccountChargeRequest extends BaseRequest {

    public AccountChargeRequest(long amount, int charge_type, int bank_card_id, String trade_password) {
        this.amount = amount;
        this.charge_type = charge_type;
        this.bank_card_id = bank_card_id;
        this.trade_password = trade_password;
    }

    long amount;//充值金额
    int charge_type;//充值类型
    int bank_card_id;//银行卡ID
    String trade_password;//支付密码
}
