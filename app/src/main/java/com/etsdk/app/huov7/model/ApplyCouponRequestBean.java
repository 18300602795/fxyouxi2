package com.etsdk.app.huov7.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * Created by liu hong liang on 2017/1/18.
 * 领取礼包请求bean
 */

public class ApplyCouponRequestBean extends BaseRequestBean{

    private String couponid;//	是	STRING	代金卷ID

    public String getCouponid() {
        return couponid;
    }

    public void setCouponid(String couponid) {
        this.couponid = couponid;
    }
}
