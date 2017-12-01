package com.weiyankeji.zhongmei.ui.mpresenter;

import com.weiyankeji.library.net.client.RestApiProvider;
import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.zhongmei.api.RestService;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.mmodel.bean.InvestRecordResponse;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestRecordRequest;
import com.weiyankeji.zhongmei.ui.mview.InvestRecordView;

import java.util.List;

/**
 * Created by caiwancheng on 2017/8/30.
 */

public class InvestRecordPresenter extends BasePresenter<InvestRecordView> {

    public void loadData(InvestRecordRequest request) {
        mView.showLoading();
        mCall = mRestService.postData(UrlConfig.INVEST_RECORD, request);
        mCall.enqueue(new RestBaseCallBack<List<InvestRecordResponse>>() {
            @Override
            public void onResponse(List<InvestRecordResponse> response) {
                mView.hideLoading();
                mView.setData(response);
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.responseFail(code, msg);
                    mView.hideLoading();
                }
            }
        });
    }
}
