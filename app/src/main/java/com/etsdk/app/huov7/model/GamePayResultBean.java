package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by liu hong liang on 2017/2/24.
 */

public class GamePayResultBean {
    private Float rate;//	FLAOT	与游戏币比例 10表示 1RMB: 10YXB	10
    private String maxcoupon;//		FLOAT	可使用最大代金卷数量
    private String integral;//	FLOAT	可获得积分	可获得积分
    private Integer count;//	INT	代金卷类别总数	代金卷类别总数
    private List<CouponPay> list;// 	二维数组	代金卷列表

    public Float getRate() {
        return rate;
    }

    public String getMaxcoupon() {
        return maxcoupon;
    }

    public void setMaxcoupon(String maxcoupon) {
        this.maxcoupon = maxcoupon;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public List<CouponPay> getList() {
        return list;
    }

    public void setList(List<CouponPay> list) {
        this.list = list;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }
}
