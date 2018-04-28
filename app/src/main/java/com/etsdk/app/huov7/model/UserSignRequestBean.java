package com.etsdk.app.huov7.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * Created by liu hong liang on 2017/1/20.
 */

public class UserSignRequestBean extends BaseRequestBean{
    private int signday;//	是	INT	第几天签到

    public int getSignday() {
        return signday;
    }

    public void setSignday(int signday) {
        this.signday = signday;
    }
}
