package com.weiyankeji.zhongmei.ui.mview;

import com.weiyankeji.zhongmei.ui.mmodel.bean.AccountInfoBean;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestIncomeResponse;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestMentResponse;

/**
 * Created by caiwancheng on 2017/8/30.
 */

public interface InvestMentView extends BaseView {
    //设置账户信息
    void setAccountInfoData(AccountInfoBean bean);
    //投资成功
    void postInvestComplete(InvestMentResponse response);
    //投资失败
    void postInvestError(int code, String errorMsg);
    //设置预计收益
    void setIncome(InvestIncomeResponse response);
    //获取账号信息失败
    void postAccountInfoError(int code, String errorMsg);
}
