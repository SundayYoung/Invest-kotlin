package com.weiyankeji.zhongmei.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.weiyankeji.zhongmei.R;

/**
 * Created by caiwancheng on 2017/7/28.
 */

public class RecyclerViewDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;

    private Paint mDividerPaint;

    private int mLineHeight;
    private int mMarginLeft;
    private int mMarginRight;

    public RecyclerViewDecoration(Context context) {
        mContext = context;
        mMarginLeft = mContext.getResources().getDimensionPixelOffset(R.dimen.margin_big);
        mLineHeight = mContext.getResources().getDimensionPixelSize(R.dimen.list_line_height);
        mDividerPaint = new Paint();
        mDividerPaint.setColor(ContextCompat.getColor(context, R.color.line_color));
    }

    /**
     * 设置左右间距
     *
     * @param left
     * @param right
     */
    public RecyclerViewDecoration(Context context, int left, int right) {
        this.mMarginLeft = left;
        this.mMarginRight = right;
        mContext = context;
        mLineHeight = mContext.getResources().getDimensionPixelSize(R.dimen.list_line_height);
        mDividerPaint = new Paint();
        mDividerPaint.setColor(ContextCompat.getColor(context, R.color.line_color));
    }

    /**
     * 设置左右间距
     * 设置颜色
     * 设置线的高度
     *
     * @param left
     * @param right
     * @param color
     * @param height
     */
    public RecyclerViewDecoration(Context context, int left, int right, int color, int height) {
        this.mMarginLeft = left;
        this.mMarginRight = right;
        mContext = context;
        mLineHeight = mContext.getResources().getDimensionPixelSize(height);
        mDividerPaint = new Paint();
        mDividerPaint.setColor(ContextCompat.getColor(context, color));
    }

    /**
     * @param outRect 边界
     * @param view    recyclerView ItemView
     * @param parent  recyclerView
     * @param state   recycler 内部数据管理
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = mLineHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int left = mMarginLeft;
        int right = parent.getWidth() - mMarginRight;

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + mLineHeight;
            c.drawRect(left, top, right, bottom, mDividerPaint);
        }
    }


}