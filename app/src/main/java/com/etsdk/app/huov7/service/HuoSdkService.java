package com.etsdk.app.huov7.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.etsdk.app.huov7.BuildConfig;
import com.etsdk.app.huov7.http.AppApi;
import com.game.sdk.HuosdkManager;
import com.game.sdk.listener.OnInitSdkListener;
import com.liang530.log.L;
import com.liang530.log.T;

/**
 * Created by liu hong liang on 2017/1/15.
 * sdk初始化相关服务
 */

public class HuoSdkService extends Service{
    private static final String TAG = HuoSdkService.class.getSimpleName();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(BuildConfig.projectCode != 137){
            AppApi.AD_IMAGE_HW_RATA = 300/720f;
        }
        HuosdkManager huosdkManager = HuosdkManager.getInstance();
        huosdkManager.initSdk(this, new OnInitSdkListener() {
            @Override
            public void initSuccess(String code, String msg) {
                L.e(TAG,"初始化成功了");
            }

            @Override
            public void initError(String code, String msg) {
                T.s(HuoSdkService.this,"初始化失败，请稍后重新打开应用");
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

}
