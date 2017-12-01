package com.weiyankeji.zhongmei.ui.mpresenter;

import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.library.utils.ExtendUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.fragment.BaseFragment;
import com.weiyankeji.zhongmei.ui.mmodel.TicketListResponse;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestTicketRequest;
import com.weiyankeji.zhongmei.ui.mmodel.multi.MyTicketRequest;
import com.weiyankeji.zhongmei.ui.mview.TicketListView;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;

import java.util.List;

/**
 * Created by liuhaiyang on 2017/8/23.
 */

public class TicketListPresenter extends BasePresenter<TicketListView> {
    public int mPage = 1;

    public void loadInvestTicketDate(final BaseFragment fragment, String skuId, int type, long amount) {
        mView.showLoading();
        InvestTicketRequest request = new InvestTicketRequest();
        request.sku_id = skuId;
        request.type = type;
        request.invest_amount = amount;
        mCall = mRestService.postData(UrlConfig.TICKET_LIST, request);
        mCall.enqueue(new RestBaseCallBack<List<TicketListResponse>>() {
            @Override
            public void onResponse(List<TicketListResponse> response) {
                mView.hideLoading();
                if (ExtendUtil.listIsNullOrEmpty(response)) {
                    fragment.setErrorPageView(R.drawable.common_img_noticket, ZMApplication.getZMApplication().getString(R.string.you_not_have_available_ticket), null, null);
                    return;
                }
                mView.setListData(response);
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    if (!ErrorMsgUtils.showNetErrorPageOrLogin(fragment, code, msg)) {
                        ToastUtil.showShortToast(ZMApplication.getZMApplication(), msg);
                    }
                    mView.hideLoading();
                }
            }
        });
    }


    public void loadMyTicketData(final BaseFragment fragment, String type) {
        mView.showLoading();
        MyTicketRequest request = new MyTicketRequest();
        request.page = mPage;
        request.status = type;
        request.page_size = ConstantUtils.PAGE_SIZE;
        mCall = mRestService.postData(UrlConfig.TICKET_LISTS, request);
        mCall.enqueue(new RestBaseCallBack<List<TicketListResponse>>() {
            @Override
            public void onResponse(List<TicketListResponse> response) {
                mView.hideLoading();
                if (ExtendUtil.listIsNullOrEmpty(response)) {
                    fragment.setErrorPageView(R.drawable.common_img_noticket, ZMApplication.getZMApplication().getString(R.string.you_not_have_available_ticket), null, null);
                    return;
                }
                mPage += 1;
                mView.setListData(response);
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    if (!ErrorMsgUtils.showNetErrorPageOrLogin(fragment, code, msg)) {
                        ToastUtil.showShortToast(ZMApplication.getZMApplication(), msg);
                    }
                    mView.hideLoading();
                }
            }
        });
    }


}
