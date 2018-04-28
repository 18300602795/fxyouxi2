package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/1/17.
 */

public class UserInfoResultBean {
    private String portrait;//	URL 头像	头像地址
    private String nickname;//	STRING	昵称	昵称长城
    private String myintegral;//	FLOAT	我的积分	12
    private String couponcnt;//	INT		12
    private String giftcnt;	//INT	礼包数量	12
    private String gmgamecnt;//	INT	游戏币游戏数量	（代金券数量）
    private String newmsg;	//INT	是否有新消息	2 有 1 没有 空 没有
    private String mobile;	//String 手机号
    private String email;	//String 邮箱
    private String agent_id;	//String 渠道
    private float ptbcnt;	// 平台币数量
    private String name;    //String 渠道名字
    private String give_integral; // 今日积分

    public String getGive_integral() {
        return give_integral;
    }

    public void setGive_integral(String give_integral) {
        this.give_integral = give_integral;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public float getPtbcnt() {
        return ptbcnt;
    }

    public void setPtbcnt(float ptbcnt) {
        this.ptbcnt = ptbcnt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMyintegral() {
        return myintegral;
    }

    public void setMyintegral(String myintegral) {
        this.myintegral = myintegral;
    }

    public String getCouponcnt() {
        return couponcnt;
    }

    public void setCouponcnt(String couponcnt) {
        this.couponcnt = couponcnt;
    }

    public String getGiftcnt() {
        return giftcnt;
    }

    public void setGiftcnt(String giftcnt) {
        this.giftcnt = giftcnt;
    }

    public String getGmgamecnt() {
        return gmgamecnt;
    }

    public void setGmgamecnt(String gmgamecnt) {
        this.gmgamecnt = gmgamecnt;
    }

    public String getNewmsg() {
        return newmsg;
    }

    public void setNewmsg(String newmsg) {
        this.newmsg = newmsg;
    }
}
