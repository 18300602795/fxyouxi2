package com.etsdk.app.huov7.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import com.etsdk.app.huov7.service.HuoSdkService;
import com.game.sdk.SdkConstant;
import com.liang530.log.L;
import com.liang530.utils.BaseAppUtil;

/**
 * @author liuhongliang
 * 
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {
    private static final String TAG = ConnectionChangeReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {//在此监听wifi有无
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    L.e("start","wifi WIFI_STATE_DISABLED");
//                    EventBus.getDefault().post(new NetConnectEvent(NetConnectEvent.TYPE_STOP));
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    L.e("start","wifi WIFI_STATE_ENABLING");
                    break;

                case WifiManager.WIFI_STATE_UNKNOWN:
                    break;
            }
        }
        L.e(TAG,"NETWORK_STATE_CHANGED"+BaseAppUtil.isOnline(context));
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            if(BaseAppUtil.isOnline(context)&& TextUtils.isEmpty(SdkConstant.BASE_URL)){//连接到网络，但是没初始化成功，进行重新初始化
                Intent serverIntent=new Intent(context,HuoSdkService.class);
                context.startService(serverIntent);
                Log.e(TAG,"net restart，startup again!");
            }
        }
    }
}