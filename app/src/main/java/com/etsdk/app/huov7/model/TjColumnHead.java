package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2016/12/21.
 */
public class TjColumnHead {
    public static final int TYPE_NEW_GAME_SF=1;
    public static final int TYPE_GAME_FXB =2;
    public static final int TYPE_GAME_TJ =3;
    public static final int TYPE_GAME_LIKE =4;
    public static final int TYPE_TODAY =5;
    public static final int TYPE_LUXURY_GIFT =6;//mainFuli 豪华礼包
    public static final int TYPE_TIME_COUPON =7;//mainFuli 限时代金券
    public static final int TYPE_HOT_REC_GIFT =8;//FuliGift 热门推荐礼包
    public static final int TYPE_HOT_GIFT=9;//FuliGift 热门礼包
    public static final int TYPE_NEW_GIFT=10;//FuliGift 最新礼包
    public static final int TYPE_SCORE_COUPONE=11;//积分商城优惠券
    public static final int TYPE_SCORE_GIFT_CARD=12;//积分商城礼品卡
    public static final int TYPE_SCORE_GOODS=13;//积分商城实物
    public static final int TYPE_GAME_WELFARE=14;//公益游戏
    private int type;

    public TjColumnHead(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}