package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by liu hong liang on 2017/1/21.
 * 福利顶部轮播图
 */
public class FuliTopAd extends AdImageKeyBean{
    private List<AdImage> datas;

    public FuliTopAd(List<AdImage> datas) {

        this.datas = datas;
    }
    public List<AdImage> getDatas() {
        return datas;
    }

    public void setDatas(List<AdImage> datas) {
        this.datas = datas;
    }


}