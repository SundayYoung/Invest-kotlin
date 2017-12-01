package com.weiyankeji.zhongmei.utils;

import android.os.CountDownTimer;

import com.weiyankeji.zhongmei.ui.mview.CountDownTimerView;

/**
 * Created by caiwancheng on 2017/8/29.
 */

public class CountDownTimerUtils extends CountDownTimer {

    public CountDownTimerView mCountDownTimerView;
    private boolean mIsFinish = true;

    public CountDownTimerUtils(CountDownTimerView countDownTimerView) {
        super(60 * 1000L, 1000);
        this.mCountDownTimerView = countDownTimerView;
    }

    public CountDownTimerUtils(long millisInFuture, long countDownInterval, CountDownTimerView countDownTimerView) {
        super(millisInFuture, countDownInterval);
        this.mCountDownTimerView = countDownTimerView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (mCountDownTimerView != null) {
            mIsFinish = false;
            mCountDownTimerView.schedule(millisUntilFinished / 1000);
        }
    }

    @Override
    public void onFinish() {
        if (mCountDownTimerView != null) {
            mIsFinish = true;
            mCountDownTimerView.countdownFinish();
        }
    }

    /**
     * 是否已经结束倒计时
     *
     * @return
     */
    public boolean isFinish() {
        return mIsFinish;
    }

}
