package com.etsdk.app.huov7.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * Created by liu hong liang on 2017/1/17.
 * 意见反馈
 */

public class FeedbackRequestBean extends BaseRequestBean {
    private String content;//	是	STRING	反馈内容
    private String linkman;//	是	STRING	联系方式
    private String gameid;//	否	STRING	关联游戏ID
    private String gamecontent;//	否	STRING	更新 gamecontent 为反馈标题

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public String getGamecontent() {
        return gamecontent;
    }

    public void setGamecontent(String gamecontent) {
        this.gamecontent = gamecontent;
    }
}
