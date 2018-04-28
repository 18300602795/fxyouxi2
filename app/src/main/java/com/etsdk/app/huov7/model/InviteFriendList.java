package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by liu hong liang on 2017/2/22.
 */

public class InviteFriendList {
    private String totalintegral;//	FLOAT	累计获得积分	累计获得积分
    private String count;//	INT	奖励总数	奖励总数
    private List<AwardRecordItem> list;//	二维数组	商品列表

    public String getTotalintegral() {
        return totalintegral;
    }

    public void setTotalintegral(String totalintegral) {
        this.totalintegral = totalintegral;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<AwardRecordItem> getList() {
        return list;
    }

    public void setList(List<AwardRecordItem> list) {
        this.list = list;
    }
}
