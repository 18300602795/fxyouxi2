package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/2/24.
 * 推广员ui界面数据
 */

public class TgyResultBean {
    private String myintegral;//	FLOAT	我的积分
    private String totalitg;//	FLOAT	累计积分奖励
    private String usercnt;//	INT	邀请人数
    private String disc;//	STRING	邀请说明

    public String getMyintegral() {
        return myintegral;
    }

    public void setMyintegral(String myintegral) {
        this.myintegral = myintegral;
    }

    public String getTotalitg() {
        return totalitg;
    }

    public void setTotalitg(String totalitg) {
        this.totalitg = totalitg;
    }

    public String getUsercnt() {
        return usercnt;
    }

    public void setUsercnt(String usercnt) {
        this.usercnt = usercnt;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }
}
