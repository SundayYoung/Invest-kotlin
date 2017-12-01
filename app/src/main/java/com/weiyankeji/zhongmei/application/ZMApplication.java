package com.weiyankeji.zhongmei.application;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatService;
import com.weiyankeji.library.log.LogUtils;
import com.weiyankeji.zhongmei.utils.ConstantUtils;

import static com.weiyankeji.zhongmei.utils.ConstantUtils.MTA_APP_KEY;

/**
 * @aythor: lilei
 * time: 2017/8/15  下午2:45
 * function:
 */

public class ZMApplication extends Application {

    private static Context sContext;
    private static ZMApplication sApplication;

    public static IWXAPI sWXApi;

    /**
     * 单利获取application
     *
     * @return
     */
    public static ZMApplication getZMApplication() {
        if (sApplication == null) {
            sApplication = new ZMApplication();
        }
        return sApplication;
    }

    /**
     * 全局context
     *
     * @return
     */
    public static Context getZMContext() {
        if (sContext == null) {
            sContext = sApplication.getApplicationContext();
        }
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        sContext = getApplicationContext();
        initWXApi();

        //Bugly init debug:ture relese:false
        CrashReport.initCrashReport(getApplicationContext(), ConstantUtils.BUGLY_APP_ID, false);

        try {
            // 初始化并启动MTA
            StatService.startStatService(getApplicationContext(), MTA_APP_KEY, com.tencent.stat.common.StatConstants.VERSION);
        } catch (MtaSDkException e) {
            // MTA初始化失败
            LogUtils.d("MTA", "MTA初始化失败" + e);
        }

    }

    /**
     * 接入微信功能初始化
     */
    private void initWXApi() {
        sWXApi = WXAPIFactory.createWXAPI(this, ConstantUtils.WX_APP_ID, true);
        sWXApi.registerApp(ConstantUtils.WX_APP_ID);
    }
}
