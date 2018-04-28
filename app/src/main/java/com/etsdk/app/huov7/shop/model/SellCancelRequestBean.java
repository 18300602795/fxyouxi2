package com.etsdk.app.huov7.shop.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * 角色商品添加\取消收藏
 */

public class SellCancelRequestBean extends BaseRequestBean {
   private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
