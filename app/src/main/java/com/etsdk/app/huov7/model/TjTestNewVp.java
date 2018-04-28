package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by liu hong liang on 2017/1/12.
 */
public class TjTestNewVp {
    private List testGameList;
    private AdImage hometestgame;
    private List newServerList;
    private AdImage homenewserver;
    public TjTestNewVp(List newServerList, List testGameList) {
        this.newServerList = newServerList;
        this.testGameList = testGameList;
    }

    public List getTestGameList() {
        return testGameList;
    }

    public void setTestGameList(List testGameList) {
        this.testGameList = testGameList;
    }

    public List getNewServerList() {
        return newServerList;
    }

    public void setNewServerList(List newServerList) {
        this.newServerList = newServerList;
    }
}