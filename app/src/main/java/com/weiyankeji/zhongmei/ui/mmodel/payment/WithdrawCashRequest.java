package com.weiyankeji.zhongmei.ui.mmodel.payment;

import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;


/**
 * Created by zff on 2017/9/18.
 */

public class WithdrawCashRequest extends BaseRequest {
    public WithdrawCashRequest(long amount, String trade_password, int bank_card_id) {
        this.amount = amount;
        this.trade_password = trade_password;
        this.bank_card_id = bank_card_id;
    }
    long amount;//金额
    String trade_password;//支付密码
    int bank_card_id;//银行卡ID
}
