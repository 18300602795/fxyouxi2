package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * 猜你喜欢
 * Created by Administrator on 2017/4/27 0027.
 */

public class GamelikeBean {
    public List<GameBean> getGameBeanList() {
        return gameBeanList;
    }

    public void setGameBeanList(List<GameBean> gameBeanList) {
        this.gameBeanList = gameBeanList;
    }

    private List<GameBean> gameBeanList;
}
