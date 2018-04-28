package com.etsdk.app.huov7.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * Created by liu hong liang on 2017/6/21.
 */

public class AccountInfoRequestBean extends BaseRequestBean {
    private String username;//	是	STRING	玩家账号

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
