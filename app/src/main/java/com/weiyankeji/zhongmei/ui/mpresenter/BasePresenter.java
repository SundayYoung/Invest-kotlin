package com.weiyankeji.zhongmei.ui.mpresenter;

import com.weiyankeji.library.net.client.BaseServerResponse;
import com.weiyankeji.library.net.client.RestApiProvider;
import com.weiyankeji.library.utils.SharedPreferencesUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.api.RestService;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.net.SignInterceptor;

import retrofit2.Call;

/**
 * Created by liuhaiyang on 2017/8/2.
 */

public abstract class BasePresenter<V> {

    public V mView;
    public Call<BaseServerResponse> mCall;
    protected RestService mRestService = (boolean) SharedPreferencesUtil.get(ZMApplication.getZMContext(), ZMApplication.getZMContext().getString(R.string.debug_cache_name), ConstantUtils.DEBUG_SSL, true)
            ? RestApiProvider.getInstance().withInterceptor(new SignInterceptor(ZMApplication.getZMContext()), CommonUtils.getCertificata()).builder().getApiService(RestService.class)
            : RestApiProvider.getInstance().withInterceptor(new SignInterceptor(ZMApplication.getZMContext())).builder().getApiService(RestService.class);

    /**
     * 绑定接口
     * @param view
     */
    public void attach(V view) {
        this.mView = view;
    }

    /**
     * 释放接口
     */
    public void dettach() {
        this.mView = null;
        if (mCall != null) {
            this.mCall.cancel();
        }
    }
}
