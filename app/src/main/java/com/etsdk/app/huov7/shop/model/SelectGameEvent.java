package com.etsdk.app.huov7.shop.model;

/**
 * Created by Administrator on 2017/6/27 0027.
 */

public class SelectGameEvent {
    String gameId;
    String gameName;
    String gameIcon;

    public SelectGameEvent(String gameId, String gameName) {
        this.gameId = gameId;
        this.gameName = gameName;
    }

    public SelectGameEvent(String gameId, String gameName, String gameIcon) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.gameIcon = gameIcon;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameIcon() {
        return gameIcon;
    }

    public void setGameIcon(String gameIcon) {
        this.gameIcon = gameIcon;
    }
}
