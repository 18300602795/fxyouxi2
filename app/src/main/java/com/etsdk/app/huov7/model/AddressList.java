package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by liu hong liang on 2017/2/7.
 */

public class AddressList extends BaseModel {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean{
        private int count;
        private List<AddressInfo> list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<AddressInfo> getList() {
            return list;
        }

        public void setList(List<AddressInfo> list) {
            this.list = list;
        }
    }
}
