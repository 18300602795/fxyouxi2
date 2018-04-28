package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by liu hong liang on 2016/12/21.
 * 推荐轮播文字
 */
public class TjAdText {
    private List<AdImage> adTextList;

    public TjAdText(List<AdImage> adTextList) {
        this.adTextList = adTextList;
    }

    public List<AdImage> getAdTextList() {
        return adTextList;
    }

    public void setAdTextList(List<AdImage> adTextList) {
        this.adTextList = adTextList;
    }
}