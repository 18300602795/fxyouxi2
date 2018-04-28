package com.etsdk.app.huov7.shop.model;

import java.util.List;

/**
 * Created by Administrator on 2017/7/1 0001.
 */

public class MyGameBean {

    /**
     * code : 200
     * msg : 请求成功
     * data : {"list":[{"id":2,"gamename":"王者荣耀","icon":"http://www.baidu.com/upload/20170622/594bc5745fc78.png"}],"count":234}
     */

    private String code;
    private String msg;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
        /**
         * list : [{"id":2,"gamename":"王者荣耀","icon":"http://www.baidu.com/upload/20170622/594bc5745fc78.png"}]
         * count : 234
         */

        private int count;
        private List<ListBean> list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 2
             * gamename : 王者荣耀
             * icon : http://www.baidu.com/upload/20170622/594bc5745fc78.png
             */

            private int id;
            private String gamename;
            private String icon;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getGamename() {
                return gamename;
            }

            public void setGamename(String gamename) {
                this.gamename = gamename;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }
        }
    }
}
