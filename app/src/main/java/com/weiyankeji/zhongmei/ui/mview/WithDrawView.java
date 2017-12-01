package com.weiyankeji.zhongmei.ui.mview;

import com.weiyankeji.zhongmei.ui.mmodel.BankInfoResponse;
import com.weiyankeji.zhongmei.ui.mmodel.bean.AccountInfoBean;

/**
 * Created by caiwancheng on 2017/8/30.
 */

public interface WithDrawView extends BaseView {
    void setBankData(BankInfoResponse response);
    void setAccountData(AccountInfoBean response);
    void showCommitSuccess();
    void showCommitFail(int code,String msg);
    void  showInitDataFail(int code, String msg);
}
