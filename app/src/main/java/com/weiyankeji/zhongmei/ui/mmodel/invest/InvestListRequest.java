package com.weiyankeji.zhongmei.ui.mmodel.invest;

import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;

/**
 * Created by liuhaiyang on 2017/9/6.
 */

public class InvestListRequest extends BaseRequest {
    public static final String FIELD_RATE = "rate";
    public static final String FIELD_PERIOD = "period";
    public int page;
    public int page_size = 20;
    public String sort_field; //排序字段 rate：收益，period：期限
    public int sort_rule = 1; //1/0倒序 升序 默认1
    public int sku_type; //标的类型
    public String start_id; //上页最后一条sku_id
}
