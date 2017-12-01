package com.weiyankeji.zhongmei.ui.fragment;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.customview.ErrorPageView;
import com.weiyankeji.zhongmei.ui.customview.TitleBarView;
import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;
import com.weiyankeji.zhongmei.ui.mmodel.bean.InviteShareBean;
import com.weiyankeji.zhongmei.ui.mmodel.bean.MenuBean;
import com.weiyankeji.zhongmei.ui.mpresenter.InvitationPresenter;
import com.weiyankeji.zhongmei.ui.mview.InvitationView;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @aythor: lilei
 * time: 2017/9/29  下午1:36
 * function:
 */

public class InvitationFragment extends BaseMvpFragment<InvitationView, InvitationPresenter> implements InvitationView {
    @BindView(R.id.webview)
    WebView mWebView;
    @BindView(R.id.progressBar)
    ProgressBar mPbLoading;

    InvitationPresenter mInvitationPresenter;
    String mUrl;

    InviteShareBean mShareContent;

    @Override
    public InvitationPresenter initPresenter() {
        mInvitationPresenter = new InvitationPresenter();
        return mInvitationPresenter;
    }

    @Override
    public int setContentLayout() {
        return R.layout.fragment_invitation;

    }

    @Override
    public void finishCreateView(Bundle bundle) {
        setTitle();
        mInvitationPresenter.loadData(new BaseRequest());
        setWebView();
    }

    /**
     * webview设置以及加载
     */
    public void setWebView() {
        WebSettings webSettings = mWebView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);

        mWebView.loadUrl(UrlConfig.H5_RULE);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.getUrl().toString());
                }
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                view.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= 23) {
                    if (error.getErrorCode() == WebViewClient.ERROR_HOST_LOOKUP) {
                        ErrorMsgUtils.showNetErrView(InvitationFragment.this);
                    } else {
                        setErrorPageView(R.drawable.common_img_norecord, getString(R.string.webview_common_error), null, null);
                    }
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mUrl = url;
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.setVisibility(View.VISIBLE);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (mPbLoading != null) {
                    mPbLoading.setProgress(progress);
                    if (progress == 100) {
                        mPbLoading.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title) && title.toLowerCase().contains(getResources().getString(R.string.webview_http_error))) {
                    view.loadUrl("about:blank");
                    ErrorMsgUtils.showNetErrView(InvitationFragment.this);
                }
            }
        });

        getErrorPageView().setOnErrorFunctionClickListener(new ErrorPageView.OnErrorFunctionClickListener() {
            @Override
            public void onClick() {
                showErrorPage(false);
                mWebView.loadUrl(mUrl);
            }
        });
    }

    /**
     * 设置标题等信息
     */
    public void setTitle() {
        addBackListener();
        MenuBean menuBean = new MenuBean(getString(R.string.invite_for_mine));
        menuBean.setColorId(R.color.white);
        addBar(menuBean);
        setTitleBackground(R.color.blue_00);
        setOnItemBarListener(new TitleBarView.OnItemClickBarListener() {
            @Override
            public void onClick(View v, int postion) {
                startFragment(MyInviteFragment.class);
            }
        });
    }


    @OnClick({R.id.btn_share, R.id.btn_face})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.btn_share:
                mInvitationPresenter.showDialog(getActivity(), mShareContent);

                break;
            case R.id.btn_face:
                startFragment(FaceWebViewFragment.class);
                break;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void setData(InviteShareBean inviteShareBean) {
        mShareContent = inviteShareBean;
    }

}
