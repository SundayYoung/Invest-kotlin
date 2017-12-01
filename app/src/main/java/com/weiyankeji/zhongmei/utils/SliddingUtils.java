package com.weiyankeji.zhongmei.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * @aythor: lilei
 * time: 2017/9/19  上午11:23
 * function:
 * 页面滑动
 */

public class SliddingUtils {

    private Context mContext;

    private GestureDetector mGestureDetector;

    private FlingListener mFilterListener;

    private int mOutLimitX;

    private int mOutLimitY;


    public SliddingUtils() {

    }

    public SliddingUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void init(Context mContext) {
        this.mContext = mContext;
        DisplayMetrics dm = new DisplayMetrics();
        dm = mContext.getResources().getDisplayMetrics();
        int outSize = dm.widthPixels;
        mOutLimitX = outSize / 3;
        mOutLimitY = outSize / 4;
        mGestureDetector = new GestureDetector(mContext,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        if (mFilterListener == null || e1 == null || e2 == null) {
                            return super.onFling(e1, e2, velocityX, velocityY);
                        }
                        if (Math.abs(e2.getRawY() - e1.getRawY()) < mOutLimitY) {
                            if ((e2.getRawX() - e1.getRawX()) > mOutLimitX) {
                                mFilterListener.onLeftTrigger();
                                return true;
                            } else if ((e2.getRawX() - e1.getRawX()) < -mOutLimitX) {
                                mFilterListener.onRightTrigger();
                                return true;
                            }

                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });
    }

    public boolean onTouchEvent(MotionEvent ev) {
        return mGestureDetector.onTouchEvent(ev);
    }

    public void setFlingListener(FlingListener mFilterListener) {
        this.mFilterListener = mFilterListener;
    }

    public interface FlingListener {
        void onLeftTrigger();

        void onRightTrigger();
    }

}
