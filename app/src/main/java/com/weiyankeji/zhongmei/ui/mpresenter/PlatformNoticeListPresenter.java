package com.weiyankeji.zhongmei.ui.mpresenter;


import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.mmodel.PlatformNoticeResponse;
import com.weiyankeji.zhongmei.ui.mmodel.payment.PlatformNoticeListRequest;
import com.weiyankeji.zhongmei.ui.mview.PlatformNoticeListView;
import com.weiyankeji.zhongmei.utils.ConstantUtils;

import java.util.List;

/**
 * 公告列表
 * Created by zff on 2017/9/1.
 */

public class PlatformNoticeListPresenter extends BasePresenter<PlatformNoticeListView> {
    private int mPage = 1;

    public void loadData(boolean isFirst) {
        if (isFirst) {
            mView.showLoading();
        }
        mCall = mRestService.postData((UrlConfig.PLATFORM_NOTICE_LIST), new PlatformNoticeListRequest(mPage, ConstantUtils.PAGE_SIZE));
        mCall.enqueue(new RestBaseCallBack<List<PlatformNoticeResponse>>() {
            @Override
            public void onResponse(List<PlatformNoticeResponse> itemBeans) {
                mView.hideLoading();
                mView.loadNotices(itemBeans);
                mPage += 1;
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.hideLoading();
                    mView.showLoadFial(code, msg);
                }
            }
        });
    }

}
