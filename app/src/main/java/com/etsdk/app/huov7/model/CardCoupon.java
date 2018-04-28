package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/1/23.
 */
public class CardCoupon {
    private String name;//	STRING	轮播图名称	轮播图名称
    private String content;//s	STRING	图内容	轮播图内容 ID 或者URL

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}