package com.weiyankeji.zhongmei.ui.mpresenter;

import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.fragment.BaseFragment;
import com.weiyankeji.zhongmei.ui.mmodel.bean.CustomAssetInfoBean;
import com.weiyankeji.zhongmei.ui.mview.CustomView;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;

/**
 * @aythor: lilei
 * time: 2017/9/22  上午10:30
 * function:
 */

public class AssetPresenter extends BasePresenter<CustomView> {
    public void loadData(final BaseFragment baseFragment, Object object) {
        mView.showLoading();
        mCall = mRestService.postData(UrlConfig.MINE_ASSETS, object);
        mCall.enqueue(new RestBaseCallBack<CustomAssetInfoBean>() {
            @Override
            public void onResponse(CustomAssetInfoBean response) {
                if (mView != null) {
                    mView.hideLoading();
                }
                if (response == null) {
                    return;
                }
                mView.setData(response);
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    ErrorMsgUtils.showNetErrorPageOrLogin(baseFragment, code, msg, true);
                    mView.hideLoading();
                }
            }
        });
    }
}
