package com.etsdk.app.huov7.model;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * Created by liu hong liang on 2016/9/23.
 */

public class TabEntity implements CustomTabEntity {
    private String tabTitle;
    private int tabSelectIconId;
    private int tabUnSelectIconId;

    public TabEntity(String tabTitle, int tabSelectIconId, int tabUnSelectIconId) {
        this.tabTitle = tabTitle;
        this.tabSelectIconId = tabSelectIconId;
        this.tabUnSelectIconId = tabUnSelectIconId;
    }

    @Override
    public String getTabTitle() {
        return tabTitle;
    }

    @Override
    public int getTabSelectedIcon() {
        return tabSelectIconId;
    }

    @Override
    public int getTabUnselectedIcon() {
        return tabUnSelectIconId;
    }
}
