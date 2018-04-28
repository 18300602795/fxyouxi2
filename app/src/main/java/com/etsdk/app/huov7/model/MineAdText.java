package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/1/21.
 */
public class MineAdText {

    /**
     * code : 200
     * msg : 请求成功
     * data : {"useract":{"count":1,"list":[{"name":"今天可领取100积分","type":1,"target":0,"url":"","image":null,"content":"","desc":""}]},"userearn":{"count":1,"list":[{"name":"下载游戏领积分","type":1,"target":0,"url":"","image":null,"content":"","desc":""}]},"usermygame":{"count":1,"list":[{"name":"游戏币余额测试","type":1,"target":0,"url":"","image":null,"content":"","desc":""}]},"usermysign":{"count":1,"list":[{"name":"今天可领取50火速币","type":1,"target":0,"url":"","image":null,"content":"","desc":""}]},"userrank":{"count":1,"list":[{"name":"会当凌绝顶","type":1,"target":0,"url":"","image":null,"content":"","desc":""}]},"usershop":{"count":1,"list":[{"name":"代金券充值卡","type":1,"target":0,"url":"","image":null,"content":"","desc":""}]}}
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
         * useract : {"count":1,"list":[{"name":"今天可领取100积分","type":1,"target":0,"url":"","image":null,"content":"","desc":""}]}
         * userearn : {"count":1,"list":[{"name":"下载游戏领积分","type":1,"target":0,"url":"","image":null,"content":"","desc":""}]}
         * usermygame : {"count":1,"list":[{"name":"游戏币余额测试","type":1,"target":0,"url":"","image":null,"content":"","desc":""}]}
         * usermysign : {"count":1,"list":[{"name":"今天可领取50火速币","type":1,"target":0,"url":"","image":null,"content":"","desc":""}]}
         * userrank : {"count":1,"list":[{"name":"会当凌绝顶","type":1,"target":0,"url":"","image":null,"content":"","desc":""}]}
         * usershop : {"count":1,"list":[{"name":"代金券充值卡","type":1,"target":0,"url":"","image":null,"content":"","desc":""}]}
         */

        private AdImageKeyBean useract;
        private AdImageKeyBean userearn;
        private AdImageKeyBean usermygame;
        private AdImageKeyBean usermysign;
        private AdImageKeyBean userrank;
        private AdImageKeyBean usershop;

        public AdImageKeyBean getUsershop() {
            return usershop;
        }

        public void setUsershop(AdImageKeyBean usershop) {
            this.usershop = usershop;
        }

        public AdImageKeyBean getUseract() {
            return useract;
        }

        public void setUseract(AdImageKeyBean useract) {
            this.useract = useract;
        }

        public AdImageKeyBean getUserearn() {
            return userearn;
        }

        public void setUserearn(AdImageKeyBean userearn) {
            this.userearn = userearn;
        }

        public AdImageKeyBean getUsermygame() {
            return usermygame;
        }

        public void setUsermygame(AdImageKeyBean usermygame) {
            this.usermygame = usermygame;
        }

        public AdImageKeyBean getUsermysign() {
            return usermysign;
        }

        public void setUsermysign(AdImageKeyBean usermysign) {
            this.usermysign = usermysign;
        }

        public AdImageKeyBean getUserrank() {
            return userrank;
        }

        public void setUserrank(AdImageKeyBean userrank) {
            this.userrank = userrank;
        }
    }
}