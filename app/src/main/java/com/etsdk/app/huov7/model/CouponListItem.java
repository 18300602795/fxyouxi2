package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/1/5.
 * 代金券实体类
 */
public class CouponListItem {
    private String couponid;//INT	代金卷ID	12
    private String couponname;//            	INT	代金卷名称	5元代金卷
    private String gameid;//        INT	代金卷对应游戏ID	123
    private String money;//           float	代金卷金额	6
    private int total;//         INT	代金卷总数	10
    private int remain;//           INT	代金卷剩余数量	2
    private String icon;//             URL 代金卷logo	默认取游戏logo
    private String starttime;//	          STRING	代金卷使用开始时间	2016-08-06 12:00:00
    private String enttime;//             STRING	金卷使用结束时间	2016-08-16 12:00:00
    private String integral;//             STRING	代金卷所需积分	100
    private String scope;//             STRING	使用范围	全服通用
    private String func;//               STRING	使用方法	使用方法
    private String mytotal;//	INT	获得玩家获得此类代金卷数量	10
    private String myremain;//INT	获得玩家拥有的代金卷数量	5
    private String myintegral;//	INT	我的实时积分	456456

    public String getMytotal() {
        return mytotal;
    }

    public void setMytotal(String mytotal) {
        this.mytotal = mytotal;
    }

    public String getMyremain() {
        return myremain;
    }

    public void setMyremain(String myremain) {
        this.myremain = myremain;
    }

    public String getCouponid() {
        return couponid;
    }

    public void setCouponid(String couponid) {
        this.couponid = couponid;
    }

    public String getCouponname() {
        return couponname;
    }

    public void setCouponname(String couponname) {
        this.couponname = couponname;
    }

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEnttime() {
        return enttime;
    }

    public void setEnttime(String enttime) {
        this.enttime = enttime;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public String getMyintegral() {
        return myintegral;
    }

    public void setMyintegral(String myintegral) {
        this.myintegral = myintegral;
    }
}