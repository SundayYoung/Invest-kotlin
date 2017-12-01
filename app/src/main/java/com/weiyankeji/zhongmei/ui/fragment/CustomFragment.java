package com.weiyankeji.zhongmei.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.adapter.CustomFragmentPagerAdapter;
import com.weiyankeji.zhongmei.ui.mmodel.bean.CustomAssetInfoBean;
import com.weiyankeji.zhongmei.ui.mmodel.multi.SkuTypeRequest;
import com.weiyankeji.zhongmei.ui.mpresenter.CustomPresenter;
import com.weiyankeji.zhongmei.ui.mview.CustomView;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.MoneyFormatUtils;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;

/**
 * @aythor: lilei
 * time: 2017/8/24  下午1:48
 * function:
 */

public class CustomFragment extends BaseMvpFragment<CustomView, CustomPresenter> implements CustomView {

    @BindView(R.id.tv_custom_prinvipal)
    TextView mTVCustomPrivnvipal;
    @BindView(R.id.tv_interest_received)
    TextView mTVInterestReceived;
    @BindView(R.id.tv_custom_accumulated)
    TextView mTVCustomAccumulated;
    @BindView(R.id.vp_custom_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tl_custom)
    TabLayout mTlCustom;

    @BindString(R.string.mine_customization)
    String mTitleCustom;
    @BindString(R.string.mine_regular)
    String mTitleRegula;

    @BindString(R.string.custom_in_custom)
    String mInCustom;
    @BindString(R.string.custom_is_over)
    String mIsOver;

    @BindColor(R.color.blue_00)
    int mSelectColor;
    @BindColor(R.color.gray_8a)
    int mNormalColor;

    CustomPresenter mCustomPresenter;

    ArrayList<Fragment> mFragmentList;
    //定期、定制、网贷
    int mType;

    @Override
    public int setContentLayout() {
        return R.layout.fragment_custom;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        setTitleForType(bundle);
        addBackListener();
        initViewPager();
        mCustomPresenter.loadData(this, new SkuTypeRequest(mType));

    }

    /**
     * 设置标题
     *
     * @param bundle
     */
    public void setTitleForType(Bundle bundle) {
        mType = bundle.getInt(ConstantUtils.SKEY_SIGN_TAG);
        switch (mType) {
            case ConstantUtils.INVEST_SKU_TYPE_CUSTOM:
                setTitle(mTitleCustom);
                break;
            case ConstantUtils.INVEST_SKU_TYPE_STIPULATE:
                setTitle(mTitleRegula);
                break;
            default:
                break;
        }
    }

    /**
     * 设置viewpager
     */
    public void initViewPager() {
        mTlCustom.addTab(mTlCustom.newTab().setText(mInCustom), true);
        mTlCustom.addTab(mTlCustom.newTab().setText(mIsOver));

        mFragmentList = new ArrayList<>();
        mFragmentList.add(CustomInvestmentFragment.newInstance(mType, ConstantUtils.STYPE_INVESTMENT));
        mFragmentList.add(CustomInvestmentFragment.newInstance(mType, ConstantUtils.STYPE_ISOVER));
        CommonUtils.setIndicator(mContext, mTlCustom, 70);
        mViewPager.setAdapter(new CustomFragmentPagerAdapter(getActivity().getSupportFragmentManager(), getActivity(), mFragmentList));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mTlCustom.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mTlCustom.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
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
    public void setData(CustomAssetInfoBean mCustomAssetInfoBean) {
        mTVCustomPrivnvipal.setText(MoneyFormatUtils.decimalFormatRuleOne(mCustomAssetInfoBean.unback_principal));
        mTVInterestReceived.setText(MoneyFormatUtils.decimalFormatRuleOne(mCustomAssetInfoBean.unback_interest));
        mTVCustomAccumulated.setText(MoneyFormatUtils.decimalFormatRuleOne(mCustomAssetInfoBean.total_earning));
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public CustomPresenter initPresenter() {
        mCustomPresenter = new CustomPresenter();
        return mCustomPresenter;
    }

}
