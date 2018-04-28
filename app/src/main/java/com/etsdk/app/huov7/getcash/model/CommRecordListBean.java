package com.etsdk.app.huov7.getcash.model;

import java.util.List;

/**
 * 商品记录、分享用户记录、收入记录通用
 * Created by Administrator on 2017/5/31 .
 */

public class CommRecordListBean {

    private int count;
    private List<DataBean> list;//收入记录、提现记录
    private List<DataBean> product_list;//商品列表
    private List<DataBean> share_list;//分享用户列表

    public List<DataBean> getShare_list() {
        return share_list;
    }

    public void setShare_list(List<DataBean> share_list) {
        this.share_list = share_list;
    }

    public List<DataBean> getProduct_list() {
        return product_list;
    }

    public void setProduct_list(List<DataBean> product_list) {
        this.product_list = product_list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<DataBean> getList() {
        return list;
    }

    public void setList(List<DataBean> list) {
        this.list = list;
    }

    public static class DataBean {

        private int gameid;
        private int id;
        private int product_id;
        private int product_count;
        private int relevance_game_id;
        private long create_time;
        private float price;
        private float total_price;
        private String gamename;
        private String game_username;
        private String remark;
        private String product_name;
        private String order_id;
        private String relevance_game_name;
        private String paytype;
        private String status;
        private String orderid;
        private String pay_time;
        private String time;
        private String username;
        private float amount;
        private float ptb;
        //提现记录
        private String cash_id;
        private String card;
        private long check_time;
        private String failreason;
        private String money;
        private String way_name;
        //积分收入记录
        private double integral;//积分
        private int is_invit;//1是自己   2是邀请

        public double getIntegral() {
            return integral;
        }

        public void setIntegral(double integral) {
            this.integral = integral;
        }

        public int getIs_invit() {
            return is_invit;
        }

        public void setIs_invit(int is_invit) {
            this.is_invit = is_invit;
        }

        public String getCash_id() {
            return cash_id;
        }

        public void setCash_id(String cash_id) {
            this.cash_id = cash_id;
        }

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }

        public long getCheck_time() {
            return check_time;
        }

        public void setCheck_time(long check_time) {
            this.check_time = check_time;
        }

        public String getFailreason() {
            return failreason;
        }

        public void setFailreason(String failreason) {
            this.failreason = failreason;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getWay_name() {
            return way_name;
        }

        public void setWay_name(String way_name) {
            this.way_name = way_name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public int getProduct_count() {
            return product_count;
        }

        public void setProduct_count(int product_count) {
            this.product_count = product_count;
        }

        public int getRelevance_game_id() {
            return relevance_game_id;
        }

        public void setRelevance_game_id(int relevance_game_id) {
            this.relevance_game_id = relevance_game_id;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public float getTotal_price() {
            return total_price;
        }

        public void setTotal_price(float total_price) {
            this.total_price = total_price;
        }

        public String getGame_username() {
            return game_username;
        }

        public void setGame_username(String game_username) {
            this.game_username = game_username;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getRelevance_game_name() {
            return relevance_game_name;
        }

        public void setRelevance_game_name(String relevance_game_name) {
            this.relevance_game_name = relevance_game_name;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public float getPtb() {
            return ptb;
        }

        public void setPtb(float ptb) {
            this.ptb = ptb;
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

        public String getPaytype() {
            return paytype;
        }

        public void setPaytype(String paytype) {
            this.paytype = paytype;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public float getAmount() {
            return amount;
        }

        public void setAmount(float amount) {
            this.amount = amount;
        }
    }
}
