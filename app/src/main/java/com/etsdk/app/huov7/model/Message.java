package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/1/16.
 */
public class Message {
   private String msgid	;//INT	代金卷ID	12
   private String title	;//STRING	消息标题	消息标题
   private String content;//	STRING	消息内容	消息内容
   private String gameid;//	INT	是否是游戏消息 0 不是 非1表示是游戏消息	游戏ID
   private String readed;//	INT	2 表示已读 1 表示未度	已读标志
   private String type	;//list.type	INT	2 系统消息,1活动消息 3 卡卷消息 4优惠活动

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public String getReaded() {
        return readed;
    }

    public void setReaded(String readed) {
        this.readed = readed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}