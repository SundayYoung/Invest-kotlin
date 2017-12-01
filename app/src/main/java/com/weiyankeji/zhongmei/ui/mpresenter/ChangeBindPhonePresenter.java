package com.weiyankeji.zhongmei.ui.mpresenter;

import android.text.TextUtils;

import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.fragment.BaseFragment;
import com.weiyankeji.zhongmei.ui.mmodel.mine.ChangeBindPhoneRequest;
import com.weiyankeji.zhongmei.ui.mmodel.multi.SendCodeRequest;
import com.weiyankeji.zhongmei.ui.mview.AccountValidaView;
import com.weiyankeji.zhongmei.ui.mview.CountDownTimerView;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.CountDownTimerUtils;

/**
 * Created by caiwancheng on 2017/9/4.
 */

public class ChangeBindPhonePresenter extends BasePresenter<AccountValidaView> {

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
    public void loadMsgValidaCode(String molibe) {
        if (!CommonUtils.checkPhoneNumber(molibe, true)) {
            return;
        }
        SendCodeRequest request = new SendCodeRequest(molibe, ConstantUtils.VERIFY_TYPE_NEW_PHONE);
        mCall = mRestService.postData(UrlConfig.MULTI_SEND_CODE, request);
        mCall.enqueue(new RestBaseCallBack<String>() {

            @Override
            public void onResponse(String str) {
                mView.requestCodeSuccessful();
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    if (!TextUtils.isEmpty(msg)) {
                        ToastUtil.showShortToast(ZMApplication.getZMApplication(), msg);
                    }
                    mView.requestCodeFailure(code, msg);
                }
            }
        });
    }


    /**
     * 提交
     *
     * @param number
     * @param code
     */
    public void submit(final BaseFragment fragment, String number, String code) {
        mView.showLoading();
        ChangeBindPhoneRequest request = new ChangeBindPhoneRequest();
        request.new_mobile = number;
        request.verify_code = code;
        mCall = mRestService.postData(UrlConfig.REBIND_PHONE, request);
        mCall.enqueue(new RestBaseCallBack<String>() {

            @Override
            public void onResponse(String str) {
                mView.hideLoading();
                mView.validaRequest();
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.hideLoading();
                    mView.validaRequestFailure(code, msg);
                }
            }
        });
    }


    /**
     * 验证提交信息
     *
     * @param phone
     * @param code
     */
    public boolean checkCommit(String phone, String code) {
        if (!CommonUtils.checkPhoneNumber(phone, true) || !CommonUtils.checkMessageCode(code, true)) {
            return false;
        }
        return true;
    }

}
