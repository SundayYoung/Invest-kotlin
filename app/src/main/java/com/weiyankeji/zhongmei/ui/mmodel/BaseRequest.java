package com.weiyankeji.zhongmei.ui.mmodel;

import android.util.Log;

import com.weiyankeji.library.BuildConfig;
import com.weiyankeji.zhongmei.utils.UserUtils;

/**
 * Created by liuhaiyang on 2017/9/8.
 */

public class BaseRequest {
    public String token;

    public BaseRequest() {
        if (UserUtils.getInstance().isUserLogin()) {
            token = UserUtils.getInstance().getUserObject().getToken();
            if (BuildConfig.DEBUG) {
                Log.i("HTTP", "token = " + token);
            }
        }
    }

}
