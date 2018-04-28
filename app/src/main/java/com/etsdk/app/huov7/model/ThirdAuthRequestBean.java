package com.etsdk.app.huov7.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * 2017/5/24.
 */

public class ThirdAuthRequestBean extends BaseRequestBean {
    private String openid;
    private String access_token;
    private String portrait;
    private String nickname;
    private String head_img;
    private String introducer;
    private long expires_date;
    private int userfrom;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public String getIntroducer() {
        return introducer;
    }

    public void setIntroducer(String introducer) {
        this.introducer = introducer;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public long getExpires_date() {
        return expires_date;
    }

    public void setExpires_date(long expires_date) {
        this.expires_date = expires_date;
    }

    public int getUserfrom() {
        return userfrom;
    }

    public void setUserfrom(int userfrom) {
        this.userfrom = userfrom;
    }
}
