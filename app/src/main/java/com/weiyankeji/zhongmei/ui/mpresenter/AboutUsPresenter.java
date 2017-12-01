package com.weiyankeji.zhongmei.ui.mpresenter;

import android.text.TextUtils;

import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.fragment.BaseFragment;
import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;
import com.weiyankeji.zhongmei.ui.mmodel.multi.CheckUpdateResponse;
import com.weiyankeji.zhongmei.ui.mview.BaseView;
import com.weiyankeji.zhongmei.utils.CheckUpdateUtils;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;

/**
 * @aythor: lilei
 * time: 2017/9/22  上午10:30
 * function:
 */

public class AboutUsPresenter extends BasePresenter<BaseView> {


    public void checkUpdate(final BaseFragment fragment) {
        mView.showLoading();
        mCall = mRestService.postData(UrlConfig.CHECK_UPDATE, new BaseRequest());
        mCall.enqueue(new RestBaseCallBack<CheckUpdateResponse>() {
            @Override
            public void onResponse(CheckUpdateResponse response) {
                if (mView != null) {
                    mView.hideLoading();
                    if (response != null) {
                        if (TextUtils.isEmpty(response.apkUrl)) {
                            ToastUtil.showShortToast(ZMApplication.getZMApplication(), ZMApplication.getZMApplication().getString(R.string.is_the_latest_version));
                        } else {
                            CheckUpdateUtils.showUpdateDialog(fragment, response);
                        }
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    if (!ErrorMsgUtils.showNetErrorToastOrLogin(fragment, code, msg)) {
                        ToastUtil.showShortToast(ZMApplication.getZMApplication(), msg);
                    }
                    mView.hideLoading();
                }
            }
        });
    }

}
