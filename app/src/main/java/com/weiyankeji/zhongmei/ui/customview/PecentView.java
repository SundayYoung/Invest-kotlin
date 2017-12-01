package com.weiyankeji.zhongmei.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.mmodel.CircularPecent;

import java.util.ArrayList;

/**
 * Created by lilei on 2017/8/1.
 */
public class PecentView extends View {
    int mHeight;         //高度
    int mWidth;          //宽度
    Paint mPaint;        //画笔
    float mMaxRadius = 60;    //大圆半径
    float mMinRadius = 30;    //小圆半径
    int mTextColor = Color.BLACK;      //文字颜色
    int mPecentColor = Color.BLACK;    //百分比的颜色
    float mTextSize = 12;     //文字颜色
    float mStartAngel = -90;  //扇形开始角度
    float mSax360 = 360;
    //存放数据（"可用余额"，金钱）
    ArrayList<CircularPecent> mDataList;
    //金钱总额
    float mMaxData = 0;
    Context mContext;

    //初始化画笔以及属性文件
    private void init(Context context, @Nullable AttributeSet attrs) {
        this.mContext = context;
        mPaint = new Paint();
        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PecentView);
        mMaxRadius = typedArray.getDimension(R.styleable.PecentView_maxRadius, mMaxRadius);
        mMinRadius = typedArray.getDimension(R.styleable.PecentView_minRadius, mMinRadius);
        mTextColor = typedArray.getColor(R.styleable.PecentView_textColor, mTextColor);
        mPecentColor = typedArray.getColor(R.styleable.PecentView_pecentColor, mPecentColor);
        mTextSize = typedArray.getDimension(R.styleable.PecentView_textSize, mTextSize);
        mPaint.setColor(mPecentColor);
    }

    //设置数据,最好每次设置数据的时候就重绘
    public void setData(ArrayList<CircularPecent> list) {
        mDataList = list;
        invalidate();
    }

    public PecentView(Context context) {
        super(context);
        init(context, null);
    }

    public PecentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PecentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mHeight = h;
        this.mWidth = w;
    }

    //整个都由自己画出
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(mWidth / 2, mHeight / 2);
        RectF rectF = new RectF(-mMaxRadius, -mMaxRadius, mMaxRadius, mMaxRadius);
        CircularPecent myPecent;
        canvas.drawCircle(0, 0, mMaxRadius, mPaint);
        if (mDataList != null) {
            for (int i = 0; i < mDataList.size(); i++) {
                myPecent = mDataList.get(i);
                mPaint.setColor(myPecent.getPecentColor());
                canvas.drawArc(rectF, mStartAngel, myPecent.getPecent() * mSax360, true, mPaint);
                mStartAngel = mStartAngel + myPecent.getPecent() * mSax360;
            }
        }
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(0, 0, mMinRadius, mPaint);

    }
}