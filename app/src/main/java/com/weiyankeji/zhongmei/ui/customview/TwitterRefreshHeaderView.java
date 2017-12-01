package com.weiyankeji.zhongmei.ui.customview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiyankeji.library.customview.swipetoloadlayout.SwipeRefreshHeaderLayout;
import com.weiyankeji.zhongmei.R;


public class TwitterRefreshHeaderView extends SwipeRefreshHeaderLayout {
    /**
     * 拖动的最大偏移
     */
    public static final int REFRESH_MAX_DRAG_OFFSET = 300;

    private static final String TAG = "TwitterRefreshHeaderView";
    private Context mContext;

    private ImageView mIvArrow;

    private TextView mTvRefresh;

    /**
     * 最大下拉高度
     */
    private int mMaxDragOffset;
    private Handler mHandler;

    private static final int MSG_WHAT_REFRESHING = 1;
    private static final int MSG_WHAT_DRAGING = 2;

    private int[] mRefreshImages = {
            R.drawable.refresh_18, R.drawable.refresh_19, R.drawable.refresh_20, R.drawable.refresh_21,
            R.drawable.refresh_22, R.drawable.refresh_23, R.drawable.refresh_24, R.drawable.refresh_25,
            R.drawable.refresh_26, R.drawable.refresh_27, R.drawable.refresh_28, R.drawable.refresh_29,
            R.drawable.refresh_30, R.drawable.refresh_31, R.drawable.refresh_32, R.drawable.refresh_33};

    private int[] mDragImages = {
            R.drawable.refresh_1, R.drawable.refresh_2, R.drawable.refresh_3, R.drawable.refresh_4,
            R.drawable.refresh_5, R.drawable.refresh_6, R.drawable.refresh_7, R.drawable.refresh_8,
            R.drawable.refresh_9, R.drawable.refresh_10, R.drawable.refresh_11, R.drawable.refresh_12,
            R.drawable.refresh_13, R.drawable.refresh_14, R.drawable.refresh_15, R.drawable.refresh_16,
            R.drawable.refresh_17, R.drawable.refresh_18};

    private int mIndexRefresh;

    private int mIndexDrag;

    public TwitterRefreshHeaderView(Context context) {
        this(context, null);
    }

    public TwitterRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TwitterRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mMaxDragOffset = REFRESH_MAX_DRAG_OFFSET;
        initHandler();
//        mRotateUp = AnimationUtils.loadAnimation(context, R.anim.rotate_up);
        // mRotateDown = AnimationUtils.loadAnimation(context, R.anim.rotate_down);

    }

    private void initHandler() {
        mHandler = new Handler(mContext.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_WHAT_REFRESHING:
                        handleRefresh();
                        break;
                    case MSG_WHAT_DRAGING:
                        handleDrag(msg);
                        break;
                }
            }
        };
    }

    private void handleRefresh() {
        int index = mIndexRefresh++ % mRefreshImages.length;
       // LogUtils.i(TAG, "refresh index=" + index);
        mIvArrow.setImageResource(mRefreshImages[index]);
        mHandler.sendEmptyMessageDelayed(MSG_WHAT_REFRESHING, 200);
    }

    private void handleDrag(Message message) {
        int index = message.arg1;
     //   LogUtils.i(TAG, "drag index=" + index);
        mIvArrow.setImageResource(mDragImages[index]);
        mIndexDrag = index;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTvRefresh = (TextView) findViewById(R.id.tvRefresh);
        mIvArrow = (ImageView) findViewById(R.id.ivArrow);
    }

    @Override
    public void onRefresh() {
     //   LogUtils.i(TAG, "onRefresh");
        mTvRefresh.setText(R.string.refresh_header_on_refresh);
        startShowRefresh();
    }

    @Override
    public void onPrepare() {
     //   LogUtils.i(TAG, "onPrepare");
        mTvRefresh.setText(R.string.refresh_header_on_prepare);
        mIvArrow.setImageResource(R.drawable.refresh_1);
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        mIvArrow.setVisibility(VISIBLE);
        if (!isComplete) {
            // 计算y值，计算加载的图片
            int index;
            if (y >= mMaxDragOffset) {
                index = mDragImages.length - 1;
            } else {
                index = y / (mMaxDragOffset / mDragImages.length);
            }
            if (index >= mDragImages.length) {
                index = mDragImages.length - 1;
            }
            if (index != mIndexDrag) {
                Message message = Message.obtain();
                message.what = MSG_WHAT_DRAGING;
                message.arg1 = index;
                mHandler.sendMessage(message);
            }
        }
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {
        mTvRefresh.setText(R.string.refresh_header_on_complete);
        stopShowRefresh();
        mIvArrow.setImageResource(R.drawable.refresh_33);
    }

    private void startShowRefresh() {
        mHandler.sendEmptyMessage(MSG_WHAT_REFRESHING);
    }

    private void stopShowRefresh() {
        mHandler.removeMessages(MSG_WHAT_REFRESHING);
    }

    @Override
    public void onReset() {
        stopShowRefresh();
    }

}
