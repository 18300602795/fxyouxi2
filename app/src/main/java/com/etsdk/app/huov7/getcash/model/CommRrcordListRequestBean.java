package com.etsdk.app.huov7.getcash.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * 充值、消费记录等，都可以用
 * 2017/5/16.
 */

public class CommRrcordListRequestBean extends BaseRequestBean {
    private int page = 1;
    private int offset = 10;
    private int type = 1;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
