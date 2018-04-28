package com.etsdk.app.huov7.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by liu hong liang on 2017/6/5.
 * 微信支付activity
 * 接收调起微信支付参数，调起微信支付
 */
public class WxPayPluginActivity extends Activity {
    public  static String APPID;
    public static WxPayPluginActivity wxPayPluginActivity;
    private String packageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        startPay(intent);
    }
    private void startPay(Intent intent){
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);

        wxPayPluginActivity=this;
        if(intent!=null){

            String token=intent.getStringExtra("token");
            packageName = intent.getStringExtra("packageName");
            Log.e("WxPayPluginActivity","收到参数 token="+token);
            try {
                JSONObject json = new JSONObject(token);
                if(null != json && !json.has("retcode") ){
                    PayReq req = new PayReq();
                    //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                    req.appId			= json.getString("appid");
                    req.partnerId		= json.getString("partnerid");
                    req.prepayId		= json.getString("prepayid");
                    req.nonceStr		= json.getString("noncestr");
                    req.timeStamp		= json.getString("timestamp");
                    req.packageValue	= json.getString("package");
                    req.sign			= json.getString("sign");
//                    req.extData			= json.optString("extData"); // optional
                    // 将该app注册到微信
                    APPID=req.appId;
                    msgApi.registerApp(req.appId);
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    msgApi.sendReq(req);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                //调起失败
                setPayResultBack(-1,"参数错误");
            }
        }else{//参数未传递，直接回调失败
            setPayResultBack(-1,"参数错误");
        }
    }
    public void setPayResultBack(int errCode,String errStr){
        Intent intent=new Intent();
        intent.putExtra("errCode", errCode);
        intent.putExtra("errStr", errStr);
        setResult(101,intent);
        Log.e("hongliang","回调支付结果："+errCode+" "+errStr);
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        startPay(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wxPayPluginActivity=null;
        Log.e("hongliang","WxPayPluginActivity,onDestroy");
    }
}
