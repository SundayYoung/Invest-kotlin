package com.weiyankeji.zhongmei.ui.adapter;

import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.weiyankeji.library.utils.ExtendUtil;
import com.weiyankeji.zhongmei.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页底部 fragments
 */
public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private List<BaseFragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();
    private BaseFragment mCurrentFragment;

    public FragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    public void addFragment(BaseFragment fragment) {
        mFragments.add(fragment);
    }

    public void addFragment(BaseFragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }

    @Override
    public BaseFragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((BaseFragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ExtendUtil.listIsNullOrEmpty(mFragmentTitles) ? null : mFragmentTitles.get(position);
    }

    /**
     * Get the current fragment
     */
    public BaseFragment getCurrentFragment() {
        return mCurrentFragment;
    }
}