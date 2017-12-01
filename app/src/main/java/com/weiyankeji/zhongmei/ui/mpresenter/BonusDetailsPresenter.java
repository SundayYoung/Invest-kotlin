package com.weiyankeji.zhongmei.ui.mpresenter;

import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.mmodel.bonus.BonusDaysRequest;
import com.weiyankeji.zhongmei.ui.mmodel.bonus.BonusDaysResponse;
import com.weiyankeji.zhongmei.ui.mmodel.bonus.BonusInviteeRequest;
import com.weiyankeji.zhongmei.ui.mmodel.bonus.BonusInviteeResponse;
import com.weiyankeji.zhongmei.ui.mview.BonusDetailsView;

import java.util.List;

/**
 * Created by zff on 2017/9/26.
 */

public class BonusDetailsPresenter extends BasePresenter<BonusDetailsView> {


    /**
     * 根据邀请人奖励
     */
    public void loadBonusInviteeData(String inviteeUuid, int page, boolean isFirst) {
        if (isFirst) {
            mView.showLoading();
        }

        mCall = mRestService.postData(UrlConfig.BONUS_INVITEE, new BonusInviteeRequest(inviteeUuid, page));
        mCall.enqueue(new RestBaseCallBack<List<BonusInviteeResponse>>() {
            @Override
            public void onResponse(List<BonusInviteeResponse> itemBeans) {
                mView.hideLoading();
                mView.setListData(itemBeans);
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

    /**
     * 根据天数奖励明细
     */
    public void loadBonusDayData(long date, int page, boolean isFirst) {
        if (isFirst) {
            mView.showLoading();
        }

        mCall = mRestService.postData(UrlConfig.BONUS_DAY, new BonusDaysRequest(date, page));
        mCall.enqueue(new RestBaseCallBack<List<BonusDaysResponse>>() {
            @Override
            public void onResponse(List<BonusDaysResponse> itemBeans) {
                mView.hideLoading();
                mView.setListData(itemBeans);
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
