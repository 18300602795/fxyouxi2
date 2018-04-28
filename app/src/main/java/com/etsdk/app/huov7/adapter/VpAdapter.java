package com.etsdk.app.huov7.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by liu hong liang on 2016/12/9.
 */

public class VpAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList;
    private String[] titleList;
    public VpAdapter(FragmentManager fm,List<Fragment> fragmentList,String [] titleList) {
        super(fm);
        this.fragmentList=fragmentList;
        this.titleList=titleList;
    }
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList[position];
    }
}
