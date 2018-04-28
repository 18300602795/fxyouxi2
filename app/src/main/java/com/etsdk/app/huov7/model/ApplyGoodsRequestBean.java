package com.etsdk.app.huov7.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * Created by liu hong liang on 2017/1/18.
 * 领取礼包请求bean
 */

public class ApplyGoodsRequestBean extends BaseRequestBean{

    private String goodsid;//	是	STRING	代金卷ID

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }
}
