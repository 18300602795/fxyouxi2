package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * 游戏代金券，对应的游戏币换来的
 * 2017/5/18.
 */

public class CouponBeanListV2 extends BaseModel{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int count;
        private List<CouponListItemV2> list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<CouponListItemV2> getList() {
            return list;
        }

        public void setList(List<CouponListItemV2> list) {
            this.list = list;
        }
    }
}
