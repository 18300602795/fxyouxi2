package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by liu hong liang on 2017/2/23.
 * 任务活动
 */

public class DoTaskResultBean {
    private List<DoTaskItem> list;
    private String myintegral;
    private String mymoney;

    public List<DoTaskItem> getList() {
        return list;
    }

    public void setList(List<DoTaskItem> list) {
        this.list = list;
    }

    public String getMyintegral() {
        return myintegral;
    }

    public void setMyintegral(String myintegral) {
        this.myintegral = myintegral;
    }

    public String getMymoney() {
        return mymoney;
    }

    public void setMymoney(String mymoney) {
        this.mymoney = mymoney;
    }
}
