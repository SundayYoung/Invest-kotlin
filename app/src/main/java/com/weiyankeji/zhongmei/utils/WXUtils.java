package com.weiyankeji.zhongmei.utils;

import android.graphics.Bitmap;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;

/**
 * @aythor: lilei
 * time: 2017/8/15  下午3:28
 * function: 分享
 */

public class WXUtils {


    public static int TYPE_WX_FRIENT = 0;
    public static int TYPE_WX_FRIENT_CIRCLE = 1;

    /**
     * @param mWXApi
     * @param mShareTitle       要分享的标题
     * @param mShareDescription 描述
     * @param mShareBitmap      icon
     * @param mShareCode        分享类型，好友（0）、朋友圈（1)
     */
    public static void shareWXScene(IWXAPI mWXApi, String mShareTitle, String mShareUrl, String mShareDescription, Bitmap mShareBitmap, int mShareCode) {
        WXWebpageObject mWebObject = new WXWebpageObject();
        mWebObject.webpageUrl = mShareUrl;
        WXMediaMessage mMsg = new WXMediaMessage(mWebObject);
        mMsg.title = mShareTitle;
        mMsg.description = mShareDescription;
        mMsg.thumbData = Util.bmpToByteArray(mShareBitmap, true);
        SendMessageToWX.Req mReq = new SendMessageToWX.Req();
        mShareBitmap.recycle();
        mReq.transaction = String.valueOf(System.currentTimeMillis());
        mReq.message = mMsg;
        mReq.scene = mShareCode == TYPE_WX_FRIENT ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        mWXApi.sendReq(mReq);
    }


}
