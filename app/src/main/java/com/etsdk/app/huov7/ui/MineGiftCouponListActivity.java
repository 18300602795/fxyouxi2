package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.VpAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.ui.fragment.MineCardFragment;
import com.etsdk.app.huov7.ui.fragment.MineCouponFragment;
import com.etsdk.app.huov7.ui.fragment.MineEntityFragment;
import com.etsdk.app.huov7.ui.fragment.MineGiftFragment;
import com.flyco.tablayout.SlidingTabLayout;
import com.liang530.views.viewpager.SViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MineGiftCouponListActivity extends ImmerseActivity {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.tab_gift_coupon)
    SlidingTabLayout tabGiftCoupon;
    @BindView(R.id.vp_gift_coupon)
    SViewPager vpGiftCoupon;

    private VpAdapter vpAdapter;
    private List<Fragment> fragmentList=new ArrayList();
    private String[] titleNames={"礼包","代金券","礼品卡","我的礼物"};
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_coupon_list);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        position=getIntent().getIntExtra("position",0);
        tvTitleName.setText("我的礼券包");
        fragmentList.add(new MineGiftFragment());
        fragmentList.add(new MineCouponFragment());
        fragmentList.add(new MineCardFragment());
        fragmentList.add(new MineEntityFragment());
        vpAdapter=new VpAdapter(getSupportFragmentManager(),fragmentList,titleNames);
        vpGiftCoupon.setAdapter(vpAdapter);
        vpGiftCoupon.setOffscreenPageLimit(3);
        tabGiftCoupon.setViewPager(vpGiftCoupon);
        vpGiftCoupon.setCanScroll(true);
        switchFragment(position);
    }
    public void switchFragment(int position){
        tabGiftCoupon.setCurrentTab(position);
        vpGiftCoupon.setCurrentItem(position,false);
    }
    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
        }
    }
    public static void start(Context context,int position) {
        Intent starter = new Intent(context, MineGiftCouponListActivity.class);
        starter.putExtra("position",position);
        context.startActivity(starter);
    }
}
