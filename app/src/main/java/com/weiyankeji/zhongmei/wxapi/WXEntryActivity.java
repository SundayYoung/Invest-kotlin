package com.weiyankeji.zhongmei.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.application.ZMApplication;

/**
 * @aythor: lilei
 * time: 2017/8/15  下午3:28
 * function: 回调微信分享结果
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI mApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApi = ZMApplication.sWXApi;
        mApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mApi.handleIntent(intent, this);
    }

    /**
     * 请求（实验证明没有效果）
     *
     * @param baseReq
     */
    @Override
    public void onReq(BaseReq baseReq) {
        switch (baseReq.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                break;
            default:
                break;
        }
    }

    /**
     * 结果回调
     *
     * @param baseResp
     */
    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                ToastUtil.showShortToast(this, getString(R.string.wechat_share_success));
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                ToastUtil.showShortToast(this, getString(R.string.wechat_share_cancle));
                break;
            default:
                ToastUtil.showShortToast(this, getString(R.string.wechat_share_failure));
                break;
        }
        finish();
    }

}