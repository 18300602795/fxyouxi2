package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by liu hong liang on 2017/1/18.
 *  积分排行列表
 */

public class ScoreRankListBean extends BaseModel{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String myintegral;//	FLOAT	我的积分	默认为0
        private String myrank;    //INT	我的排行	默认为0
        private int count;    //INT	总人数	总人数
        private List<ScoreRankBean> list;//	二维数组	商品列表

        public String getMyintegral() {
            return myintegral;
        }

        public void setMyintegral(String myintegral) {
            this.myintegral = myintegral;
        }

        public String getMyrank() {
            return myrank;
        }

        public void setMyrank(String myrank) {
            this.myrank = myrank;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<ScoreRankBean> getList() {
            return list;
        }

        public void setList(List<ScoreRankBean> list) {
            this.list = list;
        }
    }
    public static class ScoreRankBean {
        private String mem_id;//INT	代金卷ID	12
        private String nicename;//STRING	玩家昵称	玩家昵称
        private String portrait;//URL 玩家头像	玩家头像
        private String integral;//FLOAT	积分	123
        private int rank;//玩家排名

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public String getMem_id() {
            return mem_id;
        }

        public void setMem_id(String mem_id) {
            this.mem_id = mem_id;
        }

        public String getNicename() {
            return nicename;
        }

        public void setNicename(String nicename) {
            this.nicename = nicename;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }
    }
}
