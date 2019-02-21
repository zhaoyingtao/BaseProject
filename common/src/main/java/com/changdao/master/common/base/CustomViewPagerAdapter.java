package com.changdao.master.common.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class CustomViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;

    private List<String> mFragmentTitle;
    private long baseId = 0;

    public CustomViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragmentList = fragments;
    }

    public CustomViewPagerAdapter(FragmentManager fm, List<Fragment> mFragmentList, List<String> mFragmentTitle) {
        super(fm);
        this.mFragmentList = mFragmentList;
        this.mFragmentTitle = mFragmentTitle;
        baseId = System.currentTimeMillis();
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mFragmentTitle != null && mFragmentTitle.size() > 0 && mFragmentTitle.size() == mFragmentList.size()) {
            return mFragmentTitle.get(position);
        } else {
            return super.getPageTitle(position);
        }
    }


    @Override
    public long getItemId(int position) {
        return baseId + position;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}
