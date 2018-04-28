package com.game.sdk;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.etsdk.app.huov7.BuildConfig;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.MessageEvent;
import com.etsdk.app.huov7.model.StartupResultBean;
import com.game.sdk.domain.StartUpBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.listener.OnInitSdkListener;
import com.game.sdk.so.NativeListener;
import com.game.sdk.so.SdkNative;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.control.LoginControl;
import com.liang530.log.L;
import com.liang530.log.SP;
import com.liang530.utils.BaseAppUtil;

import org.greenrobot.eventbus.EventBus;


/**
 * author janecer 2014年7月22日上午9:45:18
 */
public class HuosdkManager {
    private final static int CODE_INIT_FAIL = -1;
    private final static int CODE_INIT_SUCCESS = 1;
    private static final boolean isDebug= BuildConfig.LOG_DEBUG;
    private static final String TAG = HuosdkManager.class.getSimpleName();
    private static HuosdkManager instance;
    private  Context mContext;
    private OnInitSdkListener onInitSdkListener;
    private int initRequestCount=0;
    private Handler huosdkHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_INIT_FAIL:
                    if(msg.arg2<3){//最多重试3次
                        initSdk(msg.arg2+1);
                    }else{
                        //关闭等待loading
                        if (onInitSdkListener!=null) {
                            onInitSdkListener.initError(msg.arg1+"",msg.obj+"");
                        }
                    }
                    break;
                case CODE_INIT_SUCCESS:
                    L.e("hongliangsdk1", SdkConstant.HS_AGENT);
                    L.e("hongliangsdk1", SdkConstant.HS_AGENT);
                    initRequestCount++;
                    //去初始化
                    gotoStartup(1);
                    break;
            }
        }
    };
    // 单例模式
    public static synchronized HuosdkManager getInstance() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            L.d(TAG, "实例化失败,未在主线程调用");
            return null;
        }
        if (null == instance) {
            instance = new HuosdkManager();
        }
        return instance;
    }
    public void setContext(Context context){
        this.mContext=context;
    }
    public Context getContext(){
        return mContext;
    }

    private HuosdkManager() {
        RxVolley.setDebug(isDebug);
        L.init(isDebug);
    }

    /**
     * 初始化sdk
     * @param context 上下文对象
     * @param onInitSdkListener 回调监听
     */
    public void initSdk(Context context,OnInitSdkListener onInitSdkListener){
        this.onInitSdkListener=onInitSdkListener;
        this.mContext=context;
        //初始化设备信息
        SdkNative.soInit(context);
        //初始化sp
        SP.init(mContext);
        initRequestCount=0;
        initSdk(1);
    }

    /**
     * 初始化相关数据
     * count=1标示正常请求，2表示在初始化时发现rsakey错误后的重试流程
     */
    private void initSdk(final int count) {
        //初始化native
        AsyncTask<String, Integer, String> nativeAsyncTask = new AsyncTask<String, Integer, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //弹出等待loading在，installer和startup都完成后或者出现异常时关闭
            }
            @Override
            protected String doInBackground(String... params) {
                //初始化本地c配置
                if(SdkNative.initLocalConfig(mContext, SdkNative.TYPE_SDK)){
                    SdkNative.initNetConfig(mContext, new NativeListener() {
                        @Override
                        public void onSuccess() {
                            Message message = Message.obtain();
                            message.what = CODE_INIT_SUCCESS;
                            message.arg2=count;
                            huosdkHandler.sendMessage(message);
                        }
                        @Override
                        public void onFail(int code, final String msg) {
                            L.e("hongliangsdk", "native 失败code=" + code);
                            L.e("hongliangsdk", "native 失败msg=" + msg);
                            Message message = Message.obtain();
                            message.what = CODE_INIT_FAIL;
                            message.arg1 = code;
                            message.obj = msg;
                            message.arg2=count;
                            huosdkHandler.sendMessage(message);
                        }
                    });
                }else{
                    Message message = Message.obtain();
                    message.what = CODE_INIT_SUCCESS;
                    message.arg2=count;
                    huosdkHandler.sendMessage(message);
                }
                return null;
            }
        };
        if (!BaseAppUtil.isOnline(mContext)) {
            Toast.makeText(mContext, "网络连接错误，请检查网络", Toast.LENGTH_SHORT).show();
            return;
        }
        nativeAsyncTask.execute();
    }
    /**
     * count=1标示正常请求，2表示在初始化时发现rsakey错误后的重试流程
     * @param count 当前是第几次请求
     */
    public void gotoStartup(final int count) {
        StartUpBean startUpBean = new StartUpBean();
        int open_cnt = SdkNative.addInstallOpenCnt(mContext);//增量更新openCnt
        startUpBean.setOpen_cnt(open_cnt + "");
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(startUpBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<StartupResultBean>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(StartupResultBean data) {
                if (data != null) {
                    LoginControl.saveToken(data.getUser_token());
                    //处理更新信息
                    handleUpdateInfo(data.getUp_info());
                    if (onInitSdkListener!=null) {
                        onInitSdkListener.initSuccess("200","初始化成功");
                    }
                    EventBus.getDefault().postSticky(new MessageEvent(data.getNewmsg()));
                }
            }
            @Override
            public void onFailure(String code, String msg) {
                if(count<3){
                    //1001	请求KEY错误	rsakey	解密错误
                    if(HttpCallbackDecode.CODE_RSA_KEY_ERROR.equals(code)){//删除本地公钥，重新请求rsa公钥
                        SdkNative.resetInstall(mContext);
                        L.e(TAG,"rsakey错误，重新请求rsa公钥");
                        if(initRequestCount<2){//initSdk只重试一次rsa请求
                            initSdk(1000);
                            return;
                        }
                    }
                    super.onFailure(code,msg);
                    gotoStartup(count+1);//重试
                }else{
                    super.onFailure(code,msg);
                    if (onInitSdkListener!=null) {
                        onInitSdkListener.initError(code,msg);
                    }
                }
                L.e(TAG, "初始化失败 "+code+" "+msg);
            }
        };
        httpCallbackDecode.setShowTs(false);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);//对话框继续使用install接口，在startup联网结束后，自动结束等待loading
        RxVolley.post(AppApi.getUrl(AppApi.appinit), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }
    public void handleUpdateInfo(StartupResultBean.UpdateInfo updateInfo){
        if(updateInfo==null) return;
        if("0".equals(updateInfo.getUp_status())){
            return;
        }else {//有更新
            EventBus.getDefault().postSticky(updateInfo);
        }
    }
    public void setOnInitSdkListener(OnInitSdkListener onInitSdkListener){
        this.onInitSdkListener = onInitSdkListener;
    }
    public void handleSplashInfo(StartupResultBean.SplashInfo splashInfo){

    }
}
