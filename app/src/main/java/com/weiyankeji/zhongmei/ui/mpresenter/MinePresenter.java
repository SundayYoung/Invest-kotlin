package com.weiyankeji.zhongmei.ui.mpresenter;


import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.mmodel.MineResponse;
import com.weiyankeji.zhongmei.ui.mview.MineView;


/**
 * @aythor: lilei
 * time: 2017/8/29  上午10:01
 * function:
 */

public class MinePresenter extends BasePresenter<MineView> {

    /**
     * 我的主页面请求
     *
     * @param object
     */
    public void loadData(Object object) {
        mView.showLoading();
        mCall = mRestService.postData(UrlConfig.MINE_INDEX, object);
        mCall.enqueue(new RestBaseCallBack<MineResponse>() {
            @Override
            public void onResponse(MineResponse response) {
                if (mView != null){
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
                    mView.failureToken(code, msg);
                    mView.hideLoading();
                }
            }
        });
    }

}
