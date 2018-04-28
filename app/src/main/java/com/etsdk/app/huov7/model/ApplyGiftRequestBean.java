package com.etsdk.app.huov7.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * Created by liu hong liang on 2017/1/18.
 */

public class ApplyGiftRequestBean extends BaseRequestBean {
    private String giftid;//	是	STRING	礼包ID

    public String getGiftid() {
        return giftid;
    }

    public void setGiftid(String giftid) {
        this.giftid = giftid;
    }
}
