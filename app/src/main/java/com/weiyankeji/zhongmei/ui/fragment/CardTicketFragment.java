package com.weiyankeji.zhongmei.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.adapter.CostomViewPagerAdapter;
import com.weiyankeji.zhongmei.ui.adapter.FragmentPagerAdapter;
import com.weiyankeji.zhongmei.ui.mmodel.TicketListResponse;
import com.weiyankeji.zhongmei.ui.mpresenter.InvestListPresenter;
import com.weiyankeji.zhongmei.ui.mview.InvestListView;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.ConstantUtils;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 卡券
 * Created by caiwancheng on 2017/8/30.
 */


public class CardTicketFragment extends BaseMvpFragment<InvestListView, InvestListPresenter> implements ViewPager.OnPageChangeListener {
    /**
     * 我的卡券
     */
    public static final int TYPE_MY_TICKET = 0;

    /**
     * 选择卡券
     */
    public static final int TYPE_SELECT_TICKET = 1;
    @BindView(R.id.tl_card_tictet)
    TabLayout mTlTicket;
    @BindView(R.id.vp_card_ticket)
    ViewPager mVpTicket;

    private FragmentPagerAdapter mAdapter;

    private ArrayList<BaseFragment> mFragments = new ArrayList<>();

    private int mTicketType = TYPE_MY_TICKET;

    @Override
    public int setContentLayout() {
        return R.layout.fragment_card_ticket;
    }


    @Override
    public void finishCreateView(Bundle bundle) {
        if (bundle != null) {
            mTicketType = bundle.getInt(ConstantUtils.KEY_TYPE, TYPE_MY_TICKET);
        }

        if (mTicketType == TYPE_MY_TICKET) {
            setTitle(getString(R.string.my_crad_ticket), true);
            mTlTicket.addTab(mTlTicket.newTab().setText(mContext.getString(R.string.unuse)), true);
            mTlTicket.addTab(mTlTicket.newTab().setText(mContext.getString(R.string.used)));
            mTlTicket.addTab(mTlTicket.newTab().setText(mContext.getString(R.string.overdue)));
            addTicketFragment(ConstantUtils.TICKET_UNUSE, null, null, 0);
            addTicketFragment(ConstantUtils.TICKET_USED, null, null, 0);
            addTicketFragment(ConstantUtils.TICKET_TIMED, null, null, 0);
        } else {
            setTitle(getString(R.string.select_ticket), true);
            mTlTicket.addTab(mTlTicket.newTab().setText(mContext.getString(R.string.available)), true);
            mTlTicket.addTab(mTlTicket.newTab().setText(mContext.getString(R.string.unavailable)));
            TicketListResponse response = (TicketListResponse) bundle.getSerializable(ConstantUtils.KEY_DATA);
            String skuId = bundle.getString(ConstantUtils.KEY_ID);
            long money = bundle.getLong(ConstantUtils.KEY_VALUE);
            addTicketFragment(ConstantUtils.TICKET_AVAILABLE, response, skuId, money);
            addTicketFragment(ConstantUtils.TICKET_UNAVAILABLE, null, skuId, money);
        }

        CommonUtils.setIndicator(mContext, mTlTicket, 30);
        mAdapter = new CostomViewPagerAdapter(getFragmentManager(), mFragments);
        mVpTicket.setAdapter(mAdapter);
        mVpTicket.addOnPageChangeListener(this);
        mVpTicket.setCurrentItem(0);
        mVpTicket.setOffscreenPageLimit(4);
        mTlTicket.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mVpTicket.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public InvestListPresenter initPresenter() {
        return new InvestListPresenter();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mVpTicket.setCurrentItem(position);
        mTlTicket.getTabAt(position).select();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 添加Fragment
     *
     * @param type
     * @param itemBean
     */
    public void addTicketFragment(int type, TicketListResponse itemBean, String skuId, long value) {
        TicketListFragment fragment = new TicketListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ConstantUtils.KEY_TYPE, type);
        bundle.putSerializable(ConstantUtils.KEY_DATA, itemBean);
        bundle.putString(ConstantUtils.KEY_ID, skuId);
        bundle.putLong(ConstantUtils.KEY_VALUE, value);
        fragment.setArguments(bundle);
        mFragments.add(fragment);
//        mFragments.add(TicketListFragment.newInstance(type, itemBean));
    }
}
