package com.etsdk.app.huov7.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * Created by liu hong liang on 2016/11/12.
 */

public class SmsMailRequestBean extends BaseRequestBean {
    private String email;//	是	STRING	接收邮箱地址
    private String type	;//是	STRING	此次发送邮箱类型 短信邮件发送对应类型 2017-06-22 默认为4

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
