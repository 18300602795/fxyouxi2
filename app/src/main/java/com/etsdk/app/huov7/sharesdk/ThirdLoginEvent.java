package com.etsdk.app.huov7.sharesdk;
/**
 * Created by 刘红亮 on 2016/3/16.
 */
public class ThirdLoginEvent {
    public int accountType;
    public String account;
    public String nickName;
    public String icon;
    public String errorMsg;
    public ThirdLoginEvent(int accountType, String account,String nickName,String icon) {
        this.accountType = accountType;
        this.account = account;
        this.nickName = nickName;
        this.icon = icon;
    }
    public ThirdLoginEvent() {

    }
}


