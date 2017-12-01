package com.weiyankeji.zhongmei.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.weiyankeji.zhongmei.R;

/**
 * Created by caiwancheng on 2017/7/28.
 */

public class CostomDecoration extends RecyclerView.ItemDecoration {

    /**
     * 水平方向
     */
    public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;

    /**
     * 垂直方向
     */
    public static final int VERTICAL = LinearLayoutManager.VERTICAL;

    // 画笔
    private Paint mPaint;

    // 布局方向
    private int mOrientation;
    // 分割线颜色
    private int mColor;
    // 分割线尺寸
    private int mSize;

    private Context mContext;

    private int mMarginLeft;

    private int mMarginRight;

    public boolean mIsClearLastLine = true;

    public CostomDecoration(Context context) {
        this(context, VERTICAL, 0, 0);
    }

    /**
     * 请传参数资源ID
     *
     * @param context
     * @param marginLeft  demin
     * @param marginRight demin
     */
    public CostomDecoration(Context context, @DimenRes int marginLeft, @DimenRes int marginRight) {
        this(context, VERTICAL, marginLeft, marginRight);
    }


    public CostomDecoration(Context context, int orientation, @DimenRes int marginLeft, @DimenRes int marginRight) {
        this.mOrientation = orientation;
        mContext = context;

        if (marginLeft > 0) {
            mMarginLeft = mContext.getResources().getDimensionPixelSize(marginLeft);
        }

        if (marginRight > 0) {
            mMarginRight = mContext.getResources().getDimensionPixelSize(marginRight);
        }

        mSize = mContext.getResources().getDimensionPixelSize(R.dimen.list_line_height);
        mColor = ContextCompat.getColor(mContext, R.color.line_color);
        mPaint = new Paint();
        mPaint.setColor(mColor);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        if (mOrientation == VERTICAL) {
            drawHorizontal(c, parent);
        } else {
            drawVertical(c, parent);
        }
    }

    /**
     * 设置分割线颜色
     *
     * @param color 颜色
     */
    public void setColor(int color) {
        this.mColor = color;
        mPaint.setColor(mColor);
    }

    /**
     * 设置分割线尺寸
     *
     * @param size 尺寸
     */
    public void setSize(int size) {
        this.mSize = size;
    }


    /**
     * 清楚最后一根线
     *
     * @param bool
     */
    public void clearLastLine(boolean bool) {
        mIsClearLastLine = bool;
    }

    // 绘制垂直分割线
    protected void drawVertical(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mSize;

            c.drawRect(left, top, right, bottom, mPaint);
        }
    }

    // 绘制水平分割线
    protected void drawHorizontal(Canvas c, RecyclerView parent) {
        final int left = mMarginLeft;
        final int right = parent.getWidth() - mMarginRight;

        int childCount = parent.getChildCount();
        if (childCount > 0 && mIsClearLastLine) {
            childCount = childCount - 1;
        }

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mSize;
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }
}
