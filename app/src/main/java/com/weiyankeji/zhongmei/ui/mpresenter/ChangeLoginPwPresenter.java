package com.weiyankeji.zhongmei.ui.mpresenter;

import com.weiyankeji.library.net.client.RestApiProvider;
import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.library.utils.DialogUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.api.RestService;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.fragment.BaseFragment;
import com.weiyankeji.zhongmei.ui.mmodel.mine.ChangeLoginPwRequest;
import com.weiyankeji.zhongmei.ui.mview.ChangeLoginPwView;

/**
 * Created by caiwancheng on 2017/9/1.
 */

public class ChangeLoginPwPresenter extends BasePresenter<ChangeLoginPwView> {

    public BaseFragment mBaseFragment;

    public ChangeLoginPwPresenter(BaseFragment fragment) {
        mBaseFragment = fragment;
    }

    /**
     * 修改登陆密码
     *
     * @param oldPw
     * @param newPw
     */
    public void changePw(String oldPw, String newPw) {
        mView.showLoading();
        mCall = mRestService.postData(UrlConfig.CHANGE_LOGIN_PW, new ChangeLoginPwRequest(oldPw, newPw));
        mCall.enqueue(new RestBaseCallBack<String>() {

            @Override
            public void onResponse(String res) {
                mView.hideLoading();
                mView.changeLoginPwResponse();
            }


            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    DialogUtil.createHintDialogNoAction(mBaseFragment.mContext, msg, mBaseFragment.getString(R.string.common_ok));
                    mView.hideLoading();
                }
            }
        });
    }

}
