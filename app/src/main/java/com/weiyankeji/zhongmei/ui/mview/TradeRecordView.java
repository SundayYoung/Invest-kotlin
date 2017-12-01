package com.weiyankeji.zhongmei.ui.mview;

import com.weiyankeji.zhongmei.ui.mmodel.mine.TradeRecordResponse;

import java.util.List;

/**
 * Created by caiwancheng on 2017/8/25.
 */

public interface TradeRecordView extends BaseView {


    /**
     * 加载记录列表
     * @param itemBeans
     */
    void loadRecords(List<TradeRecordResponse> itemBeans);
}
