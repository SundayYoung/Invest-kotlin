package com.weiyankeji.zhongmei.ui.mview;

import com.weiyankeji.zhongmei.ui.mmodel.BankInfoResponse;

/**
 * Created by caiwancheng on 2017/8/28.
 */

public interface BankCardView extends BaseView {

    /**
     * 请求回来
     * @param response
     */
    void request(BankInfoResponse response);

    /**
     * 满足换卡条件
     */
    void changeRequest();

    /**
     * 换卡条件不满足
     * @param msg
     */
    void changeFailure(String msg);


}
