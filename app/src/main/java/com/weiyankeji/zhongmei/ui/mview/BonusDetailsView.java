package com.weiyankeji.zhongmei.ui.mview;

import java.util.List;

/**
 * Created by zff on 2017/9/26.
 */

public interface BonusDetailsView extends BaseView {
    void setListData(List response);
    void showLoadFial(int code, String msg);
}
