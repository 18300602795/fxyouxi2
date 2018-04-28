package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/1/18.
 * 开测表
 */

public class TestGameBean extends GameBean {
   private String testid;//	INT	开服ID	12
   private String testdesc;//	STRING	服务器描述	服务器别称
   private String starttime;//	STRING	开服时间	时间戳
   private String status;//	INT	1 删档内测 2 不删档内测	测试描述

    public String getTestid() {
        return testid;
    }

    public void setTestid(String testid) {
        this.testid = testid;
    }

    public String getTestdesc() {
        return testdesc;
    }

    public void setTestdesc(String testdesc) {
        this.testdesc = testdesc;
    }

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
}
