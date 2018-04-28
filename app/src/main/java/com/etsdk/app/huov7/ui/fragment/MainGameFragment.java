package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.BuildConfig;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.VpAdapter;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.model.MessageEvent;
import com.etsdk.app.huov7.model.SwitchFragmentEvent;
import com.etsdk.app.huov7.model.TabEntity;
import com.etsdk.app.huov7.ui.DownloadManagerActivity;
import com.etsdk.app.huov7.ui.MessageActivity;
import com.etsdk.app.huov7.ui.SearchActivity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.liang530.views.viewpager.SViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by liu hong liang on 2016/12/9.
 */

public class MainGameFragment extends AutoLazyFragment {
    @BindView(R.id.main_gameSearch)
    TextView mainGameSearch;
    @BindView(R.id.iv_tj_downManager)
    ImageView ivTjDownManager;
    @BindView(R.id.tab_main_game)
    CommonTabLayout tabMainGame;
    @BindView(R.id.vp_main_game)
    SViewPager vpMainGame;
    @BindView(R.id.iv_gotoMsg)
    ImageView ivGotoMsg;
    private VpAdapter vpAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private String[] titleList = {"BT", "折扣", "GM", "精品"};
    //    "分类",
    //    R.mipmap.fenlei_u,
//    R.mipmap.fenlei_s,
    private int[] mIconUnselectIds = {R.mipmap.kaice_u, R.mipmap.xinyoubang_u, R.mipmap.gm_icon, R.mipmap.remenbang_u};
    private int[] mIconSelectIds = {R.mipmap.kaice_s, R.mipmap.xinyoubang_s, R.mipmap.gm_icon_s, R.mipmap.remenbang_s};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private int position = 0;
    private int secondPosition = 0;
    //    MainTestNewGameFragment mainTestNewGameFragment;
    GameTestNewFragment gameTestNewFragment;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_main_game);
        EventBus.getDefault().register(this);
//        if(BuildConfig.projectCode == 137){
//            titleList[1] = "BT榜";
//            titleList[3] = "开服";
//        }else{
//            titleList[1] = "热门榜";
//            titleList[3] = "开服开测";
//        }
        setupUI();
    }

    private void setupUI() {
        for (int i = 0; i < titleList.length; i++) {
            mTabEntities.add(new TabEntity(titleList[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
//        if (BuildConfig.projectCode == 137) {
//            fragmentList.add(new GameFirstClassifyFragment());
//        } else {
//            fragmentList.add(new GameFirstClassifyFragmentV1());
//        }
        fragmentList.add(GameListFragment.newInstance(true, true, 0, 0, 0, 0, 0, 3, null));
        fragmentList.add(GameListFragment.newInstance(true, true, 0, 0, 0, 0, 0, 4, null));
        fragmentList.add(GameListFragment.newInstance(true, true, 0, 0, 0, 0, 0, 1, null));
        fragmentList.add(GameListFragment.newInstance(true, true, 0, 0, 0, 0, 0, 5, null));
//        gameTestNewFragment = new GameTestNewFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt("position", secondPosition);
//        vpMainNews.setCanScroll(true);
//        gameTestNewFragment.setArguments(bundle);
//        fragmentList.add(gameTestNewFragment);
//        fragmentList.add(new GameTestNewFragmentNew());
        vpAdapter = new VpAdapter(getChildFragmentManager(), fragmentList, titleList);
        vpMainGame.setOffscreenPageLimit(3);
        vpMainGame.setAdapter(vpAdapter);
        tabMainGame.setTabData(mTabEntities);
        tabMainGame.setCurrentTab(position);
        vpMainGame.setCurrentItem(position, false);
        vpMainGame.setCanScroll(true);
        bindTab(tabMainGame, vpMainGame);
        SwitchFragmentEvent stickyEvent = EventBus.getDefault().getStickyEvent(SwitchFragmentEvent.class);
        if (stickyEvent != null) {
            onSwitchFragmentEvent(stickyEvent);
        }
        MessageEvent messageEvent = EventBus.getDefault().getStickyEvent(MessageEvent.class);
        onMessageEvent(messageEvent);
    }

    private void bindTab(final CommonTabLayout tabMainGame, final SViewPager vpMainGame) {
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

    public void switchFragment(int position) {
        this.position = position;
        if (tabMainGame != null) {//已经赋值过了，可以直接切换
            tabMainGame.setCurrentTab(position);
            vpMainGame.setCurrentItem(position, false);
        }
    }

    /**
     * 在fragment之前收到切换通知
     *
     * @param switchFragmentEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1)
    public void onSwitchFragmentEvent(SwitchFragmentEvent switchFragmentEvent) {
        if (this.getActivity().getClass().getName().equals(switchFragmentEvent.activityClassName)) {
            if (switchFragmentEvent.positions.length > 1) {
                switchFragment(switchFragmentEvent.positions[1]);
                if (switchFragmentEvent.positions.length == 2) {
                    EventBus.getDefault().removeStickyEvent(switchFragmentEvent);
                }
            }
        }
    }

    /**
     * 切换到开服开测，并切换到开测tab
     */
    public void switchStartTestFragment() {
        this.position = 3;
        this.secondPosition = 1;//开测tab
        if (tabMainGame != null) {//已经赋值过了，可以直接切换
//            mainTestNewGameFragment.switchFragment(secondPosition);
            tabMainGame.setCurrentTab(position);
            vpMainGame.setCurrentItem(position, false);
        }
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
}
