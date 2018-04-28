package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by liu hong liang on 2017/1/17.
 */

public class GiftCardBeanList {
    private GiftCardBeanList.DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int count;
        private List<GiftCard> list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<GiftCard> getList() {
            return list;
        }

        public void setList(List<GiftCard> list) {
            this.list = list;
        }
    }

}
