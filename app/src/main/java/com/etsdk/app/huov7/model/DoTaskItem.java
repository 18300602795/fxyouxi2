package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/2/8.
 */
public class DoTaskItem {
    private String actid;//9,
    private String actname;//手机绑定,
    private String actcode;//bindmobile,
    private String actdesc;//手机绑定,
    private String integral;//20,
    private String starttime;//946656000,
    private String enttime;//4072694400,
    private String typeid;//2
    private String finishflag;//	2 已完成 1 未完成
    private String myMoney;//我总共充值的钱

    public String getMyMoney() {
        return myMoney;
    }

    public void setMyMoney(String myMoney) {
        this.myMoney = myMoney;
    }

    public String getActid() {
        return actid;
    }

    public void setActid(String actid) {
        this.actid = actid;
    }

    public String getActname() {
        return actname;
    }

    public void setActname(String actname) {
        this.actname = actname;
    }

    public String getActcode() {
        return actcode;
    }

    public void setActcode(String actcode) {
        this.actcode = actcode;
    }

    public String getActdesc() {
        return actdesc;
    }

    public void setActdesc(String actdesc) {
        this.actdesc = actdesc;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
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

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getFinishflag() {
        return finishflag;
    }

    public void setFinishflag(String finishflag) {
        this.finishflag = finishflag;
    }
}