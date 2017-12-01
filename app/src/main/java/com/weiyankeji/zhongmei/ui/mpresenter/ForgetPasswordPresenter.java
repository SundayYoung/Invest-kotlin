package com.weiyankeji.zhongmei.ui.mpresenter;



import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.mmodel.multi.SendCodeRequest;
import com.weiyankeji.zhongmei.ui.mmodel.payment.ForgetPasswordRequest;
import com.weiyankeji.zhongmei.ui.mview.CountDownTimerView;
import com.weiyankeji.zhongmei.ui.mview.ForgetPasswordView;
import com.weiyankeji.zhongmei.utils.CountDownTimerUtils;

/**
 * Created by zff on 2017/9/4.
 */

public class ForgetPasswordPresenter extends BasePresenter<ForgetPasswordView> {
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
     */
    public void requestVerificationCode(SendCodeRequest request) {

        mCall = mRestService.postData(UrlConfig.MULTI_SEND_CODE, request);
        mCall.enqueue(new RestBaseCallBack<String>() {

            @Override
            public void onResponse(String str) {
                mView.requestCodeSuccess();
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.requestCodeFail(msg);
                }


            }
        });
    }

    /**
     * 提交新密码信息
     */
    public void submitForgetPasswordInfo(ForgetPasswordRequest request) {
        mView.showLoading();
        mCall = mRestService.postData((UrlConfig.FORGET_PASSWORD), request);
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
                    mView.showCommitFail(msg);
                }
            }
        });
    }
}
