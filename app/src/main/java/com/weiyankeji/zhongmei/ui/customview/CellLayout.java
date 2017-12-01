package com.weiyankeji.zhongmei.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class CellLayout extends ViewGroup {
    /**
     * 统一高度
     **/
    private int mHeight = 0;
    private int mWidth = 0;

    /**
     * 默认间距
     **/
    private int mSpacing = 4;

    private int mSpacingSum;

    private int mCol = 3;
    private int mColSum = mCol;
    private int mRow;

    private int mHarentWidth;
    private int mParentHeight;

    private boolean mIsSquare = true;

    private OnItemClickListener mOnItemClick;
    private OnItemLongClickListener mInItemLongClick;

    public CellLayout(Context context) {
        super(context);
        initView();
    }

    public CellLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CellLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();

    }

    private void initView() {
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mSpacingSum = mSpacing * (mColSum - 1);
        mWidth = (getMeasuredWidth() - mSpacingSum) / mColSum;

        int size = getChildCount();
        for (int i = 0; i < size; i++) {
            final View child = getChildAt(i);
            if (!mIsSquare) {
                if (mHeight <= 0) {
                    child.measure(MEASURED_STATE_MASK, MEASURED_STATE_MASK);
                    mHeight = child.getMeasuredHeight();
                }
            } else {
                mHeight = mWidth;
            }
            measureContentView(child, mWidth, mHeight); //每个ChildView 的宽高
        }

        mHarentWidth = getMeasuredWidth();

        mRow = size / mCol + (size % mCol > 0 ? 1 : 0);
        mParentHeight = mRow * mHeight + mSpacing * (mRow - 1);
        if (mParentHeight < 0) {
            mParentHeight = 0;
        }
        setMeasuredDimension(mHarentWidth, mParentHeight);

    }

    private void measureContentView(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        final MarginLayoutParams lp = new MarginLayoutParams(parentWidthMeasureSpec, parentHeightMeasureSpec);

        //写入View 的宽高
        lp.width = parentWidthMeasureSpec;
        lp.height = parentHeightMeasureSpec;

        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin, lp.width);
        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, getPaddingTop() + getPaddingBottom() + lp.topMargin, lp.height);

        //测定View的预定范围
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean arg0, int left, int top, int right, int bottom) {
        try {
            int lengthX = 0; // right position of child relative to parent
            int lengthY = 0; // bottom position of child relative to parent

            for (int i = 0; i < mRow; i++) {
                lengthY = i * mHeight + i * mSpacing;
                for (int j = 0; j < mCol; j++) {
                    lengthX = j * mWidth + j * mSpacing;

                    final int position = i * mCol + j;
                    final View child = getChildAt(position);
                    if (child == null) {
                        return;
                    }

                    child.layout(lengthX, lengthY, lengthX + mWidth, lengthY + mHeight);

                    child.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnItemClick != null) {
                                mOnItemClick.onItemClick(null, v, position, v.getId());
                            }
                        }
                    });
                    child.setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if (mInItemLongClick != null) {
                                mInItemLongClick.onItemLongClick(null, child, position, position);
                            }
                            return false;
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /**
     * 间距设置
     *
     * @param mSpacing
     */
    public void setSpacing(int mSpacing) {
        this.mSpacing = mSpacing;
    }

    /**
     * 间距设置
     *
     * @param resId 资源ID
     */
    public void setSpacingForRes(int resId) {
        setSpacing((int) this.getResources().getDimension(resId));
    }

    /**
     * 设置是否为正方形 默认为true，不想要正方形的时候请传入false
     *
     * @param isSquare
     */
    public void setIsSquare(boolean isSquare) {
        this.mIsSquare = isSquare;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public void setCol(int col) {
        this.mCol = col;
    }

    public void setColSum(int colSum) {
        this.mColSum = colSum;
        setCol(colSum);
    }

    public int getViewHeight() {
        return mHeight;
    }

    public int getViewWidth() {
        return mWidth;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClick) {
        this.mOnItemClick = onItemClick;
    }

    public void setOnLongItemClickListener(OnItemLongClickListener onItemLongClick) {
        this.mInItemLongClick = onItemLongClick;
    }

}