package com.etsdk.app.huov7.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * Created by liu hong liang on 2017/1/22.
 */

public class MineGoodsRequestBean extends BaseRequestBean{
    private String is_real;//	是	INT	1 表示虚拟物品 2 表示实物

    public String getIs_real() {
        return is_real;
    }

    public void setIs_real(String is_real) {
        this.is_real = is_real;
    }
}
