package com.weiyankeji.zhongmei.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.gzsll.jsbridge.WVJBWebView;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.mmodel.bean.User;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.UserUtils;

import org.json.JSONObject;

import butterknife.BindView;

/**
 * @aythor: lilei
 * time: 2017/9/30  下午1:22
 * function:
 */

public class FaceWebViewFragment extends BaseFragment {
    @BindView(R.id.webView)
    WVJBWebView mWvjbWebView;
    @BindView(R.id.progressBar)
    ProgressBar mPbLoading;

    User mUser;

    @Override
    public int setContentLayout() {
        return R.layout.fragment_face_webview;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        addBackListener();
        mUser = UserUtils.getInstance().getUserObject();
        setWebView();

    }


    /**
     * 设置webview
     */
    public void setWebView() {

        WebSettings webSettings = mWvjbWebView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);

        mWvjbWebView.loadUrl(UrlConfig.H5_QRCODE);
        mWvjbWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (mPbLoading != null) {
                    mPbLoading.setProgress(newProgress);
                    if (newProgress == 100) {
                        mPbLoading.setVisibility(View.GONE);
                        mWvjbWebView.callHandler(ConstantUtils.H5_METHOD, struJson(), new WVJBWebView.WVJBResponseCallback() {
                            @Override
                            public void callback(Object data) {

                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * 构建新的json
     *
     * @return
     */
    public String struJson() {
        if (mUser == null) {
            return null;
        }
        JSONObject mJsonObject = new JSONObject();
        try {
            if (mUser.getAvatar() == null || TextUtils.isEmpty(mUser.getAvatar())) {
                mJsonObject.put(ConstantUtils.H5_SKEY_AVATAR, "");
            } else {
                mJsonObject.put(ConstantUtils.H5_SKEY_AVATAR, mUser.getAvatar());
            }
            mJsonObject.put(ConstantUtils.H5_SKEY_MOBILE, mUser.getMobile());
            mJsonObject.put(ConstantUtils.H5_SKEY_QRCODE, mUser.getQr_code());
        } catch (Exception e) {

        }
        return mJsonObject.toString();
    }

}
