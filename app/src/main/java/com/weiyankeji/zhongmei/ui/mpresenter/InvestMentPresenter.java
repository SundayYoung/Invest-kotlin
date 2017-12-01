package com.weiyankeji.zhongmei.ui.mpresenter;

import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;
import com.weiyankeji.zhongmei.ui.mmodel.bean.AccountInfoBean;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestIncomeRequest;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestIncomeResponse;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestMentRequest;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestMentResponse;
import com.weiyankeji.zhongmei.ui.mview.InvestMentView;


/**
 * Created by caiwancheng on 2017/8/30.
 */

public class InvestMentPresenter extends BasePresenter<InvestMentView> {

    /**
     * 立即投资
     * @param request
     */
    public void postInvest(InvestMentRequest request) {
        mCall = mRestService.postData(UrlConfig.SKU_INVEST, request);
        mCall.enqueue(new RestBaseCallBack<InvestMentResponse>() {
            @Override
            public void onResponse(InvestMentResponse response) {
                if (response == null) {
                    return;
                }

                mView.postInvestComplete(response);
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.postInvestError(code, msg);
                }
            }
        });
    }

    /**
     * 获取账号信息
     */
    public void loadAccountInfoData() {
        mCall = mRestService.postData(UrlConfig.ACCOUNT_INFO, new BaseRequest());
        mCall.enqueue(new RestBaseCallBack<AccountInfoBean>() {
            @Override
            public void onResponse(AccountInfoBean response) {
                if (response == null) {
                    return;
                }
                mView.setAccountInfoData(response);
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.postAccountInfoError(code, msg);
                }
            }
        });
    }

    /**
     * 获取预计收益
     */
    public void getIncome(InvestIncomeRequest request) {
        mCall = mRestService.postData(UrlConfig.PREDICT_INCOME, request);
        mCall.enqueue(new RestBaseCallBack<InvestIncomeResponse>() {
            @Override
            public void onResponse(InvestIncomeResponse response) {
                if (response == null) {
                    return;
                }
                mView.setIncome(response);
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
            }
        });
    }
}
