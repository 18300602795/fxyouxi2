package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by liu hong liang on 2017/1/18.
 * 开服游戏列表
 */

public class StartServerGameList {
    private StartServerGameList.DataBean data;

    public StartServerGameList.DataBean getData() {
        return data;
    }

    public void setData(StartServerGameList.DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int count;
        private List<StartServerGameBean> list;
        private List<StartServerGameBean> formerly;
        private List<StartServerGameBean> today;
        private List<StartServerGameBean> willbe;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<StartServerGameBean> getList() {
            return list;
        }

        public void setList(List<StartServerGameBean> list) {
            this.list = list;
        }

        public List<StartServerGameBean> getFormerly() {
            return formerly;
        }

        public void setFormerly(List<StartServerGameBean> formerly) {
            this.formerly = formerly;
        }

        public List<StartServerGameBean> getToday() {
            return today;
        }

        public void setToday(List<StartServerGameBean> today) {
            this.today = today;
        }

        public List<StartServerGameBean> getWillbe() {
            return willbe;
        }

        public void setWillbe(List<StartServerGameBean> willbe) {
            this.willbe = willbe;
        }
    }


}
