package com.weiyankeji.zhongmei.utils;

import android.text.TextUtils;

import com.tencent.stat.StatService;
import com.weiyankeji.zhongmei.application.ZMApplication;

import java.util.Properties;

/**
 * 统计工具类
 * Created by caiwancheng on 2017/9/28.
 */

public class StatisticalUtils {

    /**
     * 页面统计
     */
    public static final String LAB_ACCESS_PAGE = "access_page";


    /**
     * 安全保障点击
     */
    public static final String LAB_SAFE = "click_safe";

    /**
     * 定期列表
     */
    public static final String LABLE_REGULAR_MORE = "regular_more";


    /**
     * 新闻列表点击
     */
    public static final String LABLE_NEWS = "hot_news";

    /**
     * 精选理财列表点击
     */
    public static final String LABLE_SPECIAL_INVEST = "special_invest";

    /**
     * 理财首页定期
     */
    public static final String LABLE_INVEST_REGULAR = "invest_regular";


    /**
     * 充值
     */
    public static final String LABLE_CLICK_CHARGE = "click_charge";

    /**
     * 提现
     */
    public static final String LABLE_CLICK_DRAWCASH = "click_drawcash";


    /**
     * 点击位置
     */
    private static final String KEY_INDEX = "index";

    /**
     * 点击位置
     */
    public static final String KEY_NAME = "name";


    /**
     * 埋点
     *
     * @param lable
     */
    public static void trackCustomKVEvent(String lable) {
        StatService.trackCustomKVEvent(ZMApplication.getZMApplication(), lable, new Properties());
    }


    /**
     * 埋点
     *
     * @param lable
     * @param key
     * @param value
     */
    public static void trackCustomKVEve(String lable, String key, String value) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            trackCustomKVEvent(lable);
            return;
        }
        Properties prop = new Properties();
        prop.setProperty(key, value);
        trackCustomKVEve(lable, prop);
    }


    /**
     * 埋点
     * @param lable
     * @param properties
     */
    public static void trackCustomKVEve(String lable, Properties properties) {
        if (properties != null) {
            StatService.trackCustomKVEvent(ZMApplication.getZMApplication(), lable, properties);
        }
    }


    /**
     * 埋点点击次数统计
     *
     * @param lable
     * @param index
     */
    public static void trackCustomKVEvent(String lable, int index) {

        Properties prop = new Properties();
        if (index != -1) {
            prop.setProperty(StatisticalUtils.KEY_INDEX, index + "");
        }
        StatService.trackCustomKVEvent(ZMApplication.getZMApplication(), lable, prop);
    }

}
