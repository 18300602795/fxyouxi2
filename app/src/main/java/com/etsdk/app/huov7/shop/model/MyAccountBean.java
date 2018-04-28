package com.etsdk.app.huov7.shop.model;

import java.util.List;

/**
 * Created by Administrator on 2017/7/1 0001.
 */

public class MyAccountBean {

    /**
     * code : 200
     * msg : 请求成功
     * data : {"list":[{"id":2,"rolename":"boom","level":199}],"count":234}
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
         * list : [{"id":2,"rolename":"boom","level":199}]
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
             * rolename : boom
             * level : 199
             */

            private int id;
            private int mg_mem_id;
            private String rolename;
            private String nickname;
            private int level;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getRolename() {
                return rolename;
            }

            public void setRolename(String rolename) {
                this.rolename = rolename;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public int getMg_mem_id() {
                return mg_mem_id;
            }

            public void setMg_mem_id(int mg_mem_id) {
                this.mg_mem_id = mg_mem_id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }
        }
    }
}
