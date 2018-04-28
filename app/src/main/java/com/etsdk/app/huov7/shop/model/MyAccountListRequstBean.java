package com.etsdk.app.huov7.shop.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * 通用分页请求
 * 2017/5/16.
 */

public class MyAccountListRequstBean extends BaseRequestBean {
    private String gameid;

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }
}
