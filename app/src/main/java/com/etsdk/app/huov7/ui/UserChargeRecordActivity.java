package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.BuildConfig;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.ChargeRecordRcyAadapter;
import com.etsdk.app.huov7.adapter.VpAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.ui.fragment.ChargeRecordFragment;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.liang530.views.viewpager.SViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserChargeRecordActivity extends ImmerseActivity {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tab)
    SlidingTabLayout tab;
    @BindView(R.id.vp)
    SViewPager vp;

    ChargeRecordRcyAadapter adapter;
    BaseRefreshLayout baseRefreshLayout;
    private List<Fragment> fragmentList=new ArrayList();
    private String[] titleNames={"游戏币充值记录", "平台币充值记录"};
    private VpAdapter vpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_list);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        if ("0".equals(BuildConfig.USE_PLATFORM_MONEY)) {//不使用平台，隐藏
            tvTitleName.setText("游戏币充值记录");
            vp.setCanScroll(false);
            vp.setCurrentItem(0);
            tab.setVisibility(View.GONE);
        }else{
            tvTitleName.setText("充值记录");
            vp.setCanScroll(true);
            vp.setCurrentItem(0);
            tab.setVisibility(View.VISIBLE);
        }
        fragmentList.add(ChargeRecordFragment.newInstance(ChargeRecordFragment.TYPE_GAME));
        fragmentList.add(ChargeRecordFragment.newInstance(ChargeRecordFragment.TYPE_PTB));
        vpAdapter=new VpAdapter(getSupportFragmentManager(),fragmentList,titleNames);
        vp.setAdapter(vpAdapter);
        tab.setViewPager(vp);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, UserChargeRecordActivity.class);
        context.startActivity(starter);
    }

    @OnClick({R.id.iv_titleLeft})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
        }
    }

}
