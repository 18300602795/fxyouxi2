package com.etsdk.app.huov7.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.etsdk.app.huov7.shop.ui.MainShopFragment;
import com.etsdk.app.huov7.ui.fragment.MainGameFragment;
import com.etsdk.app.huov7.ui.fragment.MainMineFragmentNew2;
import com.etsdk.app.huov7.ui.fragment.MainNewsFragment;
import com.etsdk.app.huov7.ui.fragment.MainTjFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu hong liang on 2016/9/23.
 */

public class MainVpAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentList=new ArrayList<>();
    public MainVpAdapter(FragmentManager fm) {
        super(fm);
        fragmentList.add(new MainTjFragment());
        fragmentList.add(new MainGameFragment());
        fragmentList.add(new MainShopFragment());
        fragmentList.add(new MainNewsFragment());
        fragmentList.add(new MainMineFragmentNew2());
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void reloadFragment(int position){
//        WebViewFragment fragment = (WebViewFragment) fragmentList.get(position);
//        fragment.reloadFragment();
    }
}
