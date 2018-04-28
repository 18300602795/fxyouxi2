package com.game.sdk;

import com.game.sdk.domain.DeviceBean;
import com.game.sdk.domain.NotProguard;

/**
 * Created by liu hong liang on 2016/11/7.
 * sdk常量
 *
 */
@NotProguard
public class SdkConstant {
    //app 是否是正常启动，
    // 经过应用的第一个界面启动的设置为正常启动 1（StartActivity）,
    // 为0表示应用被杀启动的，这个时候static变量信息丢失。
    //当发现被杀启动时，重新走启动流程。
    public static int app_is_normal_restart;

    //nativie自动注入常量
    public static String HS_APPID="";
    public static String HS_CLIENTID="";//
    public static String HS_CLIENTKEY="";
    public static String HS_APPKEY="";
    public static String HS_AGENT="app_100_1013";
    public static String FROM="3";//1-WEB、2-WAP、3-Android、4-IOS、5-WP

    //地址信息
    public static String BASE_URL="";//域名
    public static String BASE_SUFFIX_URL="";//后缀
    public static String BASE_IP;
    //渠道编号
    public static String PROJECT_CODE;
    //使用地址方式
    public static String USE_URL_TYPE;//1域名，2ip

    public static String APP_PACKAGENAME;//app包名

    //rsa密钥
    public static String RSA_PUBLIC_KEY;
//    ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDOvTgQeOuMIop6psK0Mk58fHur" +
//            "Sbx4pKye3reS5a6Lax3IrLazLGKQEnd+S+1q5BBVwc+JCJi/AUdbJeDkx+cCfE0M" +
//            "LbNt5DiZeKBN/hV4C+pOm0AjEkWQmJfIzsgfVpcifn1R5KsgZ0FtbfO7MOFAcYox" +
//            "HCYZduX4jhIZbgxrmwIDAQAB";

    /**
     * 初始化注入
     */
    public static String userToken="";

    /**
     * 服务器时间-本地时间
     */
    public static long SERVER_TIME_INTERVAL=0;

    public static DeviceBean deviceBean;

    //sp中保存的rsa密钥
    public static String SP_RSA_PUBLIC_KEY="sp_rsa_public_key";
    public final static String SP_VERSION_CODE="versionCode";
    public final static String SP_OPEN_CNT="sp_openCnt";

    public final static boolean READ_APP_AGENT=false;



    public final static String CODE_SUCCESS="200";
    public final static String CODE_NOLOGIN="-1000";//未登陆

    public static String newMsg;//是否有新消息	2 有 1 没有 空 没
}
