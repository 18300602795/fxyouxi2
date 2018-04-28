package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/1/20.
 */

public class UserSignResultBean {
    private String status	;//INT	2 签到成功 1 签到失败 3已签到	签到状态
    private String myintegral;//	FLOAT	玩家积分	玩家积分
    public String getMyintegral() {
        return myintegral;
    }

    public void setMyintegral(String myintegral) {
        this.myintegral = myintegral;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
