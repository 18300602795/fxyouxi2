package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/1/10.
 * 轮播图
 */

public class Banner {
    private String url;
    private String type;
    private String gameId;

    public Banner(String url, String type, String gameId) {
        this.url = url;
        this.type = type;
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
