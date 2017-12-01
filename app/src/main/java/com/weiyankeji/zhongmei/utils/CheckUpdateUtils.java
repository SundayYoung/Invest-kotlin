package com.weiyankeji.zhongmei.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.weiyankeji.library.utils.DialogUtil;
import com.weiyankeji.library.utils.SharedPreferencesUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.fragment.BaseFragment;
import com.weiyankeji.zhongmei.ui.mmodel.multi.CheckUpdateResponse;

/**
 * Created by caiwancheng on 2017/9/28.
 */

public class CheckUpdateUtils {

    public static final void showUpdateDialog(final BaseFragment fragment, final CheckUpdateResponse response) {
        showUpdateDialog(fragment, response, false);
    }

    /**
     * 显示升级对话框
     *
     * @param fragment
     * @param response
     */
    public static final void showUpdateDialog(final BaseFragment fragment, final CheckUpdateResponse response, final boolean isCheckUpdate) {
        if (response == null || TextUtils.isEmpty(response.apkUrl)) {
            return;
        }

        if (response.forceUpgrade == ConstantUtils.FORCE_UPGRADE_TRUE) {
            Dialog dialog = DialogUtil.createHintDialog(fragment.mContext, response.versionDesc, ZMApplication.getZMApplication().getString(R.string.common_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    dialogInterface.cancel();
                    intentBrowser(fragment, response.apkUrl);
                    System.exit(0);
                }
            });

            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                        return true;
                    }
                    return false;
                }
            });
        } else {
            DialogUtil.createDialog(fragment.mContext, null, response.versionDesc, ZMApplication.getZMApplication().getString(R.string.common_ok), ZMApplication.getZMApplication().getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    dialogInterface.cancel();
                    intentBrowser(fragment, response.apkUrl);
                    if (isCheckUpdate) {
                        saveUpdateConfig();
                    }
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    dialogInterface.cancel();
                    if (isCheckUpdate) {
                        saveUpdateConfig();
                    }
                }
            }).show();
        }
    }

    /**
     * 检测本地卡是否可更新
     *
     * @return
     */
    public static boolean checkUpdateConfig() {
        SharedPreferences preferences = SharedPreferencesUtil.getSharedPreferences(ZMApplication.getZMApplication(), ConstantUtils.SHAREDPREFERENCES_NAME_CONFIG);
        long cacheTime = preferences.getLong(ConstantUtils.SP_VERSION_CHECK, 0);
        if (System.currentTimeMillis() - cacheTime > 24 * 60 * 60 * 1000 * 3) { //3天
            return true;
        }
        return false;
    }

    /**
     * 保存
     */
    public static void saveUpdateConfig() {
        SharedPreferencesUtil.put(ZMApplication.getZMApplication(), ConstantUtils.SHAREDPREFERENCES_NAME_CONFIG, ConstantUtils.SP_VERSION_CHECK, System.currentTimeMillis());
    }


    //跳浏览器
    public static void intentBrowser(BaseFragment fragment, String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        fragment.startActivity(intent);
    }

}
