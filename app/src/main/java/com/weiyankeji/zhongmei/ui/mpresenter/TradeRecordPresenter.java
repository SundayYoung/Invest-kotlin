package com.weiyankeji.zhongmei.ui.mpresenter;

import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.fragment.BaseFragment;
import com.weiyankeji.zhongmei.ui.mmodel.mine.TradeRecordRequest;
import com.weiyankeji.zhongmei.ui.mmodel.mine.TradeRecordResponse;
import com.weiyankeji.zhongmei.ui.mview.TradeRecordView;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;

import java.util.List;

/**
 * 交易记录
 * Created by caiwancheng on 2017/8/25.
 */

public class TradeRecordPresenter extends BasePresenter<TradeRecordView> {

    private TradeRecordRequest mTradeRecordRequest;
    public int mPage;
    private BaseFragment mBaseFragment;

    public TradeRecordPresenter(BaseFragment fragment) {
        mBaseFragment = fragment;
        mTradeRecordRequest = new TradeRecordRequest();
    }

    /**
     * 加载更多
     */
    public void loadMore() {
        mTradeRecordRequest.page += 1;
        loadData();
    }

    /**
     * 刷新
     */
    public void loadRefresh() {
        mTradeRecordRequest.page = 1;
        loadData();
    }

    public void loadData() {
        mView.showLoading();
        mCall = mRestService.postData(UrlConfig.TRADE_RECORD, mTradeRecordRequest);
        mCall.enqueue(new RestBaseCallBack<List<TradeRecordResponse>>() {
            @Override
            public void onResponse(List<TradeRecordResponse> itemBeans) {
                mView.hideLoading();
                mView.loadRecords(itemBeans);
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    if (!ErrorMsgUtils.showNetErrorPageOrLogin(mBaseFragment, code, msg)) {
                        ToastUtil.showShortToast(ZMApplication.getZMApplication(), msg);
                    }
                    mView.hideLoading();
                }
            }
        });
    }

}
