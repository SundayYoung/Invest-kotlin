package com.weiyankeji.zhongmei.ui.mmodel.bonus;

import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;

/**
 * Created by zff on 2017/9/26.
 */

public class BonusDaysRequest extends BaseRequest {
    public BonusDaysRequest(long day, int page) {
        this.day = day;
        this.page = page;
    }

   long day;//天
    int page;

}
