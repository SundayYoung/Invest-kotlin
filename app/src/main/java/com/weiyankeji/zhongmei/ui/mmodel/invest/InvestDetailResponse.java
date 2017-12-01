package com.weiyankeji.zhongmei.ui.mmodel.invest;


import com.weiyankeji.zhongmei.ui.mmodel.bean.InvestDetailBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuhaiyang on 2017/8/21.
 * 投资 项目详情 出参
 */

public class InvestDetailResponse extends InvestDetailBean implements Serializable {
    public String security_name; //安全保障名称
    public String security_logo; //安全保障图片
    public String security_intro; //安全保障介绍（带格式）
    public int is_market_card; //可用卡券类型 1:可用 2:不可用
    public long raise_amount; //递增金额
    public List<InvestProductIntroductionResponse> agreement;
}
