package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/1/12.
 * 商品
 */
public class Goods {
    private String goodsid;//	INT	代金卷ID	12
    private String goodsname;//	STRING	商品名称	小熊优盘
    private String is_real;//	INT	2 实物 1 虚拟商品	123
    private int total;//	INT	商品总数	10
    private int remain;//	INT	商品剩余数	10
    private String market_price;//	FLOAT	市场价	2
    private String goods_intro;//	STRING	商品介绍	商品介绍
    private String goods_content;//	STRING	商品详细描述	商品详细描述
    private String original_img;//	URL 图片	图片地址
    private String integral;//	FLOAT	代金卷所需积分	100
    private String shippingname;//	STRING	物流名称	发货名称
    private String invoice_no;//	STRING	物流单号	物流单号
    private String consignee;//	STRING	收货人	收货人
    private String myintegral;	//	INT	我的实时积分	456456;
    private String note;//STRING	虚拟物品为卡密 实物为说明	虚拟物品为卡密 实物为说明

    public String getMyintegral() {
        return myintegral;
    }

    public void setMyintegral(String myintegral) {
        this.myintegral = myintegral;
    }

    public String getShippingname() {
        return shippingname;
    }

    public void setShippingname(String shippingname) {
        this.shippingname = shippingname;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getIs_real() {
        return is_real;
    }

    public void setIs_real(String is_real) {
        this.is_real = is_real;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getGoods_intro() {
        return goods_intro;
    }

    public void setGoods_intro(String goods_intro) {
        this.goods_intro = goods_intro;
    }

    public String getGoods_content() {
        return goods_content;
    }

    public void setGoods_content(String goods_content) {
        this.goods_content = goods_content;
    }

    public String getOriginal_img() {
        return original_img;
    }

    public void setOriginal_img(String original_img) {
        this.original_img = original_img;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}