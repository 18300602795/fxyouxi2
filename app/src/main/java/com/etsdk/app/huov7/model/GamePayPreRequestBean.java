package com.etsdk.app.huov7.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * Created by liu hong liang on 2017/2/23.
 */

public class GamePayPreRequestBean extends BaseRequestBean {
    private String gameid;    //否	INT	对应游戏ID
    private String money;//是	FLOAT	玩家充值游戏币金额;建议传入整数,可以保留两位小数 默认6元
    private String couponcontent;//	否	STRING	代金卷闲情 ID:CNT,ID:CNT

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public String getCouponcontent() {
        return couponcontent;
    }

    public void setCouponcontent(String couponcontent) {
        this.couponcontent = couponcontent;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
