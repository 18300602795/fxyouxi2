package com.etsdk.app.huov7.model;

import android.content.Intent;

/**
 * Created by liu hong liang on 2016/12/5.
 * 首页推荐中的GridItem
 */

public class RecommandGridItem {
    private String titleName;
    private String description;
    private int drawableId;
    private Intent intent;

    public RecommandGridItem(String titleName, int drawableId, String description,Intent intent) {
        this.titleName = titleName;
        this.drawableId = drawableId;
        this.description = description;
        this.intent=intent;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }
}
