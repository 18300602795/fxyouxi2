package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/1/13.
 */
public class CouponPay extends CouponListItem{
    private int selectCount;//用户选择使用的代金券数量

    public int getSelectCount() {
        return selectCount;
    }

    public void setSelectCount(int selectCount) {
        this.selectCount = selectCount;
    }
}