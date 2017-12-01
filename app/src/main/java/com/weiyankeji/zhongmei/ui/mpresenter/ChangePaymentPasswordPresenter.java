package com.weiyankeji.zhongmei.ui.mpresenter;


import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.mmodel.multi.SendCodeRequest;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.mmodel.mine.ValidaCodeRequest;
import com.weiyankeji.zhongmei.ui.mview.ChangePaymentPasswordView;
import com.weiyankeji.zhongmei.ui.mview.CountDownTimerView;
import com.weiyankeji.zhongmei.utils.CountDownTimerUtils;
import com.weiyankeji.zhongmei.utils.UserUtils;

/**
 * Created by zff on 2017/9/5.
 */

public class ChangePaymentPasswordPresenter extends BasePresenter<ChangePaymentPasswordView> {

    private CountDownTimerUtils mCountDownTimerUtils;

    /**
     * 是否结束倒计时
     * @return
     */
    public boolean isCountDownOver() {
        if (mCountDownTimerUtils == null) {
            return true;
        }
        return mCountDownTimerUtils.isFinish();
    }

    /**
     * 开启倒计时
     */
    public void startCountDownTimer(CountDownTimerView countDownTimerView) {
        mCountDownTimerUtils = new CountDownTimerUtils(countDownTimerView);
        mCountDownTimerUtils.start();
    }


    /**
     * 取消倒计时
     */
    public void cancelCountDownTimer() {
        if (mCountDownTimerUtils != null) {
            mCountDownTimerUtils.cancel();
            mCountDownTimerUtils = null;
        }
    }


    /**
     * 加载验证码请求
     *
     * @param codeType 短信验证码类型
     */
    public void requestVerificationCode(int codeType) { //ChangePwCodeRequest request
        mCall = mRestService.postData(UrlConfig.MULTI_SEND_CODE, new SendCodeRequest("", codeType));
        mCall.enqueue(new RestBaseCallBack<String>() {
            @Override
            public void onResponse(String response) {
                mView.requestCodeSuccess();
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                ToastUtil.showShortToast(ZMApplication.getZMApplication(), msg);
                if (mView != null) {
                    mView.requestCodeFail();
                }
            }

        });
    }


    /**
     * 发送验证码请求
     */
    public void sendValidaCode(String code, int codeType) {
        mView.showLoading();
        ValidaCodeRequest request = new ValidaCodeRequest();
        request.verify_code = code;
        request.verify_type = codeType;
        mCall = mRestService.postData(UrlConfig.VALIDA_PHONE_CODE, request);
        mCall.enqueue(new RestBaseCallBack<String>() {

            @Override
            public void onResponse(String str) {
                mView.hideLoading();
                mView.validaRequest();
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                ToastUtil.showShortToast(ZMApplication.getZMApplication(), msg);
                if (mView != null) {
                    mView.hideLoading();
                }
            }
        });
    }


    /**
     * 获取用户手机号
     *
     * @return
     */
    public String getUserMobile() {
        return UserUtils.getInstance().getUserObject().getMobile();
    }

}
