package com.game.sdk.domain;

import com.etsdk.app.huov7.BuildConfig;
import com.game.sdk.SdkConstant;
import com.liang530.control.LoginControl;

/**
 * Created by liu hong liang on 2016/11/9.
 */

public class BaseRequestBean {
    private String app_id = SdkConstant.HS_APPID; //是	INT	游戏ID
    private String client_id = SdkConstant.HS_CLIENTID;    //	是	INT	客户端ID
    private String from = SdkConstant.FROM;   //	是	INT	来源信息 1-WEB、2-WAP、3-Android、4-IOS、5-WP
    private String agentgame = SdkConstant.HS_AGENT;    //是	STRING	玩家所属渠道 默认为’’
//    private String user_token=LoginControl.getToken()==null?"":LoginControl.getToken();    //是	STRING	此次连接token
    private String user_token= LoginControl.getToken();    //是	STRING	此次连接token
    private long timestamp=0;    //是	STRING	客户端时间戳 timestamp
    private String verid= BuildConfig.VERSION_CODE+"";    //是	STRING	版本号
    private DeviceBean device= SdkConstant.deviceBean;

    public BaseRequestBean() {
        timestamp=System.currentTimeMillis()+ SdkConstant.SERVER_TIME_INTERVAL;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getAgentgame() {
        return agentgame;
    }

    public void setAgentgame(String agentgame) {
        this.agentgame = agentgame;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getVerid() {
        return verid;
    }

    public void setVerid(String verid) {
        this.verid = verid;
    }

    public DeviceBean getDevice() {
        return device;
    }

    public void setDevice(DeviceBean device) {
        this.device = device;
    }
}
