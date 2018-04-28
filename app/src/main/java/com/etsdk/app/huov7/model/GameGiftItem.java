package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2016/12/21.
 */
public class GameGiftItem extends GameBean{
    private String starttime;//	STRING	开服(开测)时间	时间戳
    private String sername;//STRING	服务器名称	71服
    private String status;//	INT	1 删档内测 2 不删档内测	测试描述

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSername() {
        return sername;
    }

    public void setSername(String sername) {
        this.sername = sername;
    }
}