package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/1/21.
 */

public class HomePage1Data extends BaseModel{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean{
        private AdImageKeyBean homehotgame;//	广告图片对象	手游风向标广告图	详见 轮播图、广告图
        private AdImageKeyBean homenewgame;//	广告图片对象	新游首发广告图	详见 轮播图、广告图
        private AdImageKeyBean homenewrmd;//	广告图片对象	新游推荐广告图	详见 轮播图、广告图
        private AdImageKeyBean homenewserver;//	广告图片对象	新服表广告图	详见 轮播图、广告图
        private AdImageKeyBean hometestgame;//	广告图片对象	测试表广告图	详见 轮播图、广告图
        private AdImageKeyBean hometopper;//	广告图片对象	首页轮播图	详见 轮播图、广告图
        private AdImageKeyBean texthome;//	广告图片对象	首页轮播图	详见 轮播图、广告图
        private AdImageKeyBean welfareslide;//	广告图片对象	公益服游戏广告图	详见 轮播图、广告图
        private GameBeanList.DataBean newgame	;//游戏对象	新游首发游戏列表	详见 游戏列表
        private GameBeanList.DataBean newrmd	;//游戏对象	新游推荐游戏列表	详见 游戏列表
        private GameBeanList.GameGiftBean testgame	;//游戏对象	测试表游戏列表	详见 游戏列表
        private GameBeanList.GameGiftBean newserver;//	游戏对象	新服表游戏列表	详见 游戏列表
        private GameBeanList.DataBean hotgame	;//游戏对象	手游风向标游戏列表	详见 游戏列表
        private GameBeanList.DataBean likegame	;//游戏对象	猜你喜欢游戏列表	详见 游戏列表
        private GameBeanList.DataBean welfaregame	;//游戏对象	公益服游戏列表	详见 游戏列表

        public AdImageKeyBean getWelfareslide() {
            return welfareslide;
        }

        public void setWelfareslide(AdImageKeyBean welfareslide) {
            this.welfareslide = welfareslide;
        }

        public GameBeanList.DataBean getWelfaregame() {
            return welfaregame;
        }

        public void setWelfaregame(GameBeanList.DataBean welfaregame) {
            this.welfaregame = welfaregame;
        }

        public AdImageKeyBean getHomehotgame() {
            return homehotgame;
        }

        public void setHomehotgame(AdImageKeyBean homehotgame) {
            this.homehotgame = homehotgame;
        }

        public AdImageKeyBean getHomenewgame() {
            return homenewgame;
        }

        public void setHomenewgame(AdImageKeyBean homenewgame) {
            this.homenewgame = homenewgame;
        }

        public AdImageKeyBean getHomenewrmd() {
            return homenewrmd;
        }

        public void setHomenewrmd(AdImageKeyBean homenewrmd) {
            this.homenewrmd = homenewrmd;
        }

        public AdImageKeyBean getHomenewserver() {
            return homenewserver;
        }

        public void setHomenewserver(AdImageKeyBean homenewserver) {
            this.homenewserver = homenewserver;
        }

        public AdImageKeyBean getHometestgame() {
            return hometestgame;
        }

        public void setHometestgame(AdImageKeyBean hometestgame) {
            this.hometestgame = hometestgame;
        }

        public AdImageKeyBean getHometopper() {
            return hometopper;
        }

        public void setHometopper(AdImageKeyBean hometopper) {
            this.hometopper = hometopper;
        }

        public AdImageKeyBean getTexthome() {
            return texthome;
        }

        public void setTexthome(AdImageKeyBean texthome) {
            this.texthome = texthome;
        }

        public GameBeanList.DataBean getNewgame() {
            return newgame;
        }

        public void setNewgame(GameBeanList.DataBean newgame) {
            this.newgame = newgame;
        }

        public GameBeanList.DataBean getNewrmd() {
            return newrmd;
        }

        public void setNewrmd(GameBeanList.DataBean newrmd) {
            this.newrmd = newrmd;
        }

        public GameBeanList.GameGiftBean getTestgame() {
            return testgame;
        }

        public void setTestgame(GameBeanList.GameGiftBean testgame) {
            this.testgame = testgame;
        }

        public GameBeanList.GameGiftBean getNewserver() {
            return newserver;
        }

        public void setNewserver(GameBeanList.GameGiftBean newserver) {
            this.newserver = newserver;
        }

        public GameBeanList.DataBean getHotgame() {
            return hotgame;
        }

        public void setHotgame(GameBeanList.DataBean hotgame) {
            this.hotgame = hotgame;
        }

        public GameBeanList.DataBean getLikegame() {
            return likegame;
        }

        public void setLikegame(GameBeanList.DataBean likegame) {
            this.likegame = likegame;
        }
    }
}
