package com.weiyankeji.zhongmei.ui.mpresenter;

import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.fragment.BaseFragment;
import com.weiyankeji.zhongmei.ui.mmodel.BackPlanResponse;
import com.weiyankeji.zhongmei.ui.mview.BackPlanView;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;


/**
 * @aythor: lilei
 * time: 2017/9/10  下午5:07
 * function:
 */

public class BackPlanPresenter extends BasePresenter<BackPlanView> {
    public void loadData(Object object, final BaseFragment fragment) {
        mView.showLoading();
        mCall = mRestService.postData(UrlConfig.MINE_BACK_PLAN, object);
        mCall.enqueue(new RestBaseCallBack<BackPlanResponse>() {
            @Override
            public void onResponse(BackPlanResponse data) {
                mView.hideLoading();
                if (data == null) {
                    return;
                }
                mView.setData(data);
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.hideLoading();
                    ErrorMsgUtils.showNetErrorPageOrLogin(fragment, code, msg);
                }
            }
        });
    }
}
