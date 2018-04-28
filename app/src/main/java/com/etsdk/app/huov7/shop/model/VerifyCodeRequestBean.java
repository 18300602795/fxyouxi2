package com.etsdk.app.huov7.shop.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * 卖号的短信验证
 */

public class VerifyCodeRequestBean extends BaseRequestBean {
   private String mobile;
   private String smstype;

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
