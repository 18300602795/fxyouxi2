package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.BuildConfig;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.model.SwitchFragmentEvent;
import com.etsdk.app.huov7.ui.fragment.GameStartFragment;
import com.etsdk.app.huov7.ui.fragment.GameTestFragment;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.liang530.views.viewpager.SViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liu hong liang on 2017/1/18.
 * <p>
 * 数据分发：
 * <p>
 * <p>
 * page=1,拿到数据做数据分类处理，显示,结束刷新
 * page=2，拿到数据和已有数据合并后做分类数据处理，显示，结束加载更多
 */

public class GameTestNewActivity extends ImmerseActivity {
    @BindView(R.id.tab)
    SegmentTabLayout tab;
    @BindView(R.id.vp_list)
    SViewPager vpList;
    @BindView(R.id.tab_line)
    View tab_line;
    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    private String[] mTitles;
    private ArrayList<Fragment> mFragments = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_tab);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("开服开测");
        mFragments.add(new GameStartFragment());
        if (BuildConfig.projectCode != 137) {
            mFragments.add(new GameTestFragment());
            mTitles = new String[]{"开服", "开测"};
            tab.setVisibility(View.VISIBLE);
            tab_line.setVisibility(View.VISIBLE);
        } else {
            mTitles = new String[]{"开服"};
            tab.setVisibility(View.GONE);
            tab_line.setVisibility(View.GONE);
        }
        tab.setTabData(mTitles);
        vpList.setAdapter(new MPagerAdapter(getSupportFragmentManager()));
        vpList.setCanScroll(true);
        bindTab(tab, vpList);
    }

    private void bindTab(final SegmentTabLayout tabMainGame, final SViewPager vpMainGame) {
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
        if (getClass().getName().equals(switchFragmentEvent.activityClassName)) {
            if (switchFragmentEvent.positions.length > 2) {
                if (switchFragmentEvent.positions[2] == 1) {
                    tab.setCurrentTab(1);
                } else {
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

    @OnClick({R.id.iv_titleLeft})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, GameTestNewActivity.class);
        context.startActivity(starter);
    }


}
