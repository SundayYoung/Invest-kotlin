package com.weiyankeji.zhongmei.ui.mmodel.mine;

import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;
import com.weiyankeji.zhongmei.utils.ConstantUtils;

/**
 * @aythor: lilei
 * time: 2017/9/10  上午10:33
 * function:
 */

public class MineInvestRecordRequest extends BaseRequest {
    /**
     * type : 投资中、已结束
     * sku_type : 定制、定期、网贷
     */
    public int sku_type;
    public int type;
    public int page;
    public int page_size = ConstantUtils.PAGE_SIZE;

    public MineInvestRecordRequest(int sku_type, int type) {
        this.sku_type = sku_type;
        this.type = type;
        this.page = page;
        this.page_size = page_size;
    }
}
