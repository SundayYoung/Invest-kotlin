package com.weiyankeji.zhongmei.ui.mpresenter;

import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.mmodel.mine.AuthCodeCheckRequest;
import com.weiyankeji.zhongmei.ui.mview.AuthCodeCheckView;

/**
 * Created by caiwancheng on 2017/9/1.
 */

public class AuthCodeCheckPresenter extends BasePresenter<AuthCodeCheckView> {

    public void request(String code) {
        mView.showLoading();
        AuthCodeCheckRequest request = new AuthCodeCheckRequest(code);
        mCall = mRestService.postData(UrlConfig.VERIFY_SKU_CODE, request);
        mCall.enqueue(new RestBaseCallBack<String>() {
            @Override
            public void onResponse(String respone) {
                mView.hideLoading();
                mView.respone();
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.responeFail(code, msg);
                    mView.hideLoading();
                }
            }
        });
    }
}
