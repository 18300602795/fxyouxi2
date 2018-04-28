package com.etsdk.app.huov7.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * Created by liu hong liang on 2017/2/24.
 * 游戏支付提交数据
 */

public class GamePayRequestBean  extends BaseRequestBean{
    private String orderid;//是	STRING	订单ID
    private String paytoken;//	是	STRING	支付token,从下单时获取
    private String paytype;//是	FLOAT	支付方式【支付类型对应标识表】
    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getPaytoken() {
        return paytoken;
    }

    public void setPaytoken(String paytoken) {
        this.paytoken = paytoken;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }


}
