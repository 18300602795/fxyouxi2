package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.service.HuoSdkService;
import com.jaeger.library.StatusBarUtil;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.liang530.application.BaseApplication;
import com.liang530.log.SP;
import com.liang530.utils.BaseAppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartActivity extends ImmerseActivity {

    @BindView(R.id.iv_start_img)
    ImageView ivStartImg;
    @BindView(R.id.activity_start)
    RelativeLayout activityStart;
    private long startTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        setupUI();
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, Color.parseColor("#5DC9F7"));
    }

    private void setupUI() {
        startTime=System.currentTimeMillis();
        Intent intent=new Intent(this,HuoSdkService.class);
        startService(intent);
        if(isFirstRun(mContext)){
            GuideActivity.start(mContext);
            finish();
            return;
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StartActivity.this.finish();
                if(isFirstRun(mContext)){
                    GuideActivity.start(mContext);
                }else{
                    MainActivity.start(mContext,0);
                }
                finish();
            }
        },3000);
    }
    public static boolean isFirstRun(Context context) {
        boolean isFirstRun = false;
        if(SP.getSp() == null) {
            SP.init(BaseApplication.getInstance());
        }

        int versionCode = SP.getInt("versionCode", 0);
        int newAppVersion = BaseAppUtil.getAppVersionCode();
        if(versionCode != newAppVersion) {
            SP.putInt("versionCode", newAppVersion).commit();
            isFirstRun = true;
        }
        return isFirstRun;
    }
}
