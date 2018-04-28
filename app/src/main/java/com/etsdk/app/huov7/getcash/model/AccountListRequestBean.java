package com.etsdk.app.huov7.getcash.model;

import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.domain.NotProguard;

/**
 * 提现账号列表
 * Created by Administrator on 2017/5/31 0031.
 */
@NotProguard
public class AccountListRequestBean extends BaseRequestBean {
    public String username;
    public int page;
    public int offset;
    public int isdefault;

    public int getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(int isdefault) {
        this.isdefault = isdefault;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
