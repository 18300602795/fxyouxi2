package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.GuideAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.jude.swipbackhelper.SwipeBackHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2017/1/16.
 * 引导页
 */
public class GuideActivity extends ImmerseActivity {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.activity_guid)
    RelativeLayout activityGuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        setupUI();
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
    }

    private void setupUI() {
        viewpager.setAdapter(new GuideAdapter());
    }
    public static void start(Context context) {
        Intent starter = new Intent(context, GuideActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void onBackPressed() {
        
    }
}
