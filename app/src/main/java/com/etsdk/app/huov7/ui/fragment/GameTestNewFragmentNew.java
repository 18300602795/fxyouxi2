package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.etsdk.app.huov7.BuildConfig;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.model.SwitchFragmentEvent;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.liang530.views.viewpager.SViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 主页游戏 开服开测，内嵌两个fragment
 */

public class GameTestNewFragmentNew extends AutoLazyFragment {
    @BindView(R.id.tab)
    SegmentTabLayout tab;
    @BindView(R.id.vp_list)
    SViewPager vpList;
    @BindView(R.id.tab_line)
    View tab_line;

    private String[] mTitles;
    private ArrayList<Fragment> mFragments = new ArrayList();
    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_comm_tab);
        EventBus.getDefault().register(this);
        setupUI();
    }


    private void setupUI() {
        mFragments.add(new GameStartFragment());
        if(BuildConfig.projectCode != 137){
            mFragments.add(new GameTestFragment());
            mTitles = new String[]{"开服", "开测"};
            tab.setVisibility(View.VISIBLE);
            tab_line.setVisibility(View.VISIBLE);
        }else{
            mTitles = new String[]{"开服"};
            tab.setVisibility(View.GONE);
            tab_line.setVisibility(View.GONE);
        }
        tab.setTabData(mTitles);
        vpList.setAdapter(new MPagerAdapter(getFragmentManager()));
        vpList.setCanScroll(true);
        bindTab(tab, vpList);
    }

    private void bindTab(final SegmentTabLayout tabMainGame, final SViewPager vpMainGame){
        tabMainGame.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vpMainGame.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        vpMainGame.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabMainGame.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 切换页面
     *
     * @param switchFragmentEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSwitchFragmentEvent(SwitchFragmentEvent switchFragmentEvent) {
        if (this.getActivity().getClass().getName().equals(switchFragmentEvent.activityClassName)) {
            if (switchFragmentEvent.positions.length > 2) {
                if(switchFragmentEvent.positions[2]==1){
                    tab.setCurrentTab(1);
                }else{
                    tab.setCurrentTab(0);
                }
                EventBus.getDefault().removeStickyEvent(switchFragmentEvent);
            }
        }
    }

    private class MPagerAdapter extends FragmentPagerAdapter {
        public MPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
