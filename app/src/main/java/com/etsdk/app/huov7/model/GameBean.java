package com.etsdk.app.huov7.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu hong liang on 2016/12/21.
 * 最普通的游戏列表item
 *
 */
public class GameBean {
    private String gameid;
    private String icon;
    private String gamename="大话西游";
    private String parent_type;
    private String type;
    private String size;
    private String hot;
    private String isnew;
    private String category;

    private String downcnt;
    private String distype; //0 无折扣 1 折扣 2 返利
    private String rebate;//返利比例，是小数

    private String downlink;
    private String oneword;
    private String desc;
    private String score;

    private String likecnt;
    private String sharecnt;
    private String version;
    private String sys;
    private String runtime;
    private String packagename;
    private ArrayList<String> image;
    private String giftcnt;
    private String couponcnt;
    private String gmcnt;//	FLOAT	游戏币数量	游戏币数量
    private List<StartServerInfo> serlist;//开服信息列表
    private String teststatus;//	STRING	状态	状态说明2017-02-21
    private String upinfo;//	STRING	更新信息 2017-02-21
    private String give_first;//	1否 2是 2017-09-30 龙川奇点新增
    private String welfare;



    //    2016.10.26 欧忠富沟通的那个用户需要显示的充值按钮是否显示折扣返利
//    benefit_type跟rate，benefit_type为0表示没有充值跟折扣，1为折扣，2为返利，rate为比率
    private String benefit_type;
    private String rate;

    private int id;//下载id

    private boolean showRank;

    private int discounttype;//优惠类型 1折扣 2返利 0无
    private float discount;//续充 优惠比例0.2
    private float first_discount;//首充 优惠比例0.2




    public GameBean(String gameid, String icon, String gamename, String size) {
        this.gameid = gameid;
        this.icon = icon;
        this.gamename = gamename;
        this.size = size;
    }

    public GameBean() {
    }

    public String getWelfare() {
        return welfare;
    }

    public void setWelfare(String welfare) {
        this.welfare = welfare;
    }

    public int getDiscounttype() {
        return discounttype;
    }

    public void setDiscounttype(int discounttype) {
        this.discounttype = discounttype;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getFirst_discount() {
        return first_discount;
    }

    public void setFirst_discount(float first_discount) {
        this.first_discount = first_discount;
    }

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

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    public String getIsnew() {
        return isnew;
    }

    public void setIsnew(String isnew) {
        this.isnew = isnew;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDowncnt() {
        return downcnt;
    }

    public void setDowncnt(String downcnt) {
        this.downcnt = downcnt;
    }

    public String getDistype() {
        return distype;
    }

    public void setDistype(String distype) {
        this.distype = distype;
    }

    public String getRebate() {
        return rebate;
    }

    public void setRebate(String rebate) {
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getLikecnt() {
        return likecnt;
    }

    public void setLikecnt(String likecnt) {
        this.likecnt = likecnt;
    }

    public String getSharecnt() {
        return sharecnt;
    }

    public void setSharecnt(String sharecnt) {
        this.sharecnt = sharecnt;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSys() {
        return sys;
    }

    public void setSys(String system) {
        this.sys = system;
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

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }


    public String getGiftcnt() {
        return giftcnt;
    }

    public void setGiftcnt(String giftcnt) {
        this.giftcnt = giftcnt;
    }

    public String getCouponcnt() {
        return couponcnt;
    }

    public void setCouponcnt(String couponcnt) {
        this.couponcnt = couponcnt;
    }

    public String getBenefit_type() {
        return benefit_type;
    }

    public void setBenefit_type(String benefit_type) {
        this.benefit_type = benefit_type;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isShowRank() {
        return showRank;
    }

    public void setShowRank(boolean showRank) {
        this.showRank = showRank;
    }

    public String getGmcnt() {
        return gmcnt;
    }

    public void setGmcnt(String gmcnt) {
        this.gmcnt = gmcnt;
    }


    public List<StartServerInfo> getSerlist() {
        return serlist;
    }

    public void setSerlist(List<StartServerInfo> serlist) {
        this.serlist = serlist;
    }

    public String getTeststatus() {
        return teststatus;
    }

    public void setTeststatus(String teststatus) {
        this.teststatus = teststatus;
    }

    public String getUpinfo() {
        return upinfo;
    }

    public void setUpinfo(String upinfo) {
        this.upinfo = upinfo;
    }

    public String getGive_first() {
        return give_first;
    }

    public void setGive_first(String give_first) {
        this.give_first = give_first;
    }
}