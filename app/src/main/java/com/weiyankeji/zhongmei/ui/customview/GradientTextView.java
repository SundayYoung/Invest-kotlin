package com.weiyankeji.zhongmei.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.weiyankeji.zhongmei.R;

/**
 * 颜色渐变
 * Created by caiwancheng on 2017/9/1.
 */

public class GradientTextView extends TextView {
    private LinearGradient mLinearGradient;
    private Paint mPaint;
    private int mViewWidth = 0;
    private Rect mTextBound = new Rect();
    private boolean mIsGradient = true;
    private int mStartColor;
    private int mEndColor;

    public GradientTextView(Context context) {
        super(context);
    }

    public GradientTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GradientTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.gradient_textview);
        mIsGradient = ta.getBoolean(R.styleable.gradient_textview_isGradient, false);
        mStartColor = ta.getColor(R.styleable.gradient_textview_startColor, Color.WHITE);
        mEndColor = ta.getColor(R.styleable.gradient_textview_endColor, Color.WHITE);
        ta.recycle();
    }

    /**
     * 设置渐变颜色
     *
     * @param startColor
     * @param endColor
     */
    public void setGradientColors(int startColor, int endColor) {
        mStartColor = startColor;
        mEndColor = endColor;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mEndColor == mStartColor || !mIsGradient) {
            super.onDraw(canvas);
            return;
        }
        mViewWidth = getMeasuredWidth();
        mPaint = getPaint();

        String mTipText = getText().toString();
        mPaint.getTextBounds(mTipText, 0, mTipText.length(), mTextBound);
        mLinearGradient = new LinearGradient(0, 0, mViewWidth, 0,
                new int[]{mStartColor, mEndColor},
                null, Shader.TileMode.REPEAT);
        mPaint.setShader(mLinearGradient);
        canvas.drawText(mTipText, getMeasuredWidth() / 2 - mTextBound.width() / 2, getMeasuredHeight() / 2 + mTextBound.height() / 2, mPaint);
    }
}
