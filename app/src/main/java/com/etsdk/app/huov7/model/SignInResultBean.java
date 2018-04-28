package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by liu hong liang on 2017/1/20.
 * 签到实体类
 */

public class SignInResultBean {
    private String count	;//INT	本月天数	本月总天数
    private String signdays	;//INT	本月签到次数	本月签到次数
    private String signdesc	;//STRING	签到说明	签到说明
    private List<SignData> list	;//二维数组	签到列表

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getSigndays() {
        return signdays;
    }

    public void setSigndays(String signdays) {
        this.signdays = signdays;
    }

    public String getSigndesc() {
        return signdesc;
    }

    public void setSigndesc(String signdesc) {
        this.signdesc = signdesc;
    }

    public List<SignData> getList() {
        return list;
    }

    public void setList(List<SignData> list) {
        this.list = list;
    }

    public static class SignData{
        private int day;//	INT	签到第几天	12
        private String integral;//	FLOAT	本日签到获得积分	本日签到获得积分
        private String signed;//	INT	2 表示已签到 1 表示未签到	签到标志

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getSigned() {
            return signed;
        }

        public void setSigned(String signed) {
            this.signed = signed;
        }
    }



}
