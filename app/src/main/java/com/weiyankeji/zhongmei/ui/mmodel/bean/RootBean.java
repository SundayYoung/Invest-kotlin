package com.weiyankeji.zhongmei.ui.mmodel.bean;

import com.google.gson.JsonElement;

import java.io.Serializable;

/**
 * Created by caiwancheng on 2017/7/25.
 */

public class RootBean implements Serializable{
    private String code;
    private String msg;
    private JsonElement data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }
}
