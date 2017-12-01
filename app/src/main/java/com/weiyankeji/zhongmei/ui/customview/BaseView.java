package com.weiyankeji.zhongmei.ui.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by caiwancheng on 2017/7/21.
 */

public class BaseView extends View {

    private Unbinder mUnbinder;

    public BaseView(Context context) {
        super(context);
        initData();
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    /**
     * 初始化公共的数据
     */
    private void initData() {
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}
