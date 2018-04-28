package com.etsdk.app.huov7.sharesdk;


import android.text.TextUtils;
import android.util.Log;

import com.etsdk.app.huov7.BuildConfig;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;


/**
 * Created by hongliang on 16-6-8.
 */
public class ThirdLoginUtil {
    private static final String TAG = ThirdLoginUtil.class.getSimpleName();
    public static final int LOGIN_DEF =1;
    public static final int LOGIN_QQ =2;
    public static final int LOGIN_WX =3;
    public static final int LOGIN_XLWB =4;


    //第三方信息的sp的key
    public static final String SP_ACCOUNT_TYPE ="accountType";
    public static final String SP_USERICO ="userIco";
    public static final String SP_ACCOUNT ="account";
    public static final String SP_NAME ="name";


    public static void loginByThird(final int accountType,PlatformActionListener platformActionListener) {
        //初始化SDK
        Platform platform = null;
        switch (accountType) {
            case LOGIN_QQ://qq
                platform = ShareSDK.getPlatform(QQ.NAME);
                break;
            case LOGIN_WX://微信
                platform = ShareSDK.getPlatform(Wechat.NAME);
                break;
            case LOGIN_XLWB://新浪
                platform = ShareSDK.getPlatform(SinaWeibo.NAME);
                break;
        }
        if (platform == null) {
            return;
        }
        if (platform.isAuthValid()) {
            platform.removeAccount(true);
        }
        //使用SSO授权，通过客户单授权
        platform.SSOSetting(false);
        platform.setPlatformActionListener(platformActionListener);
        platform.showUser(null);
        Log.e(TAG, "调起第三方登录");
    }

    public static void initQQ(String appId, String appKey) {
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("Id","7");
        hashMap.put("SortId","7");
        hashMap.put("AppId",appId);
        hashMap.put("AppKey",appKey);
        hashMap.put("ShareByAppClient",true);
        hashMap.put("Enable",true);
        ShareSDK.setPlatformDevInfo(QQ.NAME,hashMap);
    }
    public static void initWx(String appId, String appSecret) {
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("Id","4");
        hashMap.put("SortId","4");
        hashMap.put("AppId",appId);
        hashMap.put("AppSecret",appSecret);
        hashMap.put("BypassApproval",false);
        hashMap.put("Enable",true);
        ShareSDK.setPlatformDevInfo(Wechat.NAME,hashMap);
    }
    public static void  initXinNan(String appKey, String appSecret, String redirectUrl) {
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("Id","1");
        hashMap.put("SortId","1");
        hashMap.put("AppKey",appKey);
        hashMap.put("AppSecret",appSecret);
        hashMap.put("RedirectUrl",redirectUrl);
        hashMap.put("ShareByAppClient",true);
        hashMap.put("Enable",true);
        ShareSDK.setPlatformDevInfo(SinaWeibo.NAME,hashMap);
    }
    public static boolean isQQEnable(){
        return !TextUtils.isEmpty(BuildConfig.qq_appid)&&!TextUtils.isEmpty(BuildConfig.qq_appkey);
    }
    public static boolean isWeiXinEnable(){
        return !TextUtils.isEmpty(BuildConfig.wx_appid)&&!TextUtils.isEmpty(BuildConfig.wx_appkey);
    }
    public static boolean isWeiBoEnable(){
        return !TextUtils.isEmpty(BuildConfig.wb_appid)&&!TextUtils.isEmpty(BuildConfig.wb_appkey)
                &&!TextUtils.isEmpty(BuildConfig.wb_directurl);
    }
}
