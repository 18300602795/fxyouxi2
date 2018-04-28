package com.etsdk.app.huov7.shop.model;

import java.util.List;

/**
 * Created by Administrator on 2017/6/14 0014.
 */

public class ShopListBean {

    /**
     * code : 200
     * msg : 请求成功
     * data : {"list":[{"id":2,"title":"155级号便宜卖","gamename":"王者荣耀","icon":"http://www.baidu.com/upload/20170622/594bc5745fc78.png","servername":"穿越26服(新服)","description":"描述描述","create_time":1498492493,"total_price":100.2,"seller_name":"boom理想","status":1}],"count":234}
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
         * list : [{"id":2,"title":"155级号便宜卖","gamename":"王者荣耀","icon":"http://www.baidu.com/upload/20170622/594bc5745fc78.png","servername":"穿越26服(新服)","description":"描述描述","create_time":1498492493,"total_price":100.2,"seller_name":"boom理想","status":1}]
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
             * title : 155级号便宜卖
             * gamename : 王者荣耀
             * icon : http://www.baidu.com/upload/20170622/594bc5745fc78.png
             * servername : 穿越26服(新服)
             * description : 描述描述
             * create_time : 1498492493
             * total_price : 100.2
             * seller_name : boom理想
             * status : 1
             */

            private int id;
            private int account_deal_id;//已交易就有交易id
            private String title;
            private String gamename;
            private String icon;
            private String servername;
            private String description;
            private long create_time;
            private long pay_time;
            private double total_price;
            private String seller_name;
            private String order_id;
            private int status;
            private int is_self;

            public int getIs_self() {
                return is_self;
            }

            public void setIs_self(int is_self) {
                this.is_self = is_self;
            }

            public int getAccount_deal_id() {
                return account_deal_id;
            }

            public void setAccount_deal_id(int account_deal_id) {
                this.account_deal_id = account_deal_id;
            }

            public String getOrder_id() {
                return order_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public long getPay_time() {
                return pay_time;
            }

            public void setPay_time(long pay_time) {
                this.pay_time = pay_time;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
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

            public String getServername() {
                return servername;
            }

            public void setServername(String servername) {
                this.servername = servername;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public long getCreate_time() {
                return create_time;
            }

            public void setCreate_time(long create_time) {
                this.create_time = create_time;
            }

            public double getTotal_price() {
                return total_price;
            }

            public void setTotal_price(double total_price) {
                this.total_price = total_price;
            }

            public String getSeller_name() {
                return seller_name;
            }

            public void setSeller_name(String seller_name) {
                this.seller_name = seller_name;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
