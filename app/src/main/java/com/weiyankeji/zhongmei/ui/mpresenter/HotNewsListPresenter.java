package com.weiyankeji.zhongmei.ui.mpresenter;




import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.mmodel.HotNewsResponse;
import com.weiyankeji.zhongmei.ui.mmodel.payment.PlatformNoticeListRequest;
import com.weiyankeji.zhongmei.ui.mview.HotNewsListView;
import com.weiyankeji.zhongmei.utils.ConstantUtils;


import java.util.List;

/**
 * 热点新闻
 * Created by zff on 2017/9/7.
 */

public class HotNewsListPresenter extends BasePresenter<HotNewsListView> {



    public void loadData(boolean isFirst, int page) {
        if (isFirst) {
            mView.showLoading();
        }

        mCall = mRestService.postData(UrlConfig.HOT_NEWS_LIST, new PlatformNoticeListRequest(page, ConstantUtils.PAGE_SIZE));
        mCall.enqueue(new RestBaseCallBack<List<HotNewsResponse>>() {
            @Override
            public void onResponse(List<HotNewsResponse> itemBeans) {
                mView.hideLoading();
                mView.loadNotices(itemBeans);
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
