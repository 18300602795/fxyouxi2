package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/1/5.
 */
public class GiftListItem {
    private String giftid	;//INT	礼包ID	12
    private String gameid	;//INT	礼包对应游戏ID	123
    private String giftname	;//STRING	礼包标题	【我叫mt】新手礼包
    private int total	;//INT	礼包总数	10
    private int remain	;//INT	剩余数量	2
    private String content	;//STRING	礼包内容	新手礼包11111
    private String icon	 ;//礼包logo	默认取游戏logo
    private String starttime;//	STRING	开始时间	2016-08-06 12:00:00
    private String enttime	;//STRING	结束时间	2016-08-16 12:00:00
    private String isget	;//INT	是否已领取	1 表示未领取 2 表示已领取过
    private String giftcode	;//STRING	若领取，此字段有用，礼包码	sjenf
    private String scope	;//STRING	使用范围	全服通用
    private String func;//	STRING	使用方法	使用方法

    public String getGiftid() {
        return giftid;
    }

    public void setGiftid(String giftid) {
        this.giftid = giftid;
    }

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public String getGiftname() {
        return giftname;
    }

    public void setGiftname(String giftname) {
        this.giftname = giftname;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEnttime() {
        return enttime;
    }

    public void setEnttime(String enttime) {
        this.enttime = enttime;
    }

    public String getIsget() {
        return isget;
    }

    public void setIsget(String isget) {
        this.isget = isget;
    }

    public String getGiftcode() {
        return giftcode;
    }

    public void setGiftcode(String giftcode) {
        this.giftcode = giftcode;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }
}