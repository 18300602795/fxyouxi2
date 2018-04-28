package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/2/28.
 * 消息读取事件
 */

public class MessageEvent {
    private String newMsg;	//2 有 1 没有 空 没有

    public MessageEvent(String newMsg) {
        this.newMsg = newMsg;
    }

    public String getNewMsg() {
        return newMsg;
    }

    public void setNewMsg(String newMsg) {
        this.newMsg = newMsg;
    }
}
