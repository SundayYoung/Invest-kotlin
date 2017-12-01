package com.weiyankeji.zhongmei.ui.mpresenter;

import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.library.utils.ExtendUtil;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestProductIntroductionResponse;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestWithIdRequest;
import com.weiyankeji.zhongmei.ui.mview.InvestProductIntroductionView;

import java.util.List;

/**
 * Created by liuhaiyang on 2017/8/29.
 */

public class InvestProductIntroductionPresenter extends BasePresenter<InvestProductIntroductionView> {

    public void loadData(String url, InvestWithIdRequest request) {
        mCall = mRestService.postData(url, request);
        mCall.enqueue(new RestBaseCallBack<List<InvestProductIntroductionResponse>>() {
            @Override
            public void onResponse(List<InvestProductIntroductionResponse> response) {
                mView.hideLoading();
                if (ExtendUtil.listIsNullOrEmpty(response)) {
                    return;
                }
                mView.setData(response);
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.hideLoading();
                }
            }
        });
    }

}
