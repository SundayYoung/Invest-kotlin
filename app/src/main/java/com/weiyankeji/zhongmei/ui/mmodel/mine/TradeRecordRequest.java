package com.weiyankeji.zhongmei.ui.mmodel.mine;

import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;
import com.weiyankeji.zhongmei.utils.ConstantUtils;

/**
 * 交易记录
 * Created by caiwancheng on 2017/9/7.
 */

public class TradeRecordRequest extends BaseRequest {

    public int page;
    public int page_size = ConstantUtils.PAGE_SIZE;
}
