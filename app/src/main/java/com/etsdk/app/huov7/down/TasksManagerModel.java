package com.etsdk.app.huov7.down;

import android.content.ContentValues;

/**
 * 任务下载模型
 */
public class TasksManagerModel implements BaseGameDownModel{
    public final static String ID = "id";//id由FileDownLoader生成
    public final static String GAME_SIZE = "gameSize";//游戏大小
    public final static String URL = "url";
    public final static String PATH = "path";
    public final static String GAME_ID="gameId";//游戏id
    public final static String GAME_NAME="gameName";//游戏名字
    public final static String GAME_ICON = "gameIcon";//游戏icon
    public final static String PACKAGE_NAME = "packageName";//包名
    public final static String ONLY_WIFI="onlyWifi";//下载时的网络类型仅仅是wifi
    public final static String USER_PAUSE="userPause";//用户暂停
    public final static String INSTALLED = "installed";//用户从这个平台安装过
    public final static String GAME_TYPE = "gameType";//游戏类型标签

    public boolean isSelected = false;//用于是否被选中的标记

    private int id;
    private String url;
    private String path;
    private String gameId;
    private String gameName;
    private String gameIcon;
    private String gameSize;
    private String packageName;
    private String gameType;
    private int onlyWifi=1;//默认只使用wifi下载
    private int userPause=0;//是否是用户暂停
    private int installed=0;//是否从这个盒子中安装过


    private String downcnt;//游戏下载次数
    private int speed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGameSize() {
        return gameSize;
    }

    public void setGameSize(String gameSize) {
        this.gameSize = gameSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getDowncnt() {
        return downcnt;
    }

    @Override
    public void setDowncnt(String downcnt) {
        this.downcnt =downcnt;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getInstalled() {
        return installed;
    }

    public void setInstalled(int installed) {
        this.installed = installed;
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

    public int getOnlyWifi() {
        return onlyWifi;
    }

    public void setOnlyWifi(int onlyWifi) {
        this.onlyWifi = onlyWifi;
    }

    public int getUserPause() {
        return userPause;
    }

    public void setUserPause(int userPause) {
        this.userPause = userPause;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setGameType(String type) {
        this.gameType = type;
    }

    public String getGameType() {
        return gameType;
    }

    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(ID, id);
        cv.put(URL, url);
        cv.put(PATH, path);
        cv.put(GAME_ID, gameId);
        cv.put(GAME_NAME, gameName);
        cv.put(GAME_ICON, gameIcon);
        cv.put(GAME_SIZE,gameSize);
        cv.put(PACKAGE_NAME, packageName);
        cv.put(ONLY_WIFI, onlyWifi);
        cv.put(USER_PAUSE, userPause);
        cv.put(INSTALLED, installed);
        cv.put(GAME_TYPE, gameType);
        return cv;
    }

    @Override
    public String toString() {
        return "TasksManagerModel{" +
                "gameName='" + gameName +
                '}';
    }
}