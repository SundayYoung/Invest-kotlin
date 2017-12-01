package com.weiyankeji.zhongmei.ui.mpresenter;


import com.weiyankeji.library.net.client.BaseServerResponse;
import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.mmodel.multi.RegisterRequest;
import com.weiyankeji.zhongmei.ui.mmodel.multi.SendCodeRequest;
import com.weiyankeji.zhongmei.ui.mview.CountDownTimerView;
import com.weiyankeji.zhongmei.ui.mview.RegisterView;
import com.weiyankeji.zhongmei.utils.CountDownTimerUtils;

/**
 * @aythor: lilei
 * time: 2017/9/4  下午5:09
 * function:
 */

public class RegisterPresent extends BasePresenter<RegisterView> {

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
     * 获取验证码
     *
     * @param reuqest
     */
    public void loadCode(SendCodeRequest reuqest) {
        mCall = mRestService.postData(UrlConfig.MULTI_SEND_CODE, reuqest);
        mCall.enqueue(new RestBaseCallBack<BaseServerResponse>() {
            @Override
            public void onResponse(BaseServerResponse data) {
                if (mView != null) {
                    mView.hideLoading();
                    mView.loadCodeSuccess();
                }

            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.hideLoading();
                    mView.loadCodeFailure(code, msg);
                }
            }
        });
    }

    /**
     * 注册
     *
     * @param request
     */
    public void registCode(RegisterRequest request) {
        mView.showLoading();
        mCall = mRestService.postData(UrlConfig.MULTI_REGISTER, request);
        mCall.enqueue(new RestBaseCallBack<BaseServerResponse>() {
            @Override
            public void onResponse(BaseServerResponse data) {
                if (mView != null) {
                    mView.hideLoading();
                }
                mView.registSuccess();

            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.hideLoading();
                    mView.registFailure(code, msg);
                }

            }
        });
    }
}
