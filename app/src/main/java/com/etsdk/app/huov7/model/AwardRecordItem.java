package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2016/12/30.
 */
public class AwardRecordItem {
    private String gettime;//date	获取时间	2016-08-16 09:00:00
    private String subusername;//	STRING	被邀请人名称	testtest
    private String integral;//	FLOAT	获得的积分	30

    public String getGettime() {
        return gettime;
    }

    public void setGettime(String gettime) {
        this.gettime = gettime;
    }

    public String getSubusername() {
        return subusername;
    }

    public void setSubusername(String subusername) {
        this.subusername = subusername;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }
}