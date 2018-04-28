package com.game.sdk.so;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.game.sdk.SdkConstant;
import com.game.sdk.domain.NotProguard;
import com.game.sdk.util.DeviceUtil;

/**
 * Created by Liuhongliangsdk on 2016/11/6.
 */
@NotProguard
public class SdkNative {
    public final static int TYPE_APP=1;
    public final static int TYPE_SDK=2;
    static {
        System.loadLibrary("hs_sdk-lib");
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     * 网络初始化
     */
    public static native void initNetConfig(Context context, NativeListener listener);
    /**
     * 本地初始化
     * @param context
     * @param apkType apk类型，1为app,2为sdk
     * @return true：需要网络初始化，false:不需要网络初始化了
     *
     */
    public static native boolean initLocalConfig(Context context,int apkType);


    /**
     * 获取之前的install结果
     * @param context
     * @return
     */
    public static String getInstallResult(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SdkConstant.SP_RSA_PUBLIC_KEY, Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(SdkConstant.SP_RSA_PUBLIC_KEY, null);
        if(TextUtils.isEmpty(value)){//没有找到，需要安装
            return null;
        }
        return value;
    }
    /**
     * 保存install结果
     * @param context
     * @param value 值
     * @return
     */
    public static void saveInstallResult(Context context,String value){
        if(TextUtils.isEmpty(value)){
            return ;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(SdkConstant.SP_RSA_PUBLIC_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(SdkConstant.SP_RSA_PUBLIC_KEY,value).apply();
    }
    public static int addInstallOpenCnt(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SdkConstant.SP_RSA_PUBLIC_KEY, Context.MODE_PRIVATE);
        int openCnt = sharedPreferences.getInt(SdkConstant.SP_OPEN_CNT, 1);
        sharedPreferences.edit().putInt(SdkConstant.SP_OPEN_CNT,openCnt+1).commit();
        return openCnt+1;
    }
    public static void resetInstall(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SdkConstant.SP_RSA_PUBLIC_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }
    public static void soInit(Context context){
        //初始化设备信息
        SdkConstant.deviceBean = DeviceUtil.getDeviceBean(context);
    }
}
