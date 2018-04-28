package com.etsdk.app.huov7.shop.model;

/**
 * Created by liu hong liang on 2017/6/17.
 */

public class ShareOptionEvent {
    public final static int WEIBO =1;
    public final static int WEIXIN =2;
    public final static int PENGYOUQUAN =3;
    public final static int QQ =4;
    public final static int QQKONGJIAN =5;
    public final static int COPY =6;
    public int type;

    public ShareOptionEvent(int type) {
        this.type = type;
    }
}
