package com.weiyankeji.zhongmei.ui.mpresenter;

import android.content.Intent;

import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.activity.MainActivity;
import com.weiyankeji.zhongmei.ui.fragment.BaseFragment;
import com.weiyankeji.zhongmei.ui.mmodel.bean.User;
import com.weiyankeji.zhongmei.ui.mmodel.multi.LoginRequest;
import com.weiyankeji.zhongmei.ui.mview.LoginView;
import com.weiyankeji.zhongmei.utils.ConstantUtils;

/**
 * @aythor: lilei
 * time: 2017/8/30  上午11:41
 * function:
 */

public class LoginPresenter extends BasePresenter<LoginView> {
    public int mIntentType;

    public void loadData(LoginRequest loginRequest) {
        mView.showLoading();
        mCall = mRestService.postData(UrlConfig.LOGIN, loginRequest);
        mCall.enqueue(new RestBaseCallBack<User>() {
            @Override
            public void onResponse(User user) {
                if (mView != null) {
                    mView.hideLoading();
                    if (user == null) {
                        return;
                    }
                    mView.setData(user);
                }

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

    /**
     * 跳转首页
     *
     * @param fragment
     * @param type
     */
    public void intentMainActivity(BaseFragment fragment, int type) {
        if (type > 0) {
            Intent intent = new Intent(fragment.getActivity(), MainActivity.class);
            intent.putExtra(ConstantUtils.KEY_TYPE, type);
            fragment.getActivity().startActivity(intent);
        }

        fragment.getActivity().onBackPressed();
    }
}
