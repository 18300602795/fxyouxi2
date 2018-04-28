package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public class SpendRecordListBean {

    /**
     * code : 200
     * msg : 请求成功
     * data : {"count":5,"list":[{"id":97,"order_id":"1493975570655810001","mem_id":77713,"agent_id":946,"app_id":100,"amount":100.99,"ptb_cnt":101,"from":1,"status":2,"create_time":1493975570,"update_time":1493975570},{"id":96,"order_id":"1493975517516970001","mem_id":77713,"agent_id":946,"app_id":100,"amount":100.99,"ptb_cnt":101,"from":1,"status":2,"create_time":1493975517,"update_time":1493975517},{"id":95,"order_id":"1493975317354150001","mem_id":77713,"agent_id":946,"app_id":100,"amount":100.99,"ptb_cnt":101,"from":1,"status":2,"create_time":1493975317,"update_time":1493975317},{"id":94,"order_id":"1493966313143780001","mem_id":77713,"agent_id":946,"app_id":100,"amount":100.99,"ptb_cnt":101,"from":1,"status":2,"create_time":1493966313,"update_time":1493966313},{"id":93,"order_id":"1493966213123110001","mem_id":77713,"agent_id":946,"app_id":100,"amount":100.99,"ptb_cnt":101,"from":1,"status":2,"create_time":1493966213,"update_time":1493966213}]}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
         * count : 5
         * list : [{"id":97,"order_id":"1493975570655810001","mem_id":77713,"agent_id":946,"app_id":100,"amount":100.99,"ptb_cnt":101,"from":1,"status":2,"create_time":1493975570,"update_time":1493975570},{"id":96,"order_id":"1493975517516970001","mem_id":77713,"agent_id":946,"app_id":100,"amount":100.99,"ptb_cnt":101,"from":1,"status":2,"create_time":1493975517,"update_time":1493975517},{"id":95,"order_id":"1493975317354150001","mem_id":77713,"agent_id":946,"app_id":100,"amount":100.99,"ptb_cnt":101,"from":1,"status":2,"create_time":1493975317,"update_time":1493975317},{"id":94,"order_id":"1493966313143780001","mem_id":77713,"agent_id":946,"app_id":100,"amount":100.99,"ptb_cnt":101,"from":1,"status":2,"create_time":1493966313,"update_time":1493966313},{"id":93,"order_id":"1493966213123110001","mem_id":77713,"agent_id":946,"app_id":100,"amount":100.99,"ptb_cnt":101,"from":1,"status":2,"create_time":1493966213,"update_time":1493966213}]
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
             * id : 97
             * order_id : 1493975570655810001
             * mem_id : 77713
             * agent_id : 946
             * app_id : 100
             * amount : 100.99
             * ptb_cnt : 101
             * from : 1
             * status : 2
             * create_time : 1493975570
             * update_time : 1493975570
             */

            private int id;
            private String order_id;
            private int mem_id;
            private int agent_id;
            private int app_id;
            private double amount;
            private double ptb_cnt;
            private int from;
            private int status;
            private int create_time;
            private int update_time;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getOrder_id() {
                return order_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public int getMem_id() {
                return mem_id;
            }

            public void setMem_id(int mem_id) {
                this.mem_id = mem_id;
            }

            public int getAgent_id() {
                return agent_id;
            }

            public void setAgent_id(int agent_id) {
                this.agent_id = agent_id;
            }

            public int getApp_id() {
                return app_id;
            }

            public void setApp_id(int app_id) {
                this.app_id = app_id;
            }

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public double getPtb_cnt() {
                return ptb_cnt;
            }

            public void setPtb_cnt(double ptb_cnt) {
                this.ptb_cnt = ptb_cnt;
            }

            public int getFrom() {
                return from;
            }

            public void setFrom(int from) {
                this.from = from;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getCreate_time() {
                return create_time;
            }

            public void setCreate_time(int create_time) {
                this.create_time = create_time;
            }

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }
        }
    }
}
