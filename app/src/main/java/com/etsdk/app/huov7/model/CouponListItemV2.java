package com.etsdk.app.huov7.model;


/**
 * 游戏代金券，对应的游戏币换来的
 * 2017/5/18.
 */
public class CouponListItemV2 {
    private String gameid;//        	代金卷对应游戏ID	123
    private String icon;//              URL 代金卷logo	默认取游戏logo
    private String couponid;//	        代金卷ID	12
    private String gamename;//          游戏名称	天天挂传奇
    private String parent_type;//     	类型，逗号(,)隔开	父类型
    private String type;//          	类型，逗号(,)隔开	类型射击，动作
    private String size;//          	包体大小	100.30M
    private int hot;//          	    2热门	2热门1普通
    private int isnew;//          	    2新游	2新游1普通
    private String category;//          单机 网游	单机
    private int downcnt;//          	下载次数	561561
    private int distype;//          	折扣类型	0无折扣1充值返利2打折充值
    private float discount;//          	折扣(0-1之间)	distype为1时显示0.5显示5折
    private float rebate;//          	返利比例	distype为2时显示1表示返利100%
    private String downlink;//          下载链接
    private String oneword;//           一句话描述
    private int score;//          	    评分 (1-10)
    private int likecnt;//          	点赞数量
    private int sharecnt;//          	分享次数
    private String runtime;//           上线时间 2016-08-16
    private String packagename;//       包名
    private float gmcnt;//          	游戏币数量

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCouponid() {
        return couponid;
    }

    public void setCouponid(String couponid) {
        this.couponid = couponid;
    }

    public String getGamename() {
        return gamename;
    }

    public void setGamename(String gamename) {
        this.gamename = gamename;
    }

    public String getParent_type() {
        return parent_type;
    }

    public void setParent_type(String parent_type) {
        this.parent_type = parent_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public int getIsnew() {
        return isnew;
    }

    public void setIsnew(int isnew) {
        this.isnew = isnew;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDowncnt() {
        return downcnt;
    }

    public void setDowncnt(int downcnt) {
        this.downcnt = downcnt;
    }

    public int getDistype() {
        return distype;
    }

    public void setDistype(int distype) {
        this.distype = distype;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getRebate() {
        return rebate;
    }

    public void setRebate(float rebate) {
        this.rebate = rebate;
    }

    public String getDownlink() {
        return downlink;
    }

    public void setDownlink(String downlink) {
        this.downlink = downlink;
    }

    public String getOneword() {
        return oneword;
    }

    public void setOneword(String oneword) {
        this.oneword = oneword;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLikecnt() {
        return likecnt;
    }

    public void setLikecnt(int likecnt) {
        this.likecnt = likecnt;
    }

    public int getSharecnt() {
        return sharecnt;
    }

    public void setSharecnt(int sharecnt) {
        this.sharecnt = sharecnt;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public float getGmcnt() {
        return gmcnt;
    }

    public void setGmcnt(float gmcnt) {
        this.gmcnt = gmcnt;
    }
}