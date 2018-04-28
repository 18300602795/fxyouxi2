package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2016/12/21.
 */
public class AdImage {

    /**
     * name : 下载游戏领积分
     * type : 1
     * target : 0
     * url :
     * image : null
     * content :
     * desc :
     */

    private String name;
    private String type;
    private String target;
    private String url;
    private String image;
    private String content;
    private String desc;

    private boolean requestPadding =false;
    private boolean requestBottomMargin=false;

    public boolean isRequestBottomMargin() {
        return requestBottomMargin;
    }

    public void setRequestBottomMargin(boolean requestBottomMargin) {
        this.requestBottomMargin = requestBottomMargin;
    }

    public boolean isRequestPadding() {
        return requestPadding;
    }

    public void setRequestPadding(boolean requestPadding) {
        this.requestPadding = requestPadding;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public AdImage(String name) {
        this.name = name;
    }

    public AdImage() {
    }
}