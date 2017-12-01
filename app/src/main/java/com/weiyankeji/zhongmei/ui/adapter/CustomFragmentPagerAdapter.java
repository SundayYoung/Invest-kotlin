package com.weiyankeji.zhongmei.ui.adapter;

/**
 * Created by caiwancheng on 2017/9/6.
 */

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

/**
 * @aythor: lilei
 * time: 2017/8/30  下午7:20
 * function:
 */

public class CustomFragmentPagerAdapter extends FragmentStatePagerAdapter {

    Context mContext;
    ArrayList<Fragment> mFragmentList;

    public CustomFragmentPagerAdapter(FragmentManager fm, Context mContext, ArrayList<Fragment> fragmentList) {
        super(fm);
        this.mContext = mContext;
        this.mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


}