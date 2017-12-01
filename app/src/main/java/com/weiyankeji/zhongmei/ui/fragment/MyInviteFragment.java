package com.weiyankeji.zhongmei.ui.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.adapter.CostomViewPagerAdapter;
import com.weiyankeji.zhongmei.ui.adapter.FragmentPagerAdapter;
import com.weiyankeji.zhongmei.ui.mmodel.bean.InviteRewardBean;
import com.weiyankeji.zhongmei.ui.mmodel.bean.InviteTotalBean;
import com.weiyankeji.zhongmei.ui.mmodel.mine.InviteResponse;
import com.weiyankeji.zhongmei.ui.mmodel.mine.MyInviteRequest;
import com.weiyankeji.zhongmei.ui.mpresenter.InvitePresenter;
import com.weiyankeji.zhongmei.ui.mview.InviteView;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.MoneyFormatUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 我的邀请
 * Created by liuhaiyang on 2017/9/26.
 */


public class MyInviteFragment extends BaseMvpFragment<InviteView, InvitePresenter> implements InviteView, ViewPager.OnPageChangeListener {

    @BindView(R.id.tl_card_tictet)
    TabLayout mTlInvite;
    @BindView(R.id.vp_card_ticket)
    ViewPager mVpInvite;

    private FragmentPagerAdapter mAdapter;

    private ArrayList<BaseFragment> mFragments = new ArrayList<>();

    @Override
    public int setContentLayout() {
        return R.layout.fragment_my_invite;
    }


    @Override
    public void finishCreateView(Bundle bundle) {

        setTitle(getString(R.string.invite_for_mine), true);

        mTlInvite.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mVpInvite.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mPresenter.loadData(new MyInviteRequest(), true);

    }

    private void initAdapter() {
        CommonUtils.setIndicator(mContext, mTlInvite, 40);
        mAdapter = new CostomViewPagerAdapter(getFragmentManager(), mFragments);
        mVpInvite.setAdapter(mAdapter);
        mVpInvite.addOnPageChangeListener(this);
    }

    @Override
    public InvitePresenter initPresenter() {
        return new InvitePresenter();
    }

    @Override
    public void showLoading() {
        setLoading(true);
    }

    @Override
    public void hideLoading() {
        setLoading(false);
    }

    @Override
    public void setListData(InviteResponse response) {
        setTab(response.invite_num, MoneyFormatUtils.decimalFormatRuleOne(response.total_bonus));
        setTabFragment(response.invitee_list);
    }

    @Override
    public void setRewardData(List<InviteRewardBean> rewardData) {

    }

    @Override
    public void responseFail(int code, String msg) {
        setTab(0, "0");
        setTabFragment(null);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mTlInvite.getTabAt(position).select();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setTab(int num, String bonus) {
        for (int i = 0; i < 2; i++) {
            View view = View.inflate(mContext, R.layout.custom_tab_view_invite, null);
            TextView tvT = view.findViewById(R.id.tabTextTop);
            tvT.setTypeface(Typeface.DEFAULT_BOLD);
            TextView tvB = view.findViewById(R.id.tabText);
            if (i == 0) {
                tvT.setText(String.valueOf(num));
                tvB.setText(getString(R.string.invite_total_invite));
            } else {
                tvT.setText(bonus);
                tvB.setText(getString(R.string.invite_total_reward));
            }
            mTlInvite.addTab(mTlInvite.newTab().setCustomView(view));
        }
    }

    private void setTabFragment(List<InviteTotalBean> list) {
        MyInviteTotalInviteFragment inviteFragment = new MyInviteTotalInviteFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstantUtils.KEY_DATA, (Serializable) list);
        inviteFragment.setArguments(bundle);
        mFragments.add(inviteFragment);
        MyInviteTotalRewardFragment rewardFragment = new MyInviteTotalRewardFragment();
        mFragments.add(rewardFragment);

        initAdapter();
    }
}
