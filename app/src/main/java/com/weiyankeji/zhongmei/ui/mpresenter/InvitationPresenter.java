package com.weiyankeji.zhongmei.ui.mpresenter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;
import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.library.utils.ImageLoaderUtil;

import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.mmodel.bean.InviteShareBean;
import com.weiyankeji.zhongmei.ui.mview.InvitationView;


import butterknife.ButterKnife;

/**
 * @aythor: lilei
 * time: 2017/9/29  下午1:45
 * function:
 */

public class InvitationPresenter extends BasePresenter<InvitationView> implements View.OnClickListener {
    ImageButton mIbClose;
    TextView mTvWechat;
    TextView mTvFrient;
    Dialog mBottomDialog;
    Context mContext;
    InviteShareBean mShareContent;
    IWXAPI mSWXApi;

    public void loadData(Object object) {
        mView.showLoading();
        mCall = mRestService.postData(UrlConfig.INVATE_SHARE, object);
        mCall.enqueue(new RestBaseCallBack<InviteShareBean>() {
            @Override
            public void onResponse(InviteShareBean response) {
                if (mView != null) {
                    mView.hideLoading();
                    if (response == null) {
                        return;
                    }
                    mView.setData(response);
                }
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.hideLoading();
                }
            }
        });
    }

    public void showDialog(Context mContext, InviteShareBean inviteShareBean) {
        this.mContext = mContext;
        this.mShareContent = inviteShareBean;
        mBottomDialog = new Dialog(mContext, R.style.BottomDialog);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_invert, null);
        mIbClose = ButterKnife.findById(contentView, R.id.ibn_close);
        mTvWechat = ButterKnife.findById(contentView, R.id.tv_share_wechat);
        mTvFrient = ButterKnife.findById(contentView, R.id.tv_share_friends);
        mIbClose.setOnClickListener(this);
        mTvWechat.setOnClickListener(this);
        mTvFrient.setOnClickListener(this);
        mBottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = mContext.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        mBottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        mBottomDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibn_close:
                break;
            case R.id.tv_share_wechat:
                share(SendMessageToWX.Req.WXSceneSession);
                break;
            case R.id.tv_share_friends:
                share(SendMessageToWX.Req.WXSceneTimeline);
                break;
            default:
        }
        mBottomDialog.dismiss();
    }

    public void share(final int tag) {
        mSWXApi = ZMApplication.sWXApi;
        if (mShareContent == null) {
            return;
        }
        if (!mSWXApi.isWXAppInstalled()) {
            ToastUtil.showLongToast(mContext, mContext.getString(R.string.wechat_noinstall));
        } else if (!mSWXApi.isWXAppSupportAPI()) {
            ToastUtil.showLongToast(mContext, mContext.getString(R.string.wechat_update));
        } else {
            final WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = mShareContent.link;
            final WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = mShareContent.title;
            msg.description = mShareContent.desc;
            if (TextUtils.isEmpty(mShareContent.icon)) {
                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.login_logo);
                shareWechat(msg, bitmap, tag);
            } else {
                ImageLoaderUtil.callBackBitmap(mContext, mShareContent.icon).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, com.bumptech.glide.request.transition.Transition transition) {
                        shareWechat(msg, resource, tag);
                    }
                });
            }
        }
    }


    public void shareWechat(WXMediaMessage msg, Bitmap bitResult, int mTag) {
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitResult, 100, 100, true);
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = mTag;
        mSWXApi.sendReq(req);
    }

}

