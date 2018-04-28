package com.etsdk.app.huov7.base;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.service.HuoSdkService;
import com.etsdk.app.huov7.ui.StartActivity;
import com.game.sdk.SdkConstant;
import com.jaeger.library.StatusBarUtil;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.liang530.application.BaseActivity;
import com.liang530.manager.AppManager;

/**
 * Created by liu hong liang on 2017/1/10.
 */

public class ImmerseActivity extends BaseActivity {
    View huo_sdk_rl_title;//标题栏
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        huo_sdk_rl_title= findViewById(R.id.huo_sdk_rl_title);

    }
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.bg_blue),0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.onCreate(this);
        if(this instanceof StartActivity){//启动页进行重新初始化
            Intent intent=new Intent(this,HuoSdkService.class);
            startService(intent);
            SdkConstant.app_is_normal_restart=1;//正常启动
            //保持在任务栈最底部,关闭其他activity，被杀后应用根据任务栈回到之前的页面，如果是主页（singleTask 不会被杀），任务栈里还有主页
            AppManager.getAppManager().finishOtherActivity(this);
        }else{//
            if(SdkConstant.app_is_normal_restart!=1){//被杀后启动，重新启动启动页（启动页设置singleTop）
                Intent intent=new Intent(this,StartActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
        setStatusBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }
    /**
     * 改变标题栏显示状态
     * @param show
     */
    public void changeTitleStatus(boolean show){
        if(huo_sdk_rl_title==null){
            Log.e(TAG,"没有设置titleView");
            return;
        }
        if(show){
            huo_sdk_rl_title.setVisibility(View.VISIBLE);
        }else{
            huo_sdk_rl_title.setVisibility(View.GONE);
        }
    }
}
