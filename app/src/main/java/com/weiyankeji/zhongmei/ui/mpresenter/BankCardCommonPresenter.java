package com.weiyankeji.zhongmei.ui.mpresenter;

import android.content.Context;

import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.library.utils.ExtendUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.mmodel.BankInfoResponse;
import com.weiyankeji.zhongmei.ui.mmodel.mine.BankInfoMsgCodeRequest;
import com.weiyankeji.zhongmei.ui.mmodel.mine.BindCardRequest;
import com.weiyankeji.zhongmei.ui.mmodel.mine.SetPayPasswordRequest;
import com.weiyankeji.zhongmei.ui.mmodel.mine.ValidaBankRequest;
import com.weiyankeji.zhongmei.ui.mview.BankCardInfoView;
import com.weiyankeji.zhongmei.ui.mview.CountDownTimerView;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.CountDownTimerUtils;

/**
 * Created by caiwancheng on 2017/8/28.
 */

public class BankCardCommonPresenter extends BasePresenter<BankCardInfoView> {


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
    public void loadMsgValidaCode(String phone, int oldBankCardId, String bankNo, String bankCode) {
        BankInfoMsgCodeRequest request = new BankInfoMsgCodeRequest();
        request.mobile = phone;
        request.bank_card_id = oldBankCardId;
        request.bank_card_no = bankNo.replace(" ", "");
        request.bank_code = bankCode;

        mCall = mRestService.postData(UrlConfig.BANK_SEND_CODE, request);
        mCall.enqueue(new RestBaseCallBack<String>() {

            @Override
            public void onResponse(String str) {
                mView.requestCodeSuccessful();
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.requestCodeFailure(code, msg);
                }
            }
        });
    }


    /**
     * 验证银行卡信息
     */
    public void validaBackNo(String no) {
        ValidaBankRequest request = new ValidaBankRequest();
        request.bank_card_no = no.replace(" ", "");
        mCall = mRestService.postData(UrlConfig.VALIDA_BANK_NO, request);
        mCall.enqueue(new RestBaseCallBack<BankInfoResponse>() {

            @Override
            public void onResponse(BankInfoResponse response) {
                mView.request(response);
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
            }
        });
    }

    /**
     * @param newBankCode 识别到的卡的ID
     * @param cardNo      卡号
     * @param phoneNo     预留电话
     * @param msgCode     短信验证码
     * @param oldBankId   更换卡的时候需要用到  之前的卡的ID
     */
    public void submitBankCardInfo(String newBankCode, String cardNo, String phoneNo, String msgCode, int oldBankId) {
        mView.showLoading();
        BindCardRequest request = new BindCardRequest();
        request.bank_card_id = oldBankId;
        request.bank_card_no = cardNo.replace(" ", "");
        request.mobile = phoneNo;
        request.bank_code = newBankCode;
        request.verify_code = msgCode;

        mCall = mRestService.postData(UrlConfig.BIND_BANK_CARD, request);
        mCall.enqueue(new RestBaseCallBack<String>() {

            @Override
            public void onResponse(String response) {
                mView.hideLoading();
                mView.submitSuccessful();
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.submitFailure(code, msg);
                    mView.hideLoading();
                }
            }
        });
    }

    /**
     * 设置交易密码
     *
     * @param pw
     */
    public void setPayPw(String pw) {
        mView.showLoading();
        SetPayPasswordRequest request = new SetPayPasswordRequest();
        request.password = pw;
        mCall = mRestService.postData(UrlConfig.SET_TRADE_PASSWORD, request);
        mCall.enqueue(new RestBaseCallBack<String>() {

            @Override
            public void onResponse(String response) {
                mView.hideLoading();
                mView.setPayPwSuccessful();
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.setPayPwFailure(code, msg);
                    mView.hideLoading();
                }
            }
        });
    }

    /**
     * 请求验证码的检验
     *
     * @param context
     * @param info
     * @param phone
     */
    public boolean checkRequestCode(Context context, BankInfoResponse info, String phone, String cardNo) {

        if (info == null) {
            ToastUtil.showShortToast(context.getApplicationContext(), ZMApplication.getZMApplication().getString(R.string.please_select_account));
            return false;
        }
        if (!CommonUtils.checkBankCard(cardNo.replace(" ", ""))) {
            ToastUtil.showShortToast(context.getApplicationContext(), ZMApplication.getZMApplication().getString(R.string.please_input_valid_bank_card_number));
            return false;
        }
        if (!ExtendUtil.isPhoneNumber(phone)) {
            ToastUtil.showShortToast(context.getApplicationContext(), ZMApplication.getZMApplication().getString(R.string.phone_format_error));
            return false;
        }
        return true;
    }

    /**
     * 检测按钮显示状态
     *
     * @param info
     * @param phone
     * @param cardNo
     * @param msgCode
     * @return
     */
    public boolean checkCommitButtonState(BankInfoResponse info, String phone, String cardNo, String msgCode) {
        if (info == null) {
            return false;
        }
        if (!CommonUtils.checkBankCard(cardNo.replace(" ", ""))) {
            return false;
        }
        if (!ExtendUtil.isPhoneNumber(phone)) {
            return false;
        }
        if (msgCode.length() == 0) {
            return false;
        }
        return true;
    }

    /**
     * 提交前的检验
     *
     * @param context
     * @param info
     * @param phone
     * @param msgCode
     */
    public boolean checkCommit(Context context, BankInfoResponse info, String phone, String cardNo, String msgCode) {

        if (info == null) {
            ToastUtil.showShortToast(context.getApplicationContext(), ZMApplication.getZMApplication().getString(R.string.please_select_account));
            return false;
        }
        if (!CommonUtils.checkBankCard(cardNo.replace(" ", ""))) {
            ToastUtil.showShortToast(context.getApplicationContext(), ZMApplication.getZMApplication().getString(R.string.please_input_valid_bank_card_number));
            return false;
        }
        if (!ExtendUtil.isPhoneNumber(phone)) {
            ToastUtil.showShortToast(context.getApplicationContext(), ZMApplication.getZMApplication().getString(R.string.phone_format_error));
            return false;
        }
        if (msgCode.length() == 0) {
            ToastUtil.showShortToast(context.getApplicationContext(), ZMApplication.getZMApplication().getString(R.string.please_input_correct_message_code));
            return false;
        }

        return true;
    }

}
