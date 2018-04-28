package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/1/18.
 */
public class GameTestNewTab {
    private boolean isStartServer;

    public GameTestNewTab(boolean isStartServer) {
        this.isStartServer = isStartServer;
    }

    public boolean isStartServer() {
        return isStartServer;
    }

    public void setStartServer(boolean startServer) {
        isStartServer = startServer;
    }
}