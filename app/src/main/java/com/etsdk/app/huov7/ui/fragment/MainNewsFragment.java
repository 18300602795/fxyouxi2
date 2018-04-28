package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.BuildConfig;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.model.MessageEvent;
import com.etsdk.app.huov7.ui.DownloadManagerActivity;
import com.etsdk.app.huov7.ui.MessageActivity;
import com.etsdk.app.huov7.ui.SearchActivity;
import com.flyco.tablayout.SlidingTabLayout;
import com.liang530.views.viewpager.SViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 资讯
 * 2017/5/11.
 */

public class MainNewsFragment extends AutoLazyFragment {
    @BindView(R.id.main_gameSearch)
    TextView mainGameSearch;
    @BindView(R.id.iv_tj_downManager)
    ImageView ivTjDownManager;
    @BindView(R.id.iv_gotoMsg)
    ImageView ivGotoMsg;
    @BindView(R.id.rl_goto_msg)
    RelativeLayout rlGotoMsg;
    @BindView(R.id.tab_main_game)
    SlidingTabLayout tabMainGame;
    @BindView(R.id.vp_main_news)
    SViewPager vpMainNews;
    private MyPagerAdapter mAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private String[] titleList = {"新闻", "活动"};
    private int position = 0;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_main_news);
        EventBus.getDefault().register(this);
        setupUI();
    }

    private void setupUI() {
        if(BuildConfig.projectCode != 137){
            fragmentList.add(NewsListFragment.newInstance("1", null));
            fragmentList.add(NewsListFragment.newInstance("3", null));
            fragmentList.add(NewsListFragment.newInstance("2", null));
            fragmentList.add(NewsListFragment.newInstance("5", null));
            titleList = new String[]{"新闻", "攻略", "活动", "评测"};
        }else{
            fragmentList.add(NewsListFragment.newInstance("1", null));
            fragmentList.add(NewsListFragment.newInstance("2", null));
            titleList = new String[]{"新闻", "活动"};
        }
        mAdapter = new MyPagerAdapter(getFragmentManager());
        vpMainNews.setCanScroll(true);
        vpMainNews.setAdapter(mAdapter);
//        vpMainNews.setCurrentItem(position, false);
        vpMainNews.setOffscreenPageLimit(2);

        tabMainGame.setViewPager(vpMainNews);
//        tabMainGame.setCurrentTab(position);

    }


    @OnClick({R.id.main_gameSearch, R.id.iv_tj_downManager, R.id.rl_goto_msg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_gameSearch:
                SearchActivity.start(mContext);
                break;
            case R.id.iv_tj_downManager:
                DownloadManagerActivity.start(mContext);
                break;
            case R.id.rl_goto_msg:
                MessageActivity.start(mContext);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        if (messageEvent != null && "2".equals(messageEvent.getNewMsg())) {
            ivGotoMsg.setImageResource(R.mipmap.syxiaoxi_red);
        } else {
            ivGotoMsg.setImageResource(R.mipmap.syxiaoxi_nomal);
        }
    }

    @Override
    protected void onDestroyViewLazy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroyViewLazy();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
    }
}
