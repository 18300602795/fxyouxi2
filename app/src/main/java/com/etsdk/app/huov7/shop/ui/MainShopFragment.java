package com.etsdk.app.huov7.shop.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.model.MessageEvent;
import com.etsdk.app.huov7.ui.DownloadManagerActivity;
import com.etsdk.app.huov7.ui.MessageActivity;
import com.etsdk.app.huov7.ui.SearchActivity;
import com.etsdk.app.huov7.view.ShopSelectTabView;
import com.liang530.views.viewpager.SViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 商城
 * 2017/6/8.
 */

public class MainShopFragment extends AutoLazyFragment {
    @BindView(R.id.main_gameSearch)
    TextView mainGameSearch;
    @BindView(R.id.iv_tj_downManager)
    ImageView ivTjDownManager;
    @BindView(R.id.iv_gotoMsg)
    ImageView ivGotoMsg;
    @BindView(R.id.rl_goto_msg)
    RelativeLayout rlGotoMsg;
    @BindView(R.id.vp_main_shop)
    SViewPager vpMainShop;
    @BindView(R.id.select_tab)
    ShopSelectTabView selectTab;
    private MyPagerAdapter mAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_main_shop);
        EventBus.getDefault().register(this);
        setupUI();
    }

    private void setupUI() {
//        mainGameSearch.setText("输入想搜索的活动");

        fragmentList.add(ShopListFragment.newInstance(ShopListFragment.TYPE_ALL));
        fragmentList.add(SellFragment.newInstance(SellFragment.MODE_PUBLISH, null, 0, null, null, null, null, null, 0, null, null, null));
        fragmentList.add(ShopListFragment.newInstance(ShopListFragment.TYPE_MY_BOUGHT));
        fragmentList.add(ShopListFragment.newInstance(ShopListFragment.TYPE_MY_SELL));
        fragmentList.add(ShopListFragment.newInstance(ShopListFragment.TYPE_MY_FAVOR));

        mAdapter = new MyPagerAdapter(getFragmentManager());
        vpMainShop.setCanScroll(true);
        vpMainShop.setAdapter(mAdapter);
        vpMainShop.setOffscreenPageLimit(2);
        vpMainShop.setCanScroll(false);

        selectTab.setViewPager(vpMainShop);
    }


    @OnClick({R.id.main_gameSearch, R.id.iv_tj_downManager, R.id.rl_goto_msg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_gameSearch:
//                SearchActivityActivity.start(mContext);
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
            return "";
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
    }
}
