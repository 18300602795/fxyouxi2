package com.etsdk.app.huov7.getcash.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * 提现账号请求
 * Created by Administrator on 2017/5/31 0031.
 */

public class GetCashRequestBean extends BaseRequestBean {
    public int infoid;
    public float money;

    public int getInfoid() {
        return infoid;
    }

    public void setInfoid(int infoid) {
        this.infoid = infoid;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }
}
