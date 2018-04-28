package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/1/14.
 */
public class StartServerInfo1 extends StartServerInfo{
    private int arrowShowType;// 箭头显示，0，不显示，1向下，2向上
    public StartServerInfo1(String sername, String starttim,String status,int arrowShowType) {
        super(sername, starttim,status);
        this.arrowShowType=arrowShowType;
    }

    public int getArrowShowType() {
        return arrowShowType;
    }

    public void setArrowShowType(int arrowShowType) {
        this.arrowShowType = arrowShowType;
    }
}