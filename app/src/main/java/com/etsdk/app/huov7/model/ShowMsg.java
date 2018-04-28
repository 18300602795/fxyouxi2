package com.etsdk.app.huov7.model;

/**
 * Created by Administrator on 2018/1/23.
 */

public class ShowMsg {
    private boolean isShow;
    private boolean isUp;
    private String num;

    public ShowMsg(boolean isShow, boolean isUp, String num) {
        this.isShow = isShow;
        this.isUp = isUp;
        this.num = num;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public ShowMsg(boolean isShow, boolean isUp) {
        this.isShow = isShow;
        this.isUp = isUp;
    }

    public ShowMsg(boolean isUp) {
        this.isUp = isUp;
    }

    public boolean isUp() {
        return isUp;
    }

    public void setUp(boolean up) {
        isUp = up;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
