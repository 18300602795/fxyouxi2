package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/1/21.
 */

public class MainFuliAd {

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
        private AdImageKeyBean weallimitcoupon;
        private AdImageKeyBean wealluxurygift;
        private AdImageKeyBean wealtoday;
        private AdImageKeyBean wealtopper;

        public AdImageKeyBean getWeallimitcoupon() {
            return weallimitcoupon;
        }

        public void setWeallimitcoupon(AdImageKeyBean weallimitcoupon) {
            this.weallimitcoupon = weallimitcoupon;
        }

        public AdImageKeyBean getWealluxurygift() {
            return wealluxurygift;
        }

        public void setWealluxurygift(AdImageKeyBean wealluxurygift) {
            this.wealluxurygift = wealluxurygift;
        }

        public AdImageKeyBean getWealtoday() {
            return wealtoday;
        }

        public void setWealtoday(AdImageKeyBean wealtoday) {
            this.wealtoday = wealtoday;
        }

        public AdImageKeyBean getWealtopper() {
            return wealtopper;
        }

        public void setWealtopper(AdImageKeyBean wealtopper) {
            this.wealtopper = wealtopper;
        }
    }
}
