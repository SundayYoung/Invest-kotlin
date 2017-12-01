package com.weiyankeji.zhongmei.ui.mpresenter;

import android.text.TextUtils;

import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.mmodel.mine.ResetPayPwRequest;
import com.weiyankeji.zhongmei.ui.mview.ChangePaymentPasswordThirdView;
import com.weiyankeji.zhongmei.utils.CommonUtils;

/**
 * Created by zff on 2017/9/5.
 */

public class ChangePaymentPasswordThirdPresenter extends BasePresenter<ChangePaymentPasswordThirdView> {

    /**
     * 提交新的支付密码
     */
    public void submitPaymentPasswordInfo(String idCardNumber, String newPw) {
        mView.showLoading();
        ResetPayPwRequest request = new ResetPayPwRequest();
        request.idcard_no = idCardNumber;
        request.password = newPw;
        mCall = mRestService.postData(UrlConfig.RESET_TRADE_PASSWORD, request);
        mCall.enqueue(new RestBaseCallBack<String>() {

            @Override
            public void onResponse(String response) {
                mView.hideLoading();
                mView.showCommitSuccess();
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.hideLoading();
                    mView.showCommitfail(code, msg);
                }
            }
        });
    }


    /**
     * 验证输入是否合法
     *
     * @param idCard
     * @param newPassword
     * @param newComfirmPassword
     * @return
     */
    public boolean validateInput(String idCard, String newPassword, String newComfirmPassword) {
        boolean flag = false;
        if (TextUtils.isEmpty(idCard) || !CommonUtils.checkIDCard(idCard)) {
            ToastUtil.showShortToast(ZMApplication.getZMApplication(), ZMApplication.getZMApplication().getString(R.string.please_input_valid_idcard));
            return flag;
        }
        if (TextUtils.isEmpty(newPassword) || newPassword.length() != 6 || TextUtils.isEmpty(newComfirmPassword) || newComfirmPassword.length() != 6) {
            ToastUtil.showShortToast(ZMApplication.getZMApplication(), ZMApplication.getZMApplication().getString(R.string.pay_pw_six_length));
            return flag;
        }
        if (!newPassword.equals(newComfirmPassword)) {
            mView.showCommitfail(0, "");
            return flag;
        }
        flag = true;
        return flag;
    }


}
