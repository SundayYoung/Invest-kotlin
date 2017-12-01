package com.weiyankeji.zhongmei.ui.mview;

import com.weiyankeji.zhongmei.ui.mmodel.TicketListResponse;

import java.util.List;

/**
 * Created by liuhaiyang on 2017/8/2.
 */

public interface TicketListView extends BaseView {
    void setListData(List<TicketListResponse> response);
}
