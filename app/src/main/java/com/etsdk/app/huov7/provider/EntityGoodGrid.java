package com.etsdk.app.huov7.provider;


import com.etsdk.app.huov7.model.Goods;

import java.util.List;

/**
 * Created by liu hong liang on 2017/3/10.
 */
public class EntityGoodGrid {
    private List<Goods> goodsList;

    public EntityGoodGrid(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }
}