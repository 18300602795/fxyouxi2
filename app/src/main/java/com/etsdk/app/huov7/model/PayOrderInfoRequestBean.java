package com.etsdk.app.huov7.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * Created by liu hong liang on 2017/2/24.
 * 请求下单信息bean
 */

public class PayOrderInfoRequestBean extends BaseRequestBean{
    private OrderInfo orderinfo;//	是	JSON数组	订单信息
    public static class OrderInfo{
        private String money;//	是	FLOAT	玩家充值游戏币金额;建议传入整数,可以保留两位小数
        private String real_money;//	是	FLOAT	玩家实际支付金额
        private String gamemoney;//	是	FLOAT	玩家可获得的游戏币
        private String integral;//	是	FLOAT	玩家可获得的积分
        private String couponcontent;//	是	STRING	代金卷闲情 ID:CNT,ID:CNT

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getReal_money() {
            return real_money;
        }

        public void setReal_money(String real_money) {
            this.real_money = real_money;
        }

        public String getGamemoney() {
            return gamemoney;
        }

        public void setGamemoney(String gamemoney) {
            this.gamemoney = gamemoney;
        }

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getCouponcontent() {
            return couponcontent;
        }

        public void setCouponcontent(String couponcontent) {
            this.couponcontent = couponcontent;
        }
    }

    public OrderInfo getOrderinfo() {
        return orderinfo;
    }

    public void setOrderinfo(OrderInfo orderinfo) {
        this.orderinfo = orderinfo;
    }
}
