package com.etsdk.app.huov7.base;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;

import com.etsdk.app.huov7.BuildConfig;
import com.etsdk.app.huov7.model.InstallApkRecord;
import com.game.sdk.log.L;
import com.liang530.application.BaseApplication;
import com.liulishuo.filedownloader.FileDownloader;
import com.mob.MobSDK;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liu hong liang on 2016/12/1.
 */

public class AileApplication extends BaseApplication {
    private Map<String,InstallApkRecord> installingApkList=new HashMap<>();
    public static String agent;
    boolean f = false;
    public static String imei;
    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(getApplicationContext(), "23eef8ceee721", "0d1fab9808cfd3c136ea9326678f685d");
        MultiTypeInstaller.start();
        L.init(BuildConfig.LOG_DEBUG);
        com.liang530.log.L.init(BuildConfig.LOG_DEBUG);
        //设置同时最大下载数量
        FileDownloader.init(getApplicationContext());
        FileDownloader.getImpl().setMaxNetworkThreadCount(8);
        imei = getIMEI(this);
        L.i("333", "imei：" + imei);
    }

    @Override
    public Class getLoginClass() {
        return null;
    }
    public Map<String, InstallApkRecord> getInstallingApkList() {
        return installingApkList;
    }
    /**
     * 获取手机IMEI号
     *
     * 需要动态权限: android.permission.READ_PHONE_STATE
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();

        return imei;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base); MultiDex.install(this);
    }
}
