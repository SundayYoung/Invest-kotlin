package com.weiyankeji.zhongmei.ui.customview;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by liuhaiyang on 2017/8/30.
 * 系统的滚动监听只能api23以上用，这为了兼容
 */

public class CustomNestedScrollView extends NestedScrollView {

    private ScrollInterface mScrollInterface;

    /**
     * 定义滑动接口
     */
    public interface ScrollInterface {
        void onScrollChange(int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }

    public CustomNestedScrollView(Context context) {
        super(context);
    }

    public CustomNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (mScrollInterface != null) {
            mScrollInterface.onScrollChange(l, t, oldl, oldt);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void setOnMyScrollChangeListener(ScrollInterface t) {
        this.mScrollInterface = t;
    }
}
