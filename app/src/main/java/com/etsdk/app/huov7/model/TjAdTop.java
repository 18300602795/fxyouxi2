package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by liu hong liang on 2016/12/21.
 */
public class TjAdTop {
    private List<AdImage> bannerList;

    public TjAdTop(List<AdImage> bannerList) {
        this.bannerList = bannerList;
    }

    public List<AdImage> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<AdImage> bannerList) {
        this.bannerList = bannerList;
    }
}