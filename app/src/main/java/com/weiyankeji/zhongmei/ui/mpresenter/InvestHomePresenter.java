package com.weiyankeji.zhongmei.ui.mpresenter;

import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestHomeResponse;
import com.weiyankeji.zhongmei.ui.mview.InvestHomeView;


/**
 * 投资理财 首页
 * Created by liuhaiyang on 2017/8/2.
 */

public class InvestHomePresenter extends BasePresenter<InvestHomeView> {

    public void loadData(boolean isShowLoading) {
        if (isShowLoading) {
            mView.showLoading();
        }
        mCall = mRestService.postData(UrlConfig.INVEST_INDEX, new BaseRequest());
        mCall.enqueue(new RestBaseCallBack<InvestHomeResponse>() {
            @Override
            public void onResponse(InvestHomeResponse response) {
                mView.hideLoading();
                if (response == null) {
                    return;
                }

                mView.setData(response);
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.hideLoading();
                    mView.netError(code, msg);
                }
            }
        });
    }

}
