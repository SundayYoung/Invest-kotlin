package com.weiyankeji.zhongmei.ui.mpresenter;

import android.content.Context;
import android.text.TextUtils;

import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.mmodel.payment.ChangePaymentPwRequest;
import com.weiyankeji.zhongmei.ui.mview.ChangePaymentPasswordSecondView;

/**
 * Created by zff on 2017/9/5.
 */

public class ChangePaymentPasswordSecondPresenter extends BasePresenter<ChangePaymentPasswordSecondView> {

    /**
     * 提交新的支付密码
     */
    public void submitPaymentPasswordInfo(ChangePaymentPwRequest request) {
        mView.showLoading();
        mCall = mRestService.postData(UrlConfig.CHANG_PAYMENT_PASSWORD, request);
        mCall.enqueue(new RestBaseCallBack<String>() {

            @Override
            public void onResponse(String response) {
                mView.hideLoading();
                mView.showCommitSuccess();
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.showCommitFail(msg, code);
                    mView.hideLoading();
                }
            }
        });
    }

    private boolean validatePassword(String password) {
        if (TextUtils.isEmpty(password) || password.length() != 6) {
            return false;
        }
        return true;
    }


    public boolean validateInput(Context context, String oldPassword, String newPassword, String newComfirmPassword) {
        boolean flag = false;
        if (!validatePassword(oldPassword)) {
            ToastUtil.showShortToast(context.getApplicationContext(), R.string.old_password_error);
            return flag;
        }
        if (!validatePassword(newPassword)) {
            ToastUtil.showShortToast(context.getApplicationContext(), R.string.pay_pw_six_length);
            return flag;
        }
        if (!validatePassword(newComfirmPassword)) {
            ToastUtil.showShortToast(context.getApplicationContext(), R.string.pay_pw_six_length);
            return flag;
        }
        if (!newPassword.equals(newComfirmPassword)) {
            mView.showCommitFail("",0);

            return flag;
        }
        flag = true;
        return flag;
    }


}
