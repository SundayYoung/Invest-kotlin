package com.weiyankeji.zhongmei.ui.fragment;


import android.os.Bundle;
import android.view.View;

import com.weiyankeji.library.customview.adapter.BaseRecycleViewAdapter;
import com.weiyankeji.library.customview.adapter.BaseViewHolder;
import com.weiyankeji.library.utils.ExtendUtil;
import com.weiyankeji.library.utils.TimeUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.mmodel.bean.InviteRewardBean;
import com.weiyankeji.zhongmei.ui.mmodel.mine.InviteResponse;
import com.weiyankeji.zhongmei.ui.mmodel.mine.MyInviteRequest;
import com.weiyankeji.zhongmei.ui.mpresenter.InvitePresenter;
import com.weiyankeji.zhongmei.ui.mview.InviteView;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.MoneyFormatUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的邀请-累计奖励
 */
public class MyInviteTotalRewardFragment extends BaseListFragment<InviteView, InvitePresenter> implements InviteView {

    private CusAdapter mAdapter;
    private List<InviteRewardBean> mItemBeans = new ArrayList<>();
    private InvitePresenter mRecordPresenter;

    private MyInviteRequest mRequest;
    private int mPage = 1;

    private String[] mStatus;

    @Override
    public void onRefreshCallback() {
        mItemBeans.clear();
        mPage = 1;
        loadData();
    }

    @Override
    public void onLoadCallback() {
        mPage++;
        loadData();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        setNotTitle();
        mAdapter = new CusAdapter();
        mAdapter.setOnItemClickListener(new BaseRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecycleViewAdapter adapter, View view, int position) {

                InviteRewardBean inviteRewardBean = mItemBeans.get(position);
                startFragment(BonusDetailsFragment.class, getBundle(inviteRewardBean.date, inviteRewardBean.bonus));
            }
        });
        mRecordPresenter = ((InvitePresenter) mPresenter);

        loadData();

        mStatus = mContext.getResources().getStringArray(R.array.reward_status);
    }

    @Override
    public BaseRecycleViewAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
        stopRefresh();
        stopLoadMore();
    }


    @Override
    public InvitePresenter initPresenter() {
        return new InvitePresenter();
    }


    @Override
    public void setListData(InviteResponse response) {

    }

    @Override
    public void setRewardData(List<InviteRewardBean> rewardData) {
        if (ExtendUtil.listIsNullOrEmpty(rewardData)) {
            setErrorPageView(R.drawable.common_img_norecord, getString(R.string.invite_total_reward_empty_tip), null, null);
            return;
        }
        if (rewardData.size() < ConstantUtils.PAGE_SIZE) {
            setCannotLoad();
        }

        mItemBeans.addAll(rewardData);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void responseFail(int code, String msg) {
        setErrorPageView(R.drawable.common_img_norecord, getString(R.string.invite_total_reward_empty_tip), null, null);
    }

    private void loadData() {
        if (mRequest == null) {
            mRequest = new MyInviteRequest();
        }
        mRequest.page = mPage;
        mRecordPresenter.loadRewardData(mRequest, false);
    }

    private class CusAdapter extends BaseRecycleViewAdapter<InviteRewardBean, BaseViewHolder> {

        CusAdapter() {
            super(R.layout.item_my_invite, mItemBeans);
        }

        @Override
        protected void convert(BaseViewHolder helper, InviteRewardBean item) {
            helper.setText(R.id.tv_user_name, TimeUtil.getYearMonthDayFormatStr(item.date));
            helper.setText(R.id.tv_time, mContext.getString(R.string.invite_total_invest_tip, mStatus[item.status]));
            helper.setText(R.id.tv_money, mContext.getString(R.string.common_yuan, MoneyFormatUtils.decimalFormatRuleOne(item.bonus)));
        }
    }


    private Bundle getBundle(long time, int bonus) {
        Bundle bundle = new Bundle();
        bundle.putInt(ConstantUtils.KEY_DATA, bonus);
        bundle.putLong(ConstantUtils.KEY_TITLE, time);
        bundle.putInt(ConstantUtils.KEY_TYPE, ConstantUtils.BOUNS_DETAILS_DAYS);
        return bundle;
    }
}