package com.etsdk.app.huov7.model;

/**
 * Created by Administrator on 2017/5/16 .
 */

public class HelpInfoBean {
    private int app_id;
    private String tel;
    private String[] qq;
    private String email;
    private String[] qqgroup;
    private String[] qqgroupkey;
    private String wx;
    private String service_time;

    public int getApp_id() {
        return app_id;
    }

    public void setApp_id(int app_id) {
        this.app_id = app_id;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String[] getQq() {
        return qq;
    }

    public void setQq(String[] qq) {
        this.qq = qq;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] getQqgroup() {
        return qqgroup;
    }

    public void setQqgroup(String[] qqgroup) {
        this.qqgroup = qqgroup;
    }

    public String[] getQqgroupkey() {
        return qqgroupkey;
    }

    public void setQqgroupkey(String[] qqgroupkey) {
        this.qqgroupkey = qqgroupkey;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public String getService_time() {
        return service_time;
    }

    public void setService_time(String service_time) {
        this.service_time = service_time;
    }
}
