package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/1/21.
 */

public class MainGameAd {


    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
//        game	游戏分类页面-头部	gametypetopper
//        game	游戏分类页面-开服开测	gametestserver
        private AdImageKeyBean gametypetopper;
        private AdImageKeyBean gametestserver;

        public AdImageKeyBean getGametypetopper() {
            return gametypetopper;
        }

        public void setGametypetopper(AdImageKeyBean gametypetopper) {
            this.gametypetopper = gametypetopper;
        }

        public AdImageKeyBean getGametestserver() {
            return gametestserver;
        }

        public void setGametestserver(AdImageKeyBean gametestserver) {
            this.gametestserver = gametestserver;
        }
    }
}
