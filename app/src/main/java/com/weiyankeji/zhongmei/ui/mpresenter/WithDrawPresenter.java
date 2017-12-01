package com.weiyankeji.zhongmei.ui.mpresenter;


import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.library.utils.StringUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.mmodel.BankInfoResponse;
import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;
import com.weiyankeji.zhongmei.ui.mmodel.bean.AccountInfoBean;
import com.weiyankeji.zhongmei.ui.mmodel.payment.AccountChargeRequest;
import com.weiyankeji.zhongmei.ui.mmodel.payment.WithdrawCashRequest;
import com.weiyankeji.zhongmei.ui.mview.WithDrawView;


/**
 * Created by caiwancheng on 2017/9/4.
 */

public class WithDrawPresenter extends BasePresenter<WithDrawView> {

    public void loadData() {
        mView.showLoading();
        loadBankInfo();
        loadAccountInfo();
    }

    private void loadBankInfo() {
        mCall = mRestService.postData(UrlConfig.BANK_INFO, new BaseRequest());
        mCall.enqueue(new RestBaseCallBack<BankInfoResponse>() {
            @Override
            public void onResponse(BankInfoResponse response) {
                mView.hideLoading();
                if (response == null || StringUtil.isNullOrEmpty(response.bank_card_no)) {
                    return;
                }
                mView.setBankData(response);
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.hideLoading();
                }

            }
        });
    }

    private void loadAccountInfo() {
        mCall = mRestService.postData(UrlConfig.ACCOUNT_INFO, new BaseRequest());
        mCall.enqueue(new RestBaseCallBack<AccountInfoBean>() {
            @Override
            public void onResponse(AccountInfoBean response) {
                mView.hideLoading();
                if (response == null) {
                    return;
                }
                mView.setAccountData(response);

            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.hideLoading();
                    mView.showInitDataFail(code, msg);
                }

            }
        });
    }


    //充值
    public void submitChargeInfo(AccountChargeRequest request) {
        mCall = mRestService.postData(UrlConfig.ACCOUNT_CHARGE, request);
        mCall.enqueue(new RestBaseCallBack<String>() {
            @Override
            public void onResponse(String response) {
                if (mView != null) {
                    mView.showCommitSuccess();
                }

            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.hideLoading();
                    mView.showCommitFail(code, msg);
                }

            }
        });
    }

    //提现
    public void submitWithdrawInfo(WithdrawCashRequest request) {
        mCall = mRestService.postData(UrlConfig.WITHDRAW_CASH, request);
        mCall.enqueue(new RestBaseCallBack<String>() {
            @Override
            public void onResponse(String response) {
                if (mView != null) {
                    mView.showCommitSuccess();
                }

            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.hideLoading();
                    mView.showCommitFail(code, msg);
                }

            }
        });
    }

}
