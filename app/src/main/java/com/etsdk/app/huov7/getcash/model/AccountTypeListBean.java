package com.etsdk.app.huov7.getcash.model;

import com.game.sdk.domain.NotProguard;

import java.util.List;

/**
 * Created by Administrator on 2017/5/31 0031.
 */
@NotProguard
public class AccountTypeListBean {

    /**
     * code : 200
     * msg : 请求成功
     * data : {"count":1,"type_list":[{"id":1,"paytype":"alipay","name":"支付宝"}]}
     */


        private int count;
        private List<TypeListBean> type_list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<TypeListBean> getType_list() {
            return type_list;
        }

        public void setType_list(List<TypeListBean> type_list) {
            this.type_list = type_list;
        }
        @NotProguard
        public static class TypeListBean {
            /**
             * id : 1
             * payname : alipay
             * name : 支付宝
             */

            private int id;
            private String name;
            private String payname;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPayname() {
                return payname;
            }

            public void setPayname(String payname) {
                this.payname = payname;
            }
        }
}
