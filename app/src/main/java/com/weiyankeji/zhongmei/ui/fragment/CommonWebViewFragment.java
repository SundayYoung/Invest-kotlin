package com.weiyankeji.zhongmei.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.weiyankeji.library.utils.StatusBarUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.customview.ErrorPageView;
import com.weiyankeji.zhongmei.ui.customview.TitleBarView;
import com.weiyankeji.zhongmei.ui.mmodel.bean.MenuBean;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;

import butterknife.BindView;


/**
 * Created by zff on 2017/9/21.
 */

public class CommonWebViewFragment extends BaseFragment {

    //用户反馈
    public static final int TYPE_WEB_BACKFEEK = 1;

    //首页安全保障
    public static final int TYPE_WEB_HOME_SAFE = 1111;

    @BindView(R.id.webview)
    WebView mWebView;
    @BindView(R.id.progressBar)
    ProgressBar mPbLoading;
    String mUrl;


    @Override
    public int setContentLayout() {
        return R.layout.fragment_normal_webview;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        if (bundle != null) {
            initTitle(bundle);
            String url = bundle.getString(ConstantUtils.KEY_URL);
            if (TextUtils.isEmpty(url)) {
                return;
            }
            WebSettings webSettings = mWebView.getSettings();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            webSettings.setJavaScriptEnabled(true);
            webSettings.setBlockNetworkImage(false);
            if (Patterns.WEB_URL.matcher(url).matches()) {
                mWebView.loadUrl(url);
            } else {
                setErrorPageView(R.drawable.common_img_norecord, getString(R.string.webview_common_error), null, null);
            }


            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.startsWith("tel:")) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        return true;
                    }
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (request.getUrl().toString().startsWith("tel:")) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(request.getUrl().toString())));
                            return true;
                        }
                        view.loadUrl(request.getUrl().toString());
                    }
                    return true;
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


                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    view.setVisibility(View.GONE);
                    if (Build.VERSION.SDK_INT >= 23) {

                        if (error.getErrorCode() == WebViewClient.ERROR_HOST_LOOKUP) {
                            ErrorMsgUtils.showNetErrView(CommonWebViewFragment.this);
                        } else {
                            setErrorPageView(R.drawable.common_img_norecord, getString(R.string.webview_common_error), null, null);
                        }
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
                    if (mWebView != null && !TextUtils.isEmpty(title) && title.toLowerCase().contains(getResources().getString(R.string.webview_http_error))) {
                        view.loadUrl("about:blank");
                        mWebView.goBack();
                        ErrorMsgUtils.showNetErrView(CommonWebViewFragment.this);
                        return;
                    }
                    if (TextUtils.isEmpty(mTitle) && !TextUtils.isEmpty(title)) {
                        setTitle(title);
                    }

                }

            });
        }
    }

    private String mTitle;

    private void initTitle(Bundle bundle) {
        mTitle = bundle.getString(ConstantUtils.KEY_TITLE);
        if (!TextUtils.isEmpty(mTitle)) {
            setTitle(mTitle);
        }
        addListener();
        addBackListener();

        int type = bundle.getInt(ConstantUtils.KEY_TYPE);
        if (type == TYPE_WEB_BACKFEEK) {
            MenuBean menuBean = new MenuBean(getString(R.string.feek_back_next));
            menuBean.setColorId(R.color.white);
            addBar(menuBean);
            setTitleBackground(R.color.blue_00);
            setOnItemBarListener(new TitleBarView.OnItemClickBarListener() {
                @Override
                public void onClick(View v, int postion) {
                    startFragment(FeekBackFragment.class);
                }
            });
        } else if (type == TYPE_WEB_HOME_SAFE) {
            setTitleBackground(R.color.blue_33);
            StatusBarUtil.setWindowStatusBarColor(getActivity(), R.color.blue_33, false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.removeAllViews();
            mWebView.destroy();
        }
    }


    public void addListener() {

        mWebView.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {

                    if (i == KeyEvent.KEYCODE_BACK && getCurrentUrl().equals(UrlConfig.H5_HELP)) {
                        getActivity().onBackPressed();
                        return true;
                    }
                    if (i == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                        mWebView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void addBackListener() {
        mTitleBar.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {

                    if (!TextUtils.isEmpty(getCurrentUrl()) && getCurrentUrl().equals(UrlConfig.H5_HELP)) {
                        getActivity().onBackPressed();
                    } else {
                        mWebView.goBack();
                    }
                } else {
                    getActivity().onBackPressed();
                }
            }
        });
    }

    public String getCurrentUrl() {
        return mWebView.copyBackForwardList().getCurrentItem() != null ? mWebView.copyBackForwardList().getCurrentItem().getUrl() : "";
    }
}
