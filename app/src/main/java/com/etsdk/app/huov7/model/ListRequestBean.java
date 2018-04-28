package com.etsdk.app.huov7.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * Created by liu hong liang on 2017/3/23.
 * 内部联网请求列表
 */

public class ListRequestBean extends BaseRequestBean {
   private String page	;//否	INT	页码 默认为1 代表第一页
   private String offset;//	否	INT	每页显示数量 默认为10

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }
}
