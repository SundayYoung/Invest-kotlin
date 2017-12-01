package com.weiyankeji.zhongmei.ui.mmodel;

/**
 * Created by lilei on 2017/8/1.
 * 用于绘制比例图类
 */


public class CircularPecent {


    int pecentColor;
    float pecent;

    public CircularPecent(int pecentColor, float pecent) {
        this.pecentColor = pecentColor;
        this.pecent = pecent;
    }


    public int getPecentColor() {
        return pecentColor;
    }

    public void setPecentColor(int pecentColor) {
        this.pecentColor = pecentColor;
    }

    public float getPecent() {
        return pecent;
    }

    public void setPecent(float pecent) {
        this.pecent = pecent;
    }


}
