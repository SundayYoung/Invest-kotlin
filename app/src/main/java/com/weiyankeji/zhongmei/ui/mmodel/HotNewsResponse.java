package com.weiyankeji.zhongmei.ui.mmodel;

/**
 * Created by zff on 2017/9/7.
 */

public class HotNewsResponse {
    public int id;
    public String title;
    public String app_img_url; //APP图片
    public String pc_img_url; //pc图片
    public String content;
    public String  is_share; //是否分享 0否1
    public String share_title;
    public String share_content;
    public String share_img_url;
    public long created_at;
    public String link_url; //详情URL
}
