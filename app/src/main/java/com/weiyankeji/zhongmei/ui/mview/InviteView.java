package com.weiyankeji.zhongmei.ui.mview;

import com.weiyankeji.zhongmei.ui.mmodel.bean.InviteRewardBean;
import com.weiyankeji.zhongmei.ui.mmodel.mine.InviteResponse;

import java.util.List;

/**
 * 我的邀请 累计邀请
 * Created by liuhaiyang on 2017/9/27.
 */

public interface InviteView extends BaseView {
    void setListData(InviteResponse response);
    void setRewardData(List<InviteRewardBean> rewardData);
    void responseFail(int code, String msg);
}
