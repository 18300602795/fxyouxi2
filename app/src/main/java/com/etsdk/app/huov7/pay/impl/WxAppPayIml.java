package com.etsdk.app.huov7.pay.impl;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.etsdk.app.huov7.model.PayResultBean;
import com.etsdk.app.huov7.pay.IHuoPay;
import com.etsdk.app.huov7.pay.IPayListener;
import com.etsdk.app.huov7.pay.impl.wxapppay.WxPayPlugin;
import com.game.sdk.SdkConstant;
import com.game.sdk.log.L;


/**
 * Created by liu hong liang on 2017/6/7.
 * 通过调用插件apk 或者对应app的微信支付实现
 */

public class WxAppPayIml extends IHuoPay {
    private Activity mActivity;
    private String orderId;
    private float money;
    private IPayListener iPayListener;
    @Override
    protected void startPay(Activity activity, IPayListener listener, float money, PayResultBean payResultBean) {
        this.iPayListener = listener;
        this.money = money;
        this.mActivity = activity;
        this.orderId = payResultBean.getOrder_id();
        WxPayPlugin.startWxPay(activity, SdkConstant.APP_PACKAGENAME,payResultBean.getToken());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("MainActivity","requestCode="+requestCode+" resultCode="+resultCode+" data="+data);
        if(requestCode==WxPayPlugin.REQUEST_WX_PAY_CODE){
            if(data!=null){
                if(data.getIntExtra("errCode",-1)==0){
                    if (iPayListener != null) {
                        iPayListener.paySuccess(orderId, money);
                    }
                }else if(data.getIntExtra("errCode",-1)==-2){
                    if (iPayListener != null) {
                        iPayListener.payFail(orderId, money,false,"取消支付");
                    }
                }else{
                    if (iPayListener != null) {
                        iPayListener.payFail(orderId, money,false,"支付失败");
                    }
                }
            }else{
                L.e("WxAppPayIml","未安装wx支付插件");
            }
        }
    }
}
