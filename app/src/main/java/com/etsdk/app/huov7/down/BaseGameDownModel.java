package com.etsdk.app.huov7.down;

/**
 * Created by liu hong liang on 2017/1/10.
 */

public interface BaseGameDownModel {
    public String getUrl();
    public String getGameId();
    public String getGameName();
    public String getGameIcon();
    public void  setUrl(String url);
    public String getDowncnt();
    public void  setDowncnt(String downcnt);
    public int getId();
    public void  setId(int id);
    public String getGameType();
    public void setGameType(String gameType);
}
