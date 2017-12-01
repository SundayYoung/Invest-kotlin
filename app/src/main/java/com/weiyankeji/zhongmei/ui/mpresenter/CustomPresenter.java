package com.weiyankeji.zhongmei.ui.mpresenter;

import com.weiyankeji.library.net.client.RestApiProvider;
import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.zhongmei.api.RestService;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.fragment.BaseFragment;
import com.weiyankeji.zhongmei.ui.mmodel.bean.CustomAssetInfoBean;
import com.weiyankeji.zhongmei.ui.mview.CustomView;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;

/**
 * @aythor: lilei
 * time: 2017/8/30  下午5:10
 * function:
 */

public class CustomPresenter extends BasePresenter<CustomView> {
    public void loadData(final BaseFragment baseFragment, Object object) {
        mView.showLoading();
        mCall = mRestService.postData(UrlConfig.MINE_ASSETS, object);
        mCall.enqueue(new RestBaseCallBack<CustomAssetInfoBean>() {
            @Override
            public void onResponse(CustomAssetInfoBean response) {
                if (mView != null) {
                    mView.hideLoading();
                    if (response == null) {
                        return;
                    }
                    mView.setData(response);
                }

            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    ErrorMsgUtils.showNetErrorToastOrLogin(baseFragment, code, msg, true);
                    mView.hideLoading();
                }
            }
        });
    }
}
