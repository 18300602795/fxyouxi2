package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by liu hong liang on 2017/1/15.
 */

public class GiftBeanList extends BaseModel{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int count;
        private List<GiftListItem> list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<GiftListItem> getList() {
            return list;
        }

        public void setList(List<GiftListItem> list) {
            this.list = list;
        }
    }
}
