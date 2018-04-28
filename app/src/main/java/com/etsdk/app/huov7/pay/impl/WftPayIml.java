package com.etsdk.app.huov7.pay.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.etsdk.app.huov7.model.PayResultBean;
import com.etsdk.app.huov7.pay.IHuoPay;
import com.etsdk.app.huov7.pay.IPayListener;
import com.game.sdk.domain.NotProguard;
import com.switfpass.pay.MainApplication;
import com.switfpass.pay.activity.PayPlugin;
import com.switfpass.pay.bean.RequestMsg;

import java.util.List;

/**
 * Created by liu hong liang on 2016/10/14.
 */

public class WftPayIml extends IHuoPay {
    private Activity activity;
    private String orderId;
    private float money;
    private IPayListener iPayListener;
    private boolean queryOrder=false;
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    @NotProguard
    public void startPay(Activity activity, IPayListener listener, float money, PayResultBean payResultBean) {
        this.money = money;
        this.iPayListener = listener;
        this.activity = activity;
        if (!isWeixinAvilible(activity)) {
            Toast.makeText(activity, "未安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        this.orderId = payResultBean.getOrder_id();
        float amount = payResultBean.getReal_amount();
        String token = payResultBean.getToken();
        RequestMsg msg = new RequestMsg();
        msg.setOutTradeNo(orderId);
        msg.setMoney(amount / 100);//用服务器的价格，可能存在打折
        msg.setTokenId(token);
        // 微信wap支付
        msg.setTradeType(MainApplication.PAY_WX_WAP);
        PayPlugin.unifiedH5Pay(activity, msg);
    }

    /**
     * 微付通支付结果回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            if (iPayListener != null) {
                iPayListener.payFail(orderId, money,queryOrder, "支付失败");
            }
            return;
        }
        String respCode = data.getExtras().getString("resultCode");
        if (!TextUtils.isEmpty(respCode)
                && respCode.equalsIgnoreCase("success")) {
            if (iPayListener != null) {
                iPayListener.paySuccess(orderId, money);
            }

        } else { // 其他状态NOPAY状态：取消支付，未支付等状态
            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
            if (iPayListener != null) {
                iPayListener.payFail(orderId, money, queryOrder,"支付失败");
            }
        }
    }

    @Override
    public void onDestory() {
        iPayListener = null;
    }
}
