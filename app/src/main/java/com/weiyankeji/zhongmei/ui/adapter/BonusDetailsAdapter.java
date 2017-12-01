package com.weiyankeji.zhongmei.ui.adapter;

import android.content.Context;
import android.view.View;

import com.weiyankeji.library.customview.adapter.BaseRecycleViewAdapter;
import com.weiyankeji.library.customview.adapter.BaseViewHolder;
import com.weiyankeji.library.utils.TimeUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.mmodel.bonus.BonusDaysResponse;
import com.weiyankeji.zhongmei.ui.mmodel.bonus.BonusInviteeResponse;
import com.weiyankeji.zhongmei.utils.MoneyFormatUtils;


import java.util.List;

/**
 * Created by zff on 2017/9/26.
 */

public class BonusDetailsAdapter extends BaseRecycleViewAdapter<Object, BaseViewHolder> {
    private Context mContext;

    public BonusDetailsAdapter(Context context, List beans) {
        super(R.layout.item_bonus, beans);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {
        if (item instanceof BonusDaysResponse) {
            BonusDaysResponse bonusDaysResponse = (BonusDaysResponse) item;
            helper.setText(R.id.tv_item_bonus_title, mContext.getString(R.string.invite_total_invite_user_title, bonusDaysResponse.invitee));
            helper.setText(R.id.tv_item_bonus_money, mContext.getString(R.string.common_yuan, MoneyFormatUtils.doubleChangeF2Y(bonusDaysResponse.bonus)));
            helper.getView(R.id.tv_item_bonus_time).setVisibility(View.GONE);

        }

        if (item instanceof BonusInviteeResponse) {
            BonusInviteeResponse bonusInviteeResponse = (BonusInviteeResponse) item;
            helper.setText(R.id.tv_item_bonus_title, mContext.getString(R.string.bonus_title_escription));
            helper.setText(R.id.tv_item_bonus_money, mContext.getString(R.string.common_yuan, MoneyFormatUtils.doubleChangeF2Y(bonusInviteeResponse.bonus)));
            helper.setText(R.id.tv_item_bonus_time, TimeUtil.getYearMonthDayFormatStr(bonusInviteeResponse.bonus_time));
            helper.getView(R.id.tv_item_bonus_time).setVisibility(View.VISIBLE);

        }

    }
}
