package com.etsdk.app.huov7.getcash.model;

import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.domain.NotProguard;

/**
 * 添加提现账号
 * Created by Administrator on 2017/5/31 0031.
 */
@NotProguard
public class AddAccountRequestBean extends BaseRequestBean {
    public String paytype;
    public String bankname;
    public String curbank;
    public String account;
    public String holder;
    public int isdefault;

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

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public int getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(int isdefault) {
        this.isdefault = isdefault;
    }
}
