package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by liu hong liang on 2017/1/15.
 */

public class GameBeanList extends BaseModel{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int count;
        private List<GameBean> list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<GameBean> getList() {
            return list;
        }

        public void setList(List<GameBean> list) {
            this.list = list;
        }
    }

    /**
     * 测试表新服表专用带礼包点击按钮
     */
    public static class GameGiftBean{
        private int count;
        private List<GameGiftItem> list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<GameGiftItem> getList() {
            return list;
        }

        public void setList(List<GameGiftItem> list) {
            this.list = list;
        }
    }
}
