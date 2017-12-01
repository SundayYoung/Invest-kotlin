package com.weiyankeji.zhongmei.ui.fragment;


import android.os.Bundle;
import android.view.View;


import com.weiyankeji.library.customview.adapter.BaseRecycleViewAdapter;
import com.weiyankeji.library.customview.adapter.BaseViewHolder;
import com.weiyankeji.library.utils.ExtendUtil;
import com.weiyankeji.library.utils.TimeUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.mmodel.bean.InviteRewardBean;
import com.weiyankeji.zhongmei.ui.mmodel.bean.InviteTotalBean;
import com.weiyankeji.zhongmei.ui.mmodel.mine.InviteResponse;
import com.weiyankeji.zhongmei.ui.mmodel.mine.MyInviteRequest;
import com.weiyankeji.zhongmei.ui.mpresenter.InvitePresenter;
import com.weiyankeji.zhongmei.ui.mview.InviteView;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.MoneyFormatUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的邀请-累计邀请
 */
public class MyInviteTotalInviteFragment extends BaseListFragment<InviteView, InvitePresenter> implements InviteView {

    private CusAdapter mAdapter;
    private InvitePresenter mInvitePresenter;

    private MyInviteRequest mRequest;
    private int mPage = 1;

    private List<InviteTotalBean> mInviteList = new ArrayList<>();

    @Override
    public void onRefreshCallback() {
        if (mInviteList != null) {
            mInviteList.clear();
        }
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
        mInvitePresenter = ((InvitePresenter) mPresenter);
        setNotTitle();

        mAdapter = new CusAdapter();
        mAdapter.setOnItemClickListener(new BaseRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecycleViewAdapter adapter, View view, int position) {
                InviteTotalBean inviteTotalBean = mInviteList.get(position);
                startFragment(BonusDetailsFragment.class, mInvitePresenter.getBundle(inviteTotalBean.invitee_uid, inviteTotalBean.bonus, inviteTotalBean.invitee));
            }
        });

        loadData();

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

    private void loadData() {
        if (mRequest == null) {
            mRequest = new MyInviteRequest();
        }
        mRequest.page = mPage;
        mInvitePresenter.loadData(mRequest, false);
    }

    @Override
    public void setListData(InviteResponse response) {
        if (ExtendUtil.listIsNullOrEmpty(response.invitee_list)) {
            setErrorPageView(R.drawable.common_img_norecord, getString(R.string.invite_total_empty_tip), null, null);
            return;
        }
        if (response.invitee_list.size() < ConstantUtils.PAGE_SIZE) {
            setCannotLoad();
        }

        mInviteList.addAll(response.invitee_list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setRewardData(List<InviteRewardBean> rewardData) {
    }

    @Override
    public void responseFail(int code, String msg) {
        setErrorPageView(R.drawable.common_img_norecord, getString(R.string.invite_total_empty_tip), null, null);
    }

    private class CusAdapter extends BaseRecycleViewAdapter<InviteTotalBean, BaseViewHolder> {

        CusAdapter() {
            super(R.layout.item_my_invite, mInviteList);
        }

        @Override
        protected void convert(BaseViewHolder helper, InviteTotalBean item) {
            helper.setText(R.id.tv_user_name, mContext.getString(R.string.invite_total_invite_user, item.invitee));
            helper.setText(R.id.tv_time, mContext.getString(R.string.invite_total_invite_time, TimeUtil.getYearMonthDayFormatStr(item.reg_time)));
            helper.setText(R.id.tv_money, mContext.getString(R.string.common_yuan, MoneyFormatUtils.decimalFormatRuleOne(item.bonus)));
        }
    }

}