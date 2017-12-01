package com.weiyankeji.zhongmei.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.weiyankeji.library.utils.SharedPreferencesUtil;
import com.weiyankeji.library.utils.StringUtil;
import com.weiyankeji.zhongmei.BuildConfig;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.customview.TitleBarView;
import com.weiyankeji.zhongmei.ui.mmodel.bean.MenuBean;
import com.weiyankeji.zhongmei.ui.mpresenter.AboutUsPresenter;
import com.weiyankeji.zhongmei.ui.mview.BaseView;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.ConstantUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @aythor: lilei
 * time: 2017/9/26  下午1:52
 * function:
 */

public class AboutUsFragment extends BaseMvpFragment<BaseView, AboutUsPresenter> implements BaseView {
    @BindView(R.id.tv_version_name)
    TextView mTvVersionName;

    @Override
    public AboutUsPresenter initPresenter() {
        return new AboutUsPresenter();
    }

    @Override
    public int setContentLayout() {
        return R.layout.fragment_aboutus;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        setTitle(R.string.about_us);
        addBackListener();
        mTvVersionName.setText(getString(R.string.version, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));

        settingDebug();

    }

    @OnClick({R.id.rl_learn_more, R.id.rl_check_version})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_learn_more:
                CommonUtils.intentWebView(this, null, UrlConfig.H5_ABOUT);
                break;
            case R.id.rl_check_version:
                mPresenter.checkUpdate(this);
                break;
            default:
                break;
        }
    }

    @Override
    public void showLoading() {
        setLoading(true);
    }

    @Override
    public void hideLoading() {
        setLoading(false);
    }


    private void settingDebug() {
        if (BuildConfig.DEBUG) {
            MenuBean menuBean = new MenuBean(getString(R.string.common_setting));
            menuBean.setColorId(R.color.white);
            addBar(menuBean);
            setOnItemBarListener(new TitleBarView.OnItemClickBarListener() {
                @Override
                public void onClick(View v, int p) {
                    //get share value
                    String urlType = (String) SharedPreferencesUtil.get(mContext, getString(R.string.debug_cache_name), ConstantUtils.DEBUG_URL, "");
                    boolean sslType = (boolean) SharedPreferencesUtil.get(mContext, getString(R.string.debug_cache_name), ConstantUtils.DEBUG_SSL, true);

                    final Dialog dialog = new Dialog(mContext, R.style.BottomDialog);
                    View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_setting, null);

                    final Switch release = contentView.findViewById(R.id.st_release);
                    final Switch preRelease = contentView.findViewById(R.id.st_pre_release);
                    Switch ssl = contentView.findViewById(R.id.st_ssl);

                    if (!StringUtil.isNullOrEmpty(urlType)) {
                        release.setChecked(urlType.equals(getString(R.string.debug_release)));
                        preRelease.setChecked(urlType.equals(getString(R.string.debug_pre_release)));
                    }
                    ssl.setChecked(sslType);

                    //release
                    release.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                if (preRelease.isChecked()) {
                                    preRelease.setChecked(false);
                                }
                                SharedPreferencesUtil.put(mContext, getString(R.string.debug_cache_name), ConstantUtils.DEBUG_URL, getString(R.string.debug_release));
                            } else {
                                SharedPreferencesUtil.remove(mContext, getString(R.string.debug_cache_name), ConstantUtils.DEBUG_URL);
                            }
                        }
                    });
                    //pre-release
                    preRelease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                if (release.isChecked()) {
                                    release.setChecked(false);
                                }
                                SharedPreferencesUtil.put(mContext, getString(R.string.debug_cache_name), ConstantUtils.DEBUG_URL, getString(R.string.debug_pre_release));
                            } else {
                                SharedPreferencesUtil.remove(mContext, getString(R.string.debug_cache_name), ConstantUtils.DEBUG_URL);
                            }
                        }
                    });
                    //ssl
                    ssl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            SharedPreferencesUtil.put(mContext, getString(R.string.debug_cache_name), ConstantUtils.DEBUG_SSL, isChecked);
                        }
                    });

                    dialog.setContentView(contentView);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }
            });
        }
    }

}
