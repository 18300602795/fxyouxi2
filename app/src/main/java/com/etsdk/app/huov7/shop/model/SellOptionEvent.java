package com.etsdk.app.huov7.shop.model;

/**
 * Created by liu hong liang on 2017/6/17.
 */

public class SellOptionEvent {
    public final static int STATUS_VARIFY =1;//审核中
    public final static int STATUS_ON =2;//已上架
    public final static int STATUS_OFF =3;//已下架
    public final static int STATUS_SOLD =4;//已售出
    public final static int STATUS_VARIFY_FAIL =5;//审核失败

    public final static int OPTION_CANCEL_SELL =10;//
    public final static int OPTION_DELETE_SELL =11;//
    public final static int OPTION_EDIT_SELL =12;//

    public int type;

    public SellOptionEvent(int type) {
        this.type = type;
    }
}
