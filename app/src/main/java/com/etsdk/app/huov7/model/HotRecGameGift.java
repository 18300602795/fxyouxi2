package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by liu hong liang on 2017/1/22.
 * 福利礼包热门推荐礼包
 */
public class HotRecGameGift {
    private List<GameBean> datas;

    public HotRecGameGift(List<GameBean> datas) {
        this.datas = datas;
    }

    public List<GameBean> getDatas() {
        return datas;
    }

    public void setDatas(List<GameBean> datas) {
        this.datas = datas;
    }
}