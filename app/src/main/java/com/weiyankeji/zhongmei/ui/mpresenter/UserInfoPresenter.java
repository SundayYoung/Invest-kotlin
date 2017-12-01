package com.weiyankeji.zhongmei.ui.mpresenter;

import com.weiyankeji.library.net.client.RestApiProvider;
import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.zhongmei.api.RestService;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.fragment.BaseFragment;
import com.weiyankeji.zhongmei.ui.mmodel.bean.User;
import com.weiyankeji.zhongmei.ui.mview.UserInfoView;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;

/**
 * @aythor: lilei
 * time: 2017/9/1  下午4:48
 * function:
 */

public class UserInfoPresenter extends BasePresenter<UserInfoView> {
    public void loadUserInfo(final BaseFragment baseFragment, Object object) {
        mView.showLoading();
        mCall = mRestService.postData(UrlConfig.USER_INFO, object);
        mCall.enqueue(new RestBaseCallBack<User>() {
            @Override
            public void onResponse(User response) {
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
                    ErrorMsgUtils.showNetErrorToastOrLogin(baseFragment, code, msg, true);
                    mView.hideLoading();
                }
            }
        });
    }

    public void logout(Object object) {
        mView.showLoading();
        mCall = mRestService.postData(UrlConfig.LOGOUT, object);
        mCall.enqueue(new RestBaseCallBack<Object>() {
            @Override
            public void onResponse(Object response) {
                if (mView != null) {
                    mView.hideLoading();
                }
                mView.onSuccess();
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.hideLoading();
                    mView.onFailure(code, msg);
                }
            }
        });

    }

}
