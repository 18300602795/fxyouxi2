package com.etsdk.app.huov7.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * Created by liu hong liang on 2016/11/15.
 */

public class QueryOrderRequestBean extends BaseRequestBean {
    private String order_id;//	是	STRING	查询的平台订单号

    public String getOrder_id() {
        return order_id;
    }
    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}
