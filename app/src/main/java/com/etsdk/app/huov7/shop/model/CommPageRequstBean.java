package com.etsdk.app.huov7.shop.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * 通用分页请求
 * 2017/5/16.
 */

public class CommPageRequstBean extends BaseRequestBean {
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
