package com.weiyankeji.zhongmei.ui.mpresenter;

import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.library.utils.ExtendUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.fragment.BaseFragment;
import com.weiyankeji.zhongmei.ui.mmodel.BankInfoResponse;
import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;
import com.weiyankeji.zhongmei.ui.mview.BankListView;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;

import java.util.List;

/**
 * Created by caiwancheng on 2017/8/30.
 */

public class BankListPresenter extends BasePresenter<BankListView> {


    /**
     * 加载银行列表
     */
    public void loadBankList(final BaseFragment fragment) {
        mView.showLoading();
        mCall = mRestService.postData(UrlConfig.BANK_LIST, new BaseRequest());
        mCall.enqueue(new RestBaseCallBack<List<BankInfoResponse>>() {
            @Override
            public void onResponse(List<BankInfoResponse> itemBeans) {
                mView.hideLoading();
                if (!ExtendUtil.listIsNullOrEmpty(itemBeans)) {
                    mView.requstBankList(itemBeans);
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
}
