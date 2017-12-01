package com.weiyankeji.zhongmei.ui.mpresenter;

import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.mmodel.PlatformNoticeDetailResponse;
import com.weiyankeji.zhongmei.ui.mmodel.payment.PlatformNoticeDetailRequset;
import com.weiyankeji.zhongmei.ui.mview.PlatformNoticeDetailView;

/**
 * 平台公告详情
 * Created by zff on 2017/9/1.
 */
public class PlatformNoticeDetailPresenter extends BasePresenter<PlatformNoticeDetailView> {

    public void loadData(PlatformNoticeDetailRequset requset) {
        mView.showLoading();
        mCall = mRestService.postData((UrlConfig.PLATFORM_NOTICE_DETAIL), requset);
        mCall.enqueue(new RestBaseCallBack<PlatformNoticeDetailResponse>() {
            @Override
            public void onResponse(PlatformNoticeDetailResponse itemBean) {
                mView.hideLoading();
                if (itemBean != null) {
                    mView.loadNotices(itemBean);
                }
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
