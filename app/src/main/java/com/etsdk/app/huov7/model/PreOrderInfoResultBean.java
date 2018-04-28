package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by liu hong liang on 2017/2/24.
 */

public class PreOrderInfoResultBean {
    private String count;//	INT	可选择充值总数	充值总数
    private List<PayTypeData> list;//
    private String order_id;//	是	STRING	此次支付 平台订单ID
    private String real_amount;//	是	DOUBLE(10,2）	此次支付实际支付金额(单位: 元) 保留两位有效数字
    private String paytoken;//	是	STRING	对应支付的token

    public static class PayTypeData{
        private String paytype;//是	STRING	支付方式标号 【支付类型对应标识表】

        public String getPaytype() {
            return paytype;
        }

        public void setPaytype(String paytype) {
            this.paytype = paytype;
        }
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<PayTypeData> getList() {
        return list;
    }

    public void setList(List<PayTypeData> list) {
        this.list = list;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getReal_amount() {
        return real_amount;
    }

    public void setReal_amount(String real_amount) {
        this.real_amount = real_amount;
    }

    public String getPaytoken() {
        return paytoken;
    }

    public void setPaytoken(String paytoken) {
        this.paytoken = paytoken;
    }
}
