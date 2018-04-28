package com.game.sdk.http;

import android.content.Context;
import android.text.TextUtils;

import com.etsdk.app.huov7.ui.LoginActivity;
import com.game.sdk.SdkConstant;
import com.game.sdk.domain.NotProguard;
import com.game.sdk.log.L;
import com.game.sdk.log.T;
import com.game.sdk.util.AuthCodeUtil;
import com.game.sdk.util.RSAUtils;
import com.google.gson.Gson;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * Created by liu hong liang on 2016/11/9.
 */
@NotProguard
public abstract class HttpCallbackDecode<E> extends HttpCallback {
    private static final String TAG = HttpCallbackDecode.class.getSimpleName();
    public static final String CODE_RSA_KEY_ERROR = "1001";//1001	请求KEY错误	rsakey	解密错误
    public static final String CODE_SESSION_ERROR = "1002";//1002	登陆已过期, 重新登录	session过期	session过期 返回信息CODE
    private boolean showLoading = false;
    private boolean loadingCancel = true;
    private boolean showTs = false;
    private String loadMsg;
    private Context activity;
    private String authkey;
    private LoadWaitDialogUtil loadingDialogView;
    private boolean isBack;


    public HttpCallbackDecode(Context activity, String authkey) {
        this.activity = activity;
        this.authkey = authkey;
    }

    public HttpCallbackDecode(Context activity, String authkey, boolean isBack) {
        this.activity = activity;
        this.authkey = authkey;
        this.isBack = isBack;
    }

    @Override
    public final void onSuccess(String t) {
        L.e(TAG, "http_result=" + t);
        if (isBack) {
            E dataObject = new Gson().fromJson(t, getTClass());
            onDataSuccess(dataObject);
            return;
        }
        try {
            JSONObject object = new JSONObject(t);
            String data = object.optString("data");
            Integer code = object.optInt("code");
            String msg = object.optString("msg");
//            JSONObject msgObj = new JSONObject(msg);
//            String data1 = msgObj.optString("data");
//            String decodeAuthData1 = AuthCodeUtil.authcodeDecode(data1, authkey);
//            L.d(TAG, "decodeAuthData1=" + decodeAuthData1);
//            JSONObject dateObj = new JSONObject(decodeAuthData1);
//            String data = dateObj.optString("user_token");
            if (code >= 400) {
                if (CODE_SESSION_ERROR.equals(code + "")) {//登陆过期，需要重新登陆
                    LoginActivity.start(activity);
                    return;
                }
                onFailure(code + "", msg);
                return;
            } else if (TextUtils.isEmpty(data) || "null".equals(data)) {//数据是null的
                onDataSuccess(null);
                onDataSuccess(null, code + "", msg);
                return;
            }
            L.d(TAG, "http_result_authkey=" + authkey);
            L.d(TAG, "data=" + data);
            String decodeAuthData = AuthCodeUtil.authcodeDecode(data, authkey);
            L.e(TAG, "http_result_authd=" + decodeAuthData);
            //使用
            JSONObject jsonObject = new JSONObject(decodeAuthData);
            String sign = jsonObject.optString("sign");
            String responcedata = jsonObject.optString("responcedata");
            L.e(TAG, "responcedata=" + responcedata);
            //验证签名
            L.d(TAG, "http_result_rsaKey=" + SdkConstant.RSA_PUBLIC_KEY);
            boolean verify = RSAUtils.verify(responcedata.getBytes(), SdkConstant.RSA_PUBLIC_KEY, sign);
            if (verify) {//
                E dataObject = new Gson().fromJson(responcedata, getTClass());
                onDataSuccess(dataObject);
                onDataSuccess(dataObject, code + "", msg);
                L.d(TAG, "签名验证通过");

            } else {
                onFailure("-1", "数据认证失败，请稍后再试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            onFailure("-1", "服务器忙，请稍后再试");
        }
    }

    @Override
    public void onSuccess(Map<String, String> headers, byte[] t) {
        super.onSuccess(headers, t);
    }

    /**
     * 数据解析成功
     *
     * @param data
     */
    @NotProguard
    public abstract void onDataSuccess(E data);

    public void onDataSuccess(E data, String code, String msg) {

    }

    @Override
    public final void onFailure(int errorNo, String strMsg, String completionInfo) {
        L.d(TAG, "onFailure=" + completionInfo);
        if (isBack) {
            T.s(activity, "提交失败");
        }
        onFailure("" + errorNo, "连接失败，请稍后重试！");
    }

    public void onFailure(String code, String msg) {
        if (showTs) {
            T.s(activity, msg == null ? "连接失败" : msg);
        }
    }

    @Override
    public void onPreStart() {
        super.onPreStart();
        if (showLoading) {
            loadingDialogView = new LoadWaitDialogUtil(loadingCancel, loadMsg);
            loadingDialogView.show(activity);
//            DialogUtil.showDialog(activity,loadingCancel,loadMsg);
        }
    }

    public void onFinish() {
        super.onFinish();
        if (loadingDialogView != null) {
            try {
                loadingDialogView.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        if(DialogUtil.isShowing()){
//            try {
//                DialogUtil.dismissDialog();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    protected Class<E> getTClass() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Type resultType = type.getActualTypeArguments()[0];
        if (resultType instanceof Class) {
            return (Class<E>) resultType;
        } else {
            // 处理集合
            try {
                Field field = resultType.getClass().getDeclaredField("rawTypeName");
                field.setAccessible(true);
                String rawTypeName = (String) field.get(resultType);
                return (Class<E>) Class.forName(rawTypeName);
            } catch (Exception e) {
                return (Class<E>) Collection.class;
            }
        }
    }

    public String getLoadMsg() {
        return loadMsg;
    }

    public void setLoadMsg(String loadMsg) {
        this.loadMsg = loadMsg;
    }

    public boolean isLoadingCancel() {
        return loadingCancel;
    }

    public void setLoadingCancel(boolean loadingCancel) {
        this.loadingCancel = loadingCancel;
    }

    public boolean isShowLoading() {
        return showLoading;
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
    }

    public boolean isShowTs() {
        return showTs;
    }

    public void setShowTs(boolean showTs) {
        this.showTs = showTs;
    }
}
