package com.etsdk.app.huov7.shop.model;

/**
 * Created by Administrator on 2017/6/27 0027.
 */

public class SelectLittleAccountEvent {
    String menId;
    String accont;

    public SelectLittleAccountEvent(String gameId, String accont) {
        this.menId = gameId;
        this.accont = accont;
    }

    public String getMenId() {
        return menId;
    }

    public void setMenId(String menId) {
        this.menId = menId;
    }

    public String getAccont() {
        return accont;
    }

    public void setAccont(String accont) {
        this.accont = accont;
    }
}
