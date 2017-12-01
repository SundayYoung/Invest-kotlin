package com.weiyankeji.zhongmei.ui.mpresenter;

import android.content.Context;
import android.text.TextUtils;

import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.mmodel.mine.AuthNameRequest;
import com.weiyankeji.zhongmei.ui.mmodel.mine.AuthNameResponse;
import com.weiyankeji.zhongmei.ui.mview.AuthNameView;
import com.weiyankeji.zhongmei.utils.CommonUtils;

/**
 * Created by caiwancheng on 2017/9/1.
 */

public class AuthNamePresenter extends BasePresenter<AuthNameView> {

    public void request(String name, String idCard) {
        mView.showLoading();
        mCall = mRestService.postData(UrlConfig.IDCARD_AUTH, new AuthNameRequest(name, idCard));
        mCall.enqueue(new RestBaseCallBack<AuthNameResponse>() {
            @Override
            public void onResponse(AuthNameResponse respone) {
                mView.hideLoading();
                mView.respone(respone);
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.requestFailure(code, msg);
                    mView.hideLoading();
                }
            }
        });
    }

    /**
     * 检测实名认证下一步按钮的状态
     * @param name
     * @param idCard
     * @return
     */
    public boolean checkAuthButtonState(String name, String idCard) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        if (TextUtils.isEmpty(idCard)) {
            return false;
        } else if (!CommonUtils.checkIDCard(idCard)) {
            return false;
        }
        return true;
    }

    /**
     * 实名认证提交时检测
     *
     * @param context
     * @param name
     * @param idCard
     * @return
     */
    public boolean checkAuthNameCommit(Context context, String name, String idCard) {
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showShortToast(context, context.getString(R.string.real_name_not_null));
            return false;
        }

        if (TextUtils.isEmpty(idCard)) {
            ToastUtil.showShortToast(context, context.getString(R.string.id_card_number_not_null));
            return false;
        } else if (!CommonUtils.checkIDCard(idCard)) {
            ToastUtil.showShortToast(context, context.getString(R.string.please_input_valid_idcard));
            return false;
        }
        return true;
    }
}
