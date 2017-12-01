package com.weiyankeji.zhongmei.ui.mview;

import com.weiyankeji.zhongmei.ui.mmodel.BankInfoResponse;

/**BankCardInfoView
 * Created by caiwancheng on 2017/8/28.
 */

public interface BankCardInfoView extends BaseView {

    /**
     * 请求回来的银行卡信息
     * @param response
     */
    void request(BankInfoResponse response);

    /**
     * 提交银行卡信息成功
     */
    void submitSuccessful();

    /**
     * 提交失败
     * @param code
     * @param msg
     */
    void submitFailure(int code, String msg);

    /**
     * 设置支付密码成功
     */
    void setPayPwSuccessful();

    /**
     * 设置支付密码失败
     */
    void setPayPwFailure(int code, String msg);

    /**
     * 短信验证码请求成功
     */
    void requestCodeSuccessful();

    /**
     * 短信验证码请求失败
     */
    void requestCodeFailure(int code, String msg);

    /**
     * 验证手机验证码
     */

    void validaPhoneCode();

}
