package com.weiyankeji.zhongmei.ui.mpresenter;


import android.os.Bundle;

import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.mmodel.bean.InviteRewardBean;
import com.weiyankeji.zhongmei.ui.mmodel.mine.InviteResponse;
import com.weiyankeji.zhongmei.ui.mmodel.mine.MyInviteRequest;
import com.weiyankeji.zhongmei.ui.mview.InviteView;
import com.weiyankeji.zhongmei.utils.ConstantUtils;

import java.util.List;


/**
 * Created by liuhaiyang on 2017/9/27.
 */

public class InvitePresenter extends BasePresenter<InviteView> {

    //累计邀请人
    public void loadData(MyInviteRequest request, boolean isShowLoading) {
        if (isShowLoading) {
            mView.showLoading();
        }
        mCall = mRestService.postData(UrlConfig.MY_INVITE, request);
        mCall.enqueue(new RestBaseCallBack<InviteResponse>() {
            @Override
            public void onResponse(InviteResponse response) {
                mView.hideLoading();
                if (response == null) {
                    return;
                }
                mView.setListData(response);
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.hideLoading();
                    mView.responseFail(code, msg);
                }
            }
        });
    }

    //累计奖励
    public void loadRewardData(MyInviteRequest request, boolean isShowLoading) {
        if (isShowLoading) {
            mView.showLoading();
        }
        mCall = mRestService.postData(UrlConfig.MY_REWARD, request);
        mCall.enqueue(new RestBaseCallBack<List<InviteRewardBean>>() {
            @Override
            public void onResponse(List<InviteRewardBean> response) {
                mView.hideLoading();
                if (response == null) {
                    return;
                }
                mView.setRewardData(response);
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.hideLoading();
                    mView.responseFail(code, msg);
                }
            }
        });
    }

    public Bundle getBundle(String uuid, int bonus, String userName) {
        Bundle bundle = new Bundle();
        bundle.putString(ConstantUtils.KEY_ID, uuid);
        bundle.putInt(ConstantUtils.KEY_DATA, bonus);
        bundle.putString(ConstantUtils.KEY_TITLE, userName);
        bundle.putInt(ConstantUtils.KEY_TYPE, ConstantUtils.BOUNS_DETAILS_INVITEE);
        return bundle;
    }
}
