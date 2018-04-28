package com.etsdk.app.huov7.shop.model;


import java.util.List;

/**
 * 交易账号详情
 */

public class ShopAccountDetailBean {

    /**
     * code : 200
     * msg : 请求成功
     * data : {"gameid":1,"gamename":"demo","icon":"/upload/20170606/59360f1974e7b.png","id":16,"servername":"啊啊啊","description":"歌碟呵呵呵","create_time":1499409194,"total_price":"55.00","title":"歌碟呵呵呵","status":4,"mem_id":77691,"image":["http://171static.1tsdk.com/upload/20170707/595f2b29d061c.jpeg","http://171static.1tsdk.com/upload/20170707/595f2b29ea433.jpeg","http://171static.1tsdk.com/upload/20170707/595f2b2a0f9d8.jpeg"],"seller_name":"jim110","is_self":1,"is_collect":2,"order":{"id":72,"order_id":"14994093169238776950001","sell_mem_id":77691,"buy_mem_id":77695,"account_deal_id":16,"app_id":1,"game_name":"demo","mg_mem_id":77691953,"server":"啊啊啊","title":"歌碟呵呵呵","description":"歌碟呵呵呵","price":"55.00","total_price":"0.01","real_price":"52.00","status":2,"create_time":1499409316,"payway":"alipay","pay_time":1499409331,"trade_no":"2017070721001004010213070780","is_sell":1,"is_buy":2}}
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
         * gameid : 1
         * gamename : demo
         * icon : /upload/20170606/59360f1974e7b.png
         * id : 16
         * servername : 啊啊啊
         * description : 歌碟呵呵呵
         * create_time : 1499409194
         * total_price : 55.00
         * title : 歌碟呵呵呵
         * status : 4
         * mem_id : 77691
         * image : ["http://171static.1tsdk.com/upload/20170707/595f2b29d061c.jpeg","http://171static.1tsdk.com/upload/20170707/595f2b29ea433.jpeg","http://171static.1tsdk.com/upload/20170707/595f2b2a0f9d8.jpeg"]
         * seller_name : jim110
         * is_self : 1
         * is_collect : 2
         * order : {"id":72,"order_id":"14994093169238776950001","sell_mem_id":77691,"buy_mem_id":77695,"account_deal_id":16,"app_id":1,"game_name":"demo","mg_mem_id":77691953,"server":"啊啊啊","title":"歌碟呵呵呵","description":"歌碟呵呵呵","price":"55.00","total_price":"0.01","real_price":"52.00","status":2,"create_time":1499409316,"payway":"alipay","pay_time":1499409331,"trade_no":"2017070721001004010213070780","is_sell":1,"is_buy":2}
         */

        private int gameid;
        private String gamename;
        private String icon;
        private int id;
        private String servername;
        private String description;
        private long create_time;
        private String total_price;
        private String title;
        private int status;
        private int mem_id;
        private String seller_name;
        private String mg_mem_id;
        private String nickname;
        private int is_self;
        private int is_collect;
        private OrderBean order;
        private List<String> image;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getMg_mem_id() {
            return mg_mem_id;
        }

        public void setMg_mem_id(String mg_mem_id) {
            this.mg_mem_id = mg_mem_id;
        }

        public int getGameid() {
            return gameid;
        }

        public void setGameid(int gameid) {
            this.gameid = gameid;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getTotal_price() {
            return total_price;
        }

        public void setTotal_price(String total_price) {
            this.total_price = total_price;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getMem_id() {
            return mem_id;
        }

        public void setMem_id(int mem_id) {
            this.mem_id = mem_id;
        }

        public String getSeller_name() {
            return seller_name;
        }

        public void setSeller_name(String seller_name) {
            this.seller_name = seller_name;
        }

        public int getIs_self() {
            return is_self;
        }

        public void setIs_self(int is_self) {
            this.is_self = is_self;
        }

        public int getIs_collect() {
            return is_collect;
        }

        public void setIs_collect(int is_collect) {
            this.is_collect = is_collect;
        }

        public OrderBean getOrder() {
            return order;
        }

        public void setOrder(OrderBean order) {
            this.order = order;
        }

        public List<String> getImage() {
            return image;
        }

        public void setImage(List<String> image) {
            this.image = image;
        }

        public static class OrderBean {
            /**
             * id : 72
             * order_id : 14994093169238776950001
             * sell_mem_id : 77691
             * buy_mem_id : 77695
             * account_deal_id : 16
             * app_id : 1
             * game_name : demo
             * mg_mem_id : 77691953
             * server : 啊啊啊
             * title : 歌碟呵呵呵
             * description : 歌碟呵呵呵
             * price : 55.00
             * total_price : 0.01
             * real_price : 52.00
             * status : 2
             * create_time : 1499409316
             * payway : alipay
             * pay_time : 1499409331
             * trade_no : 2017070721001004010213070780
             * is_sell : 1
             * is_buy : 2
             */

            private int id;
            private String order_id;
            private int sell_mem_id;
            private int buy_mem_id;
            private int account_deal_id;
            private int app_id;
            private String game_name;
            private int mg_mem_id;
            private String server;
            private String title;
            private String description;
            private String price;
            private String total_price;
            private String real_price;
            private int status;
            private int create_time;
            private String payway;
            private long pay_time;
            private String trade_no;
            private int is_sell;
            private int is_buy;

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

            public int getSell_mem_id() {
                return sell_mem_id;
            }

            public void setSell_mem_id(int sell_mem_id) {
                this.sell_mem_id = sell_mem_id;
            }

            public int getBuy_mem_id() {
                return buy_mem_id;
            }

            public void setBuy_mem_id(int buy_mem_id) {
                this.buy_mem_id = buy_mem_id;
            }

            public int getAccount_deal_id() {
                return account_deal_id;
            }

            public void setAccount_deal_id(int account_deal_id) {
                this.account_deal_id = account_deal_id;
            }

            public int getApp_id() {
                return app_id;
            }

            public void setApp_id(int app_id) {
                this.app_id = app_id;
            }

            public String getGame_name() {
                return game_name;
            }

            public void setGame_name(String game_name) {
                this.game_name = game_name;
            }

            public int getMg_mem_id() {
                return mg_mem_id;
            }

            public void setMg_mem_id(int mg_mem_id) {
                this.mg_mem_id = mg_mem_id;
            }

            public String getServer() {
                return server;
            }

            public void setServer(String server) {
                this.server = server;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getTotal_price() {
                return total_price;
            }

            public void setTotal_price(String total_price) {
                this.total_price = total_price;
            }

            public String getReal_price() {
                return real_price;
            }

            public void setReal_price(String real_price) {
                this.real_price = real_price;
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

            public String getPayway() {
                return payway;
            }

            public void setPayway(String payway) {
                this.payway = payway;
            }

            public long getPay_time() {
                return pay_time;
            }

            public void setPay_time(long pay_time) {
                this.pay_time = pay_time;
            }

            public String getTrade_no() {
                return trade_no;
            }

            public void setTrade_no(String trade_no) {
                this.trade_no = trade_no;
            }

            public int getIs_sell() {
                return is_sell;
            }

            public void setIs_sell(int is_sell) {
                this.is_sell = is_sell;
            }

            public int getIs_buy() {
                return is_buy;
            }

            public void setIs_buy(int is_buy) {
                this.is_buy = is_buy;
            }
        }
    }
}
