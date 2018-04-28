package com.etsdk.app.huov7.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * Created by liu hong liang on 2016/11/12.
 */

public class SmsSendRequestBean extends BaseRequestBean {
    public final static String TYPE_REGISTER="1";
    public final static String TYPE_LOGIN="2";
    public final static String TYPE_UPDATE_PWD="3";
    public final static String TYPE_UPDATE_INFO="4";
    public final static String TYPE_FIND_PWD ="5";
    public String mobile;//	是	STRING	手机号
    public String smstype;//	是	STRING	此次发送短信类型 1 注册 2 登陆 3 修改密码 4 信息变更,5 找回密码

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSmstype() {
        return smstype;
    }

    public void setSmstype(String smstype) {
        this.smstype = smstype;
    }
}
