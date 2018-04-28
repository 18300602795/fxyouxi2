package com.etsdk.app.huov7.getcash.model;

import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.domain.NotProguard;

/**
 * 删除提现账号
 * Created by Administrator on 2017/5/31 0031.
 */
@NotProguard
public class DeleteAccountRequestBean extends BaseRequestBean {
    public int infoid;

    public int getInfoid() {
        return infoid;
    }

    public void setInfoid(int infoid) {
        this.infoid = infoid;
    }
}
