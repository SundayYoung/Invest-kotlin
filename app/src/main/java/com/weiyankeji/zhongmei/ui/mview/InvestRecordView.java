package com.weiyankeji.zhongmei.ui.mview;

import com.weiyankeji.zhongmei.ui.mmodel.bean.InvestRecordResponse;

import java.util.List;

/**
 * Created by caiwancheng on 2017/8/30.
 */

public interface InvestRecordView extends BaseView {
    void setData(List<InvestRecordResponse> responses);
    void responseFail(int code, String msg);
}
