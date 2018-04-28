package com.etsdk.app.huov7.pay.impl;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.util.Log;

import com.etsdk.app.huov7.model.PayResultBean;
import com.etsdk.app.huov7.pay.IHuoPay;
import com.etsdk.app.huov7.pay.IPayListener;
import com.game.sdk.domain.NotProguard;
import com.game.sdk.log.L;
import com.payeco.android.plugin.PayecoPluginLoadingActivity;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by liu hong liang on 2016/10/14.
 */

public class EcoPayIml extends IHuoPay {
    public static final String ENVIRONMENT = "01";// "00"测试环境 ."01"生产环境
    public final static String BROADCAST_PAY_END = "com.merchant.broadcast";// 广播
    /**
     * @Fields payecoPayBroadcastReceiver : 易联支付插件广播
     */
    private BroadcastReceiver payecoPayBroadcastReceiver;
    private Activity mActivity;
    private String orderId;
    private float money;
    private IPayListener iPayListener;
    private boolean queryOrder=false;

    @Override
    @NotProguard
    public void startPay(Activity activity, IPayListener listener, float money, PayResultBean payResultBean) {
        this.mActivity = activity;
        this.money = money;
        this.iPayListener = listener;
        this.orderId = payResultBean.getOrder_id();
        Intent intent = new Intent(activity, PayecoPluginLoadingActivity.class);
        intent.putExtra("upPay.Req", payResultBean.getToken());
        intent.putExtra("Broadcast", BROADCAST_PAY_END); // 广播接收地址
        intent.putExtra("Environment", ENVIRONMENT); // 00: 测试环境, 01:// 生产环境
        Configuration configuration = activity.getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
            intent.putExtra("orientation_mode", "land");
        }
        activity.startActivity(intent);
        // 初始化支付结果广播接收器
        initPayecoPayBroadcastReceiver();
        // 注册支付结果广播接收器
        registerPayecoPayBroadcastReceiver();
    }


    /**
     * @Title registerPayecoPayBroadcastReceiver
     * @Description 注册广播接收器
     */
    private void registerPayecoPayBroadcastReceiver() {
        if (payecoPayBroadcastReceiver != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(BROADCAST_PAY_END);
            filter.addCategory(Intent.CATEGORY_DEFAULT);
            mActivity.registerReceiver(payecoPayBroadcastReceiver, filter);
        }// 非空的之后才会去注册
    }


    // 初始化支付结果广播接收器
    private void initPayecoPayBroadcastReceiver() {
        payecoPayBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // 接收易联支付插件的广播回调
                String action = intent.getAction();

                if (!BROADCAST_PAY_END.equals(action)) {
                    L.e("test", "接收到广播，但与注册的名称不一致[" + action + "]");
                    return;
                }
                // 商户的业务处理
                String result = intent.getExtras().getString("upPay.Rsp");
                Log.i("test", "接收到广播内容：" + result);

                String notifyParams = result;
                try {
                    JSONObject json = new JSONObject(notifyParams);
                    if (json.has("respCode")) {
                        String respCode = json.getString("respCode");
                        if ("W101".equals(respCode)) { //W101订单未支付，用户主动退出插件
                            if (iPayListener != null) {
                                iPayListener.payFail(orderId, money,queryOrder, "用户取消支付");
                            }
                            return;
                        }
                        if (!"0000".equals(respCode)) { //非0000，订单支付响应异常
                            String respDesc = json.getString("respDesc");
                            if (iPayListener != null) {
                                iPayListener.payFail(orderId, money,queryOrder, "支付失败");
                            }
                            return;
                        }
                    }
                    if (json.has("Status")) {
                        String status = "";
                        if ("01".equals(json.getString("Status"))) {
                        }
                        if ("02".equals(json.getString("Status"))) {
                            if (iPayListener != null) {
                                iPayListener.paySuccess(orderId, money);
                            }
                            return;
                        }
                        if ("03".equals(json.getString("Status"))) {
                            status = "已退款(全额撤销/冲正)";
                        }
                        if ("04".equals(json.getString("Status"))) {
                            status = "已过期";
                        }
                        if ("05".equals(json.getString("Status"))) {
                            status = "已作废";
                        }
                        if ("06".equals(json.getString("Status"))) {
                            status = "支付中";
                        }
                        if ("07".equals(json.getString("Status"))) {
                            status = "退款中";
                        }
                        if ("08".equals(json.getString("Status"))) {
                            status = "已被商户撤销";
                        }
                        if ("09".equals(json.getString("Status"))) {
                            status = "已被持卡人撤销";
                        }
                        if ("10".equals(json.getString("Status"))) {
                            status = "调账-支付成功";
                            if (iPayListener != null) {
                                iPayListener.paySuccess(orderId, money);
                            }
                        }
                        if ("11".equals(json.getString("Status"))) {
                            status = "调账-退款成功";
                        }
                        if ("12".equals(json.getString("Status"))) {
                            status = "已退货";
                        }
//                            ll.addView(getRow("交易状态", status));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (iPayListener != null) {
                    iPayListener.payFail(orderId, money, queryOrder,"支付失败");
                }
            }
        };
    }

    /**
     * @Title unRegisterPayecoPayBroadcastReceiver
     * @Description 注销广播接收器
     */
    private void unRegisterPayecoPayBroadcastReceiver() {

        if (payecoPayBroadcastReceiver != null) {
            mActivity.unregisterReceiver(payecoPayBroadcastReceiver);
            payecoPayBroadcastReceiver = null;
        }
    }
    @Override
    public void onDestory() {
        //销毁异步任务
        unRegisterPayecoPayBroadcastReceiver();
        iPayListener = null;
    }
}
