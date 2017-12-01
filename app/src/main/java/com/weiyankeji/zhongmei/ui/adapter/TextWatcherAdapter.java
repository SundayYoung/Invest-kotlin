package com.weiyankeji.zhongmei.ui.adapter;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * TextWatcher抽象类
 * 调用时不必重写所有方法
 * Created by liuhaiyang on 2017/9/4.
 */

public abstract class TextWatcherAdapter implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
