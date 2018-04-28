package com.etsdk.app.huov7.pay;

/**
 * Created by Liuhongliangsdk on 2016/10/30.
 */

public interface IPayListener {
    void paySuccess(String orderId, float money);
    void payFail(String orderId, float money, boolean queryOrder, String msg);
}
