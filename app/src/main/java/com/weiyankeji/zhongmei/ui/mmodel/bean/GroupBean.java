package com.weiyankeji.zhongmei.ui.mmodel.bean;

import java.util.List;

/**
 * Created by caiwancheng on 2017/8/24.
 */

public class GroupBean {
    /**
     * title : string,定制理财
     * sku_type : 定制
     * list : [{"sku_id":1,"type":1,"sku_amount":0,"sku_rate":0,"sku_period":0,"sku_period_type":"天","repay_type":"一次性还本付息","sku_status":"string,标的状态","sku_tags":[{"color":"string,标签颜色","title":"string,标签名"}]}]
     */

    public String title;
    public int type;
    public List<InvestDetailBean> list;
}
