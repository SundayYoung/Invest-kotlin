package com.weiyankeji.zhongmei.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.weiyankeji.zhongmei.R;


/**
 * Splash Activity
 * Created by zff on 2017/9/8.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected int setContentLayout() {
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_splash;
    }

    @Override
    protected void initData(Bundle bundle) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                jumpToMainActivity();
            }
        }, 1500);
    }


    private void jumpToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        finish();
    }

}
