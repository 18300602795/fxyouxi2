package com.etsdk.app.huov7.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * Created by liu hong liang on 2017/2/7.
 */

public class UpdateNickNameRequest extends BaseRequestBean {
    private String nicename;//	是	STRING	用户昵称

    public String getNicename() {
        return nicename;
    }

    public void setNicename(String nicename) {
        this.nicename = nicename;
    }
}
