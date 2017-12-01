package com.weiyankeji.zhongmei.ui.mpresenter;


import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.mmodel.payment.FeedBackRequest;
import com.weiyankeji.zhongmei.ui.mview.FeekBackView;

/**
 * Created by zff on 2017/8/31.
 */

public class FeekBackPresenter extends BasePresenter<FeekBackView> {

    public void loadData(FeedBackRequest request) {
        mView.showLoading();
        mCall = mRestService.postData(UrlConfig.FEEK_BACK, request);
        mCall.enqueue(new RestBaseCallBack<String>() {
            @Override
            public void onResponse(String response) {
                mView.hideLoading();
                mView.showCommitSuccess(response);
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
