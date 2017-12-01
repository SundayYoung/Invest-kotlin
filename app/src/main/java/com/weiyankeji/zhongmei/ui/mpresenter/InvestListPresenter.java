package com.weiyankeji.zhongmei.ui.mpresenter;

import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.library.utils.ExtendUtil;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.mmodel.bean.InvestDetailBean;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestListRequest;
import com.weiyankeji.zhongmei.ui.mview.InvestListView;
import java.util.List;

/**
 * Created by liuhaiyang on 2017/8/23.
 */

public class InvestListPresenter extends BasePresenter<InvestListView> {

    public void loadData(InvestListRequest request, boolean isShowLoading) {
        if (isShowLoading) {
            mView.showLoading();
        }
        mCall = mRestService.postData(UrlConfig.INVEST_LIST, request);
        mCall.enqueue(new RestBaseCallBack<List<InvestDetailBean>>() {
            @Override
            public void onResponse(List<InvestDetailBean> response) {
                mView.hideLoading();
                if (ExtendUtil.listIsNullOrEmpty(response)) {
                    return;
                }
                mView.setListData(response);
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
