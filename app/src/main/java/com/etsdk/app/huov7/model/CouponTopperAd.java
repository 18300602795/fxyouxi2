package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/1/21.
 */

public class CouponTopperAd {

    /**
     * code : 200
     * msg : 请求成功
     * data : {"weallimitcoupon":null,"wealluxurygift":null,"wealtoday":null,"wealtopper":null}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private AdImageKeyBean coupontopper;

        public AdImageKeyBean getCoupontopper() {
            return coupontopper;
        }

        public void setCoupontopper(AdImageKeyBean coupontopper) {
            this.coupontopper = coupontopper;
        }
    }
}
