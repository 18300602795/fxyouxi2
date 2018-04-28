package com.etsdk.app.huov7.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * Created by liu hong liang on 2017/3/23.
 */

public class MessageDeleteRequestBean extends BaseRequestBean {
    private String msgid;//	是	STRING	消息ID

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }
}
