package com.etsdk.app.huov7.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = WXPayEntryActivity.class.getSimpleName();
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		if(TextUtils.isEmpty(WxPayPluginActivity.APPID)){
			Toast.makeText(this,"Appid 为null",Toast.LENGTH_LONG).show();
		}
    	api = WXAPIFactory.createWXAPI(this, WxPayPluginActivity.APPID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		finish();
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if(WxPayPluginActivity.wxPayPluginActivity!=null){
				WxPayPluginActivity.wxPayPluginActivity.setPayResultBack(resp.errCode,resp.errStr);
			}
		}

	}
}