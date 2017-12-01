package com.weiyankeji.zhongmei.ui.mview;

/**
 * 倒计时mview
 * Created by caiwancheng on 2017/8/29.
 */

public interface CountDownTimerView {

    /**
     * 倒计时进度
     * @param time
     */
    void schedule(long time);

    /**
     * 倒计时结束
     */
    void countdownFinish();

}
