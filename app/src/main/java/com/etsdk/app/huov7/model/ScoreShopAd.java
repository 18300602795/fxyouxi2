package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/1/21.
 */

public class ScoreShopAd {

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
        private AdImageKeyBean shoptopper;
        private AdImageKeyBean shopcoupon;
        private AdImageKeyBean shopgiftcard;

        public AdImageKeyBean getShoptopper() {
            return shoptopper;
        }

        public void setShoptopper(AdImageKeyBean shoptopper) {
            this.shoptopper = shoptopper;
        }

        public AdImageKeyBean getShopgiftcard() {
            return shopgiftcard;
        }

        public void setShopgiftcard(AdImageKeyBean shopgiftcard) {
            this.shopgiftcard = shopgiftcard;
        }

        public AdImageKeyBean getShopcoupon() {
            return shopcoupon;
        }

        public void setShopcoupon(AdImageKeyBean shopcoupon) {
            this.shopcoupon = shopcoupon;
        }
    }
}
