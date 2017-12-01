package com.weiyankeji.zhongmei.ui.customview;
//
//import android.animation.ObjectAnimator;
//import android.content.Context;
//import android.os.Handler;
//import android.os.Message;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewTreeObserver;
//import android.widget.ScrollView;
//
//import com.bigkoo.convenientbanner.ConvenientBanner;
//import com.wenyankeji.zhongmei.R;
//
///**
// * 模仿淘宝天猫商品详情页面动效
// *
// * @author tomcat
// */
//public class PullPushLayout extends ScrollView {
//
//    public interface OnTouchEventMoveListenre {
//
//        void onSlideUp(int mOriginalHeaderHeight, int mHeaderHeight);
//
//        void onSlideDwon(int mOriginalHeaderHeight, int mHeaderHeight);
//
//        void onSlide(int alpha);
//    }
//
//
//    private OnTouchEventMoveListenre mOnTouchEventMoveListenre;
//
//    public void setOnTouchEventMoveListenre(OnTouchEventMoveListenre l) {
//        mOnTouchEventMoveListenre = l;
//    }
//
//    private int mAlpha = 0;
//    private static final float MAX_ALPHA = 255.00000f;
//
//    private int mHeaderHeight;
//    private int mLastTranslationY;
//    private int mDeltaTranslationY;
//
//    private ConvenientBanner mHeader;
//    private View mContent;
//    private ObjectAnimator mOa;
//    private float mLastY = -1;
//    private float mDeltaY = -1;
//
//    private int mRange;
//    private int mWidth;
//
//    public PullPushLayout(Context context) {
//        super(context);
//    }
//
//    public PullPushLayout(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public PullPushLayout(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//    }
//
//    @Override
//    protected void onFinishInflate() {
//        super.onFinishInflate();
//        setVerticalScrollBarEnabled(false);
//        initView();
//    }
//
//
//    private void initView() {
//        mHeader = (ConvenientBanner) findViewById(R.id.home_convenientBanner);
//        mContent = findViewById(R.id.ll_home_content);
//
//        mHeader.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @SuppressWarnings("deprecation")
//            @Override
//            public void onGlobalLayout() {
//                mHeader.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                mRange = mHeader.getHeight();
//                mWidth = mHeader.getWidth();
//                mHeader.getLayoutParams().height = mRange;
//                mHeaderHeight = mRange;
//            }
//        });
//    }
//
//    @Override
//    public void onWindowFocusChanged(boolean hasWindowFocus) {
//        super.onWindowFocusChanged(hasWindowFocus);
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mLastY = ev.getY();
//                break;
//            default:
//        }
//        return super.onInterceptTouchEvent(ev);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_MOVE:
//                if (getScrollY() != 0) {
//                    mDeltaY = 0;
//                    mLastY = ev.getY();
//                } else {
//                    mDeltaY = ev.getY() - mLastY;
//                    if (mDeltaY > 0) {
//                        //下滑
//                        setT((int) -mDeltaY / 5);
//                        return true;
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                if (getScrollY() < mRange) {
//                    if (mDeltaY != 0) {
//                        reset();
//                    }
//                }
//                break;
//            default:
//        }
//        return super.onTouchEvent(ev);
//    }
//
//    /**
//     * 监听滚动事件的相关逻辑
//     */
//    private void scrollListen(float percent) {
////        mHeaderHeight -= mDeltaTranslationY;
////        if (mOnTouchEventMoveListenre != null) {
////
////            mAlpha = (int) (percent * MAX_ALPHA);
////
////            if (mDeltaTranslationY < 0) {
////                // 下滑
////                mOnTouchEventMoveListenre.onSlideDwon(
////                        mRange, mHeaderHeight);
////            } else if (mDeltaTranslationY > 0) {
////                // 上滑
////                mOnTouchEventMoveListenre.onSlideUp(
////                        mRange, mHeaderHeight);
////            }
////
////            if (mHeaderHeight == mRange) {
////                mAlpha = 0;
////            }
////            if (mAlpha > 255) {
////                mAlpha = 255;
////            }
////            if (mAlpha < 0) {
////                mAlpha = 0;
////            }
////
////            mOnTouchEventMoveListenre.onSlide(mAlpha);
////
////        }
//    }
//
//
//    @Override
//    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//        super.onScrollChanged(l, t, oldl, oldt);
////        float percent = animateScroll(t);
////
//        animateUpSlide(t);
////        mDeltaTranslationY = t - mLastTranslationY;
////        mLastTranslationY = t;
////        scrollListen(percent);
//    }
//
//
//    public void setT(int t) {
//        scrollTo(0, t);
//        if (t <= 0) {
//            animatePull(t);
//        }
//    }
//
//    private void animatePull(int t) {
//        Message msg = mAnimatePullHandler.obtainMessage();
//        msg.arg1 = t;
//        mAnimatePullHandler.sendMessage(msg);
//    }
//
//    private Handler mAnimatePullHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            int t = msg.arg1;
//            mHeader.getLayoutParams().height = mRange - t;
//            mHeader.getLayoutParams().width = mWidth - t * 3;
//            mHeader.requestLayout();
//        }
//    };
//
//
//    private void animateUpSlide(int t) {
//        if (t >= 0) {
//            Message msg = mAnimateUpSlideHandler.obtainMessage();
//            msg.arg1 = t;
//            mAnimateUpSlideHandler.sendMessage(msg);
//        }
//    }
//
//    private Handler mAnimateUpSlideHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            int t = msg.arg1;
//            mHeader.scrollTo(0, (int) (-t * 0.5));
//        }
//    };
//
//    /**
//     * 重置高
//     */
//    private void reset() {
//        if (mOa != null && mOa.isRunning()) {
//            return;
//        }
//        mOa = ObjectAnimator.ofInt(this, "t", (int) -mDeltaY / 5, 0);
//        mOa.setDuration(150);
//        mOa.start();
//    }
//}
