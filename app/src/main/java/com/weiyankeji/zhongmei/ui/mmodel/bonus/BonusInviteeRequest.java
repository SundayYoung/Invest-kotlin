package com.weiyankeji.zhongmei.ui.mmodel.bonus;

import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;

/**
 * Created by zff on 2017/9/26.
 */

public class BonusInviteeRequest extends BaseRequest {

    public BonusInviteeRequest(String invitee_uuid, int page) {
        this.invitee_uuid = invitee_uuid;
        this.page = page;
    }

    String invitee_uuid;//被邀请人
    int page;

}
