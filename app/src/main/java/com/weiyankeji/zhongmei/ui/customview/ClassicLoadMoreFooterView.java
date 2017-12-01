package com.weiyankeji.zhongmei.ui.customview;
//
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.weiyankeji.library.customview.swipetoloadlayout.SwipeLoadMoreFooterLayout;
import com.weiyankeji.zhongmei.R;

public class ClassicLoadMoreFooterView extends SwipeLoadMoreFooterLayout {
    private TextView mTvLoadMore;
    private ImageView mIvSuccess;
    private ProgressBar mProgressBar;

    private int mFooterHeight;

    public ClassicLoadMoreFooterView(Context context) {
        this(context, null);
    }

    public ClassicLoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassicLoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mFooterHeight = getResources().getDimensionPixelOffset(R.dimen.item_action_height);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTvLoadMore = (TextView) findViewById(R.id.tvLoadMore);
        mIvSuccess = (ImageView) findViewById(R.id.ivSuccess);
        mProgressBar = findViewById(R.id.progressbar);
    }

    @Override
    public void onPrepare() {
        mIvSuccess.setVisibility(GONE);
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            mIvSuccess.setVisibility(GONE);
            mProgressBar.setVisibility(GONE);
//            if (-y >= mFooterHeight) {
//                mTvLoadMore.setText("RELEASE TO LOAD MORE");
//            } else {
//                mTvLoadMore.setText("SWIPE TO LOAD MORE");
//            }
        }
    }

    @Override
    public void onLoadMore() {
//        mTvLoadMore.setText("LOADING MORE");
//        mProgressBar.setVisibility(VISIBLE);
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {
        mProgressBar.setVisibility(GONE);
//        mIvSuccess.setVisibility(VISIBLE);
    }

    @Override
    public void onReset() {
        mIvSuccess.setVisibility(GONE);
    }
}
