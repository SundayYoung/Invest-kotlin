package com.weiyankeji.zhongmei.ui.mmodel.bean;

import java.io.Serializable;

public class MenuBean implements Serializable {

    private int id = -1;

    private int icon = -1;

    private int colorId;

    private String title;

    private String content;

    private boolean isSelect;

    public MenuBean() {

    }

    public MenuBean(String title) {
        this.title = title;
    }

    public MenuBean(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public MenuBean(String title, String content, boolean isSelect) {
        this.title = title;
        this.content = content;
        this.isSelect = isSelect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

}
