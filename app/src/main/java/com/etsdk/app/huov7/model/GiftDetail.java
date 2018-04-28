package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/1/19.
 * 礼包详情model
 */

public class GiftDetail extends BaseModel{
    private GiftListItem data;

    public GiftListItem getData() {
        return data;
    }

    public void setData(GiftListItem data) {
        this.data = data;
    }
}
