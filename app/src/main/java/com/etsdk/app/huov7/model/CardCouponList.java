package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by liu hong liang on 2017/1/23.
 */

public class CardCouponList extends BaseModel{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean{
        private int count;//	INT	轮播图数量	5
        private List<CardCoupon>  list;//	JSON二维数组	轮播图列表

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<CardCoupon> getList() {
            return list;
        }

        public void setList(List<CardCoupon> list) {
            this.list = list;
        }
    }
}
