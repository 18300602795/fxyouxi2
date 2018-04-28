package com.etsdk.app.huov7.pay;

/**
 * Created by liu hong liang on 2016/10/31.
 */

public interface IQueryPayResult {
    void queryPaySuccess();
    void queryPayFail(String code, String msg);
}
