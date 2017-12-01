package com.weiyankeji.zhongmei.utils;

import android.widget.Button;

import com.weiyankeji.zhongmei.R;

/**
 * Created by caiwancheng on 2017/9/30.
 */

public class CountDownBtnUtils {

    /**
     * 不可用按钮样式
     * @param button
     */
    public static void unAvailableState(Button button) {
        button.setTextSize(10);
        button.setEnabled(false);
    }

    /**
     * 可用按钮样式
     * @param button
     */
    public static void availableState(Button button) {
        button.setEnabled(true);
        button.setTextSize(14);
        button.setText(R.string.re_obtain);
    }

}
