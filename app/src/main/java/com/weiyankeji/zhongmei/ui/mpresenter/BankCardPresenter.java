package com.weiyankeji.zhongmei.ui.mpresenter;

import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.fragment.BaseFragment;
import com.weiyankeji.zhongmei.ui.mmodel.BankInfoResponse;
import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;
import com.weiyankeji.zhongmei.ui.mview.BankCardView;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;

/**
 * Created by caiwancheng on 2017/8/28.
 */

public class BankCardPresenter extends BasePresenter<BankCardView> {

    public void loadData(final BaseFragment fragment) {
        mView.showLoading();
        mCall = mRestService.postData(UrlConfig.BANK_INFO, new BaseRequest());
        mCall.enqueue(new RestBaseCallBack<BankInfoResponse>() {

            @Override
            public void onResponse(BankInfoResponse data) {
                mView.hideLoading();
                if (data != null) {
                    mView.request(data);
                }
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    if (!ErrorMsgUtils.showNetErrorPageOrLogin(fragment, code, msg)) {
                        ToastUtil.showShortToast(ZMApplication.getZMApplication(), msg);
                    }
                    mView.hideLoading();
                }
            }
        });
    }

    /**
     * 检查银行卡是否更新
     */
    public void checkBankIsCanChange() {
        mView.showLoading();
        mCall = mRestService.postData(UrlConfig.CHECK_BANK_CARD, new BaseRequest());
        mCall.enqueue(new RestBaseCallBack<BankInfoResponse>() {

            @Override
            public void onResponse(BankInfoResponse data) {
                mView.hideLoading();
                mView.changeRequest();
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.hideLoading();
                    mView.changeFailure(msg);
                }
            }
        });
    }
}
