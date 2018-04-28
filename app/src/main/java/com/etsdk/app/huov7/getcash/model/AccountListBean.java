package com.etsdk.app.huov7.getcash.model;

import com.game.sdk.domain.NotProguard;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/5/31 0031.
 */
@NotProguard
public class AccountListBean {

        /**
         * count : 1
         * cash_list : [{"id":1,"paytype":"alipay","bankname":"中国银行","curbank":"广州支行","account":"2233333","cardname":"张三","isdefault":0}]
         */

        private int count;
        private List<CashListBean> list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<CashListBean> getCash_list() {
            return list;
        }

        public void setCash_list(List<CashListBean> cash_list) {
            this.list = cash_list;
        }
        @NotProguard
        public static class CashListBean implements Serializable {
            /**
             * id : 1
             * paytype : alipay
             * bankname : 中国银行
             * curbank : 广州支行
             * account : 2233333
             * cardname : 张三
             * isdefault : 0
             */

            private int id;
            private String paytype;
            private String bankname;
            private String curbank;
            private String account;
            private String cardname;
            private int isdefault;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getPaytype() {
                return paytype;
            }

            public void setPaytype(String paytype) {
                this.paytype = paytype;
            }

            public String getBankname() {
                return bankname;
            }

            public void setBankname(String bankname) {
                this.bankname = bankname;
            }

            public String getCurbank() {
                return curbank;
            }

            public void setCurbank(String curbank) {
                this.curbank = curbank;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public String getCardname() {
                return cardname;
            }

            public void setCardname(String cardname) {
                this.cardname = cardname;
            }

            public int getIsdefault() {
                return isdefault;
            }

            public void setIsdefault(int isdefault) {
                this.isdefault = isdefault;
            }
        }
}
