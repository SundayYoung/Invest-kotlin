package com.weiyankeji.zhongmei.ui.adapter;

import android.support.v4.app.FragmentManager;

import com.weiyankeji.zhongmei.ui.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * Created by caiwancheng on 2017/8/31.
 */

public class CostomViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<BaseFragment> mFragment = new ArrayList<>();

    public CostomViewPagerAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments) {
        super(fm);
        this.mFragment = fragments;
    }

    @Override
    public BaseFragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }
}