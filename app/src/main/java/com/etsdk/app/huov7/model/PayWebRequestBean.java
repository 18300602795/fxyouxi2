package com.etsdk.app.huov7.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * web支付请求
 * 2017/5/16.
 */

public class PayWebRequestBean extends BaseRequestBean {
    private String gameid;
    private int id;//小号交易，出售id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }
}
