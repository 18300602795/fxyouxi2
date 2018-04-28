package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by liu hong liang on 2017/1/18.
 * 开服游戏列表
 */

public class TestGameList {
    private TestGameList.DataBean data;

    public TestGameList.DataBean getData() {
        return data;
    }

    public void setData(TestGameList.DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int count;
        private List<TestGameBean> formerly;
        private List<TestGameBean> today;
        private List<TestGameBean> willbe;
        private List<TestGameBean> list;

        public List<TestGameBean> getFormerly() {
            return formerly;
        }

        public void setFormerly(List<TestGameBean> formerly) {
            this.formerly = formerly;
        }

        public List<TestGameBean> getToday() {
            return today;
        }

        public void setToday(List<TestGameBean> today) {
            this.today = today;
        }

        public List<TestGameBean> getWillbe() {
            return willbe;
        }

        public void setWillbe(List<TestGameBean> willbe) {
            this.willbe = willbe;
        }

        public List<TestGameBean> getList() {
            return list;
        }

        public void setList(List<TestGameBean> list) {
            this.list = list;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }


}
