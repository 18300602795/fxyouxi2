package com.etsdk.app.huov7.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * 充值、消费记录都可以用
 * 2017/5/16.
 */

public class ChargeRrcordListRequestBean extends BaseRequestBean {
    private int page = 1;
    private int offset = 10;

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
