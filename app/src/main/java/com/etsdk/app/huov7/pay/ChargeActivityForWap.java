package com.etsdk.app.huov7.pay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.PayWebRequestBean;
import com.etsdk.app.huov7.model.QueryOrderRequestBean;
import com.etsdk.app.huov7.model.QueryOrderResultBean;
import com.etsdk.app.huov7.shop.model.ShopListRefreshEvent;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.T;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.log.L;
import com.liang530.utils.BaseAppUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * @author lingguihua
 * @time 2016/09/08
 * @说明： 这个activity要接受两个参数，一个是mem_id(也就是账号，或者是账号的变体),另外一个是token(也就是密码，或者说密码的变体)
 * */
public class ChargeActivityForWap extends ImmerseActivity implements OnClickListener, IPayListener {
    private final static int CODE_PAY_FAIL = -1;//支付失败
    private final static int CODE_PAY_CANCEL = -2;//用户取消支付
    private static final String TAG = "ChargeActivityForWap";
    private WebView wv;
    private TextView tv_back, tv_charge_title;
    private ImageView iv_cancel;
    private String url, title;
    private String timestamp, hstoken;
    private double amount;
    private String toast;
    HashMap<String, String> header = new HashMap<String, String>();
    StringBuilder postDate = new StringBuilder();


    private static WeakReference<OnPaymentListener> paymentListener;// 充值接口监听
    public boolean isPaySus = false;// 支付jar包的回执
    public static final int REQUEST_CODE = 200;
    private String gameId;
    private int sellId;//小号交易id

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sdk_float_web);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("titleName");
        gameId = intent.getStringExtra("gameId");
        sellId = intent.getIntExtra("id", 0);

        // timestamp = intent.getStringExtra("timestamp");
        // hstoken = intent.getStringExtra("hs-token");

        wv = (WebView) findViewById(R.id.wv_content);

        tv_back = (TextView) findViewById(R.id.tv_back);

        iv_cancel = (ImageView) findViewById(R.id.iv_cancel);

        tv_charge_title = (TextView) findViewById(R.id.tv_charge_title);
        tv_charge_title.setText(title);
        tv_back.setOnClickListener(this);
        iv_cancel.setOnClickListener(this);


        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setLoadsImagesAutomatically(true);
        wv.getSettings().setAppCacheEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setDefaultTextEncodingName("UTF-8");
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//				if (dialog == null) {
//					dialog = new CustomProgressDialog(ChargeActivityForWap.this,
//							"正在加载中.....", R.anim.donghua_frame);
//				}
//				dialog.show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                L.e(TAG+"打开 "+url);
                if (url.startsWith("http") || url.startsWith("https") || url.startsWith("ftp")) {
                    return false;
                } else {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        view.getContext().startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(view.getContext(), "手机还没有安装支持打开此网页的应用！", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//				dialog.dismiss();
                webviewCompat(wv);
            }

        });
        wv.setWebChromeClient(new WebChromeClient());
        webviewCompat(wv);
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();// 移除
        if(gameId==null){
            gameId="";
        }

        PayWebRequestBean requestBean = new PayWebRequestBean();
        requestBean.setGameid(gameId);
        requestBean.setId(sellId);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));

        CommonJsForWeb js = new CommonJsForWeb(this, httpParamsBuild.getAuthkey(), this);
        wv.addJavascriptInterface(js, "huosdk");

        String postData = httpParamsBuild.getHttpParams().getUrlParams().toString();
        if (postData.startsWith("?")) {
            postData = postData.substring(1);
        }
        wv.postUrl(url, postData.getBytes());

    }
    public static WeakReference<OnPaymentListener> getPaymentListener() {
        return paymentListener;
    }

    public static void setPaymentListener(OnPaymentListener paymentListener) {
        WeakReference<OnPaymentListener> onPaymentListenerWeakReference=new WeakReference<OnPaymentListener>(paymentListener);
        ChargeActivityForWap.paymentListener = onPaymentListenerWeakReference;
    }
    /**
     * 同步一下cookie
     */
    public static void synCookies(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();// 移除
        // cookieManager.setCookie(url, cookies);//指定要修改的cookies
        CookieSyncManager.getInstance().sync();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == tv_back.getId()) {
            if (wv.canGoBack()) {
                wv.goBack();
                return;
            }else{
                finish();
            }

            handleResult();
        }
        if (v.getId() == iv_cancel.getId()) {
            handleResult();
        }

    }

    /**
     * 一些版本特性操作，需要适配、
     *
     * @date 6/3
     * @param mWebView
     * @reason 在微蓝项目的时候遇到了 返回键 之后 wv显示错误信息
     * */
    private void webviewCompat(WebView mWebView) {
        if (BaseAppUtil.isOnline(mWebView.getContext())) {
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            mWebView.getSettings().setCacheMode(
                    WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (wv.canGoBack()) {
            wv.goBack();
        } else {
            handleResult();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new ShopListRefreshEvent());
        this.finish();
    }


    private void handleResult() {
        if (isPaySus) {
            PaymentCallbackInfo payInfo = new PaymentCallbackInfo();
            payInfo.msg = toast == null ? "支付成功" : toast;
            payInfo.money = amount;
            if(paymentListener!=null&&paymentListener.get()!=null){
                paymentListener.get().paymentSuccess(payInfo);
                finish();
            }
        } else {
            PaymentErrorMsg errorMsg = new PaymentErrorMsg();
            errorMsg.code = -1;
            errorMsg.money = amount;
            errorMsg.msg = toast == null ? "支付失败" : toast;
            if(paymentListener!=null&&paymentListener.get()!=null){
                paymentListener.get().paymentError(errorMsg);
            }
        }
    }
    public static void start(Context context,String url,String titleName) {
        Intent starter = new Intent(context, ChargeActivityForWap.class);
        starter.putExtra("url",url);
        starter.putExtra("titleName",titleName);
        context.startActivity(starter);
    }

    /**
     * 购买账户支付
     * @param context
     * @param url
     * @param id 注意与游戏id区分
     */
    public static void start(Context context,String url,String titleName, int id) {
        Intent starter = new Intent(context, ChargeActivityForWap.class);
        starter.putExtra("url",url);
        starter.putExtra("titleName",titleName);
        starter.putExtra("id",id);
        context.startActivity(starter);
    }

    /**
     * 外部设置监听
     * @param context
     * @param url
     * @param titleName
     */
    public static void start(Context context,String url,String titleName,String gameId) {
        Intent starter = new Intent(context, ChargeActivityForWap.class);
        starter.putExtra("url",url);
        starter.putExtra("titleName",titleName);
        if(!TextUtils.isEmpty(gameId)){//非null加入游戏id
            starter.putExtra("gameId",gameId);
        }
        context.startActivity(starter);
    }

    @Override
    public void paySuccess(String orderId, final float money) {
        queryOrder(orderId, money, "支付成功，等待处理");
        L.e(TAG, "回调 支付成功");
    }

    @Override
    public void payFail(String orderId, float money, boolean queryOrder, String msg) {
        if (queryOrder) {
            queryOrder(orderId, money, msg);
        }
        L.e(TAG, "回调 支付失败");
    }

    /**
     * 向服务器查询支付结果
     */
    private void queryOrder(String orderId, final float money, final String msg) {
        //向服务器查询订单结果
        QueryOrderRequestBean queryOrderRequestBean = new QueryOrderRequestBean();
        queryOrderRequestBean.setOrder_id(orderId);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(queryOrderRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<QueryOrderResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(QueryOrderResultBean data) {
                if (paymentListener != null) {
                    if (data != null) {
                        if ("2".equals(data.getStatus())) {
                            if ("2".equals(data.getCpstatus())) {
                                T.s(mContext, "支付成功");
                            } else {
                                T.s(mContext, "支付成功，等待处理");
                            }
                        } else {
                            T.s(mContext, "支付失败 "+msg);
                        }
                    } else {
                        T.s(mContext, "支付失败 "+msg);
                    }
                }
                finish();
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                if (paymentListener != null) {
                    T.s(mContext, "支付失败 "+msg);
                }
                finish();
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        httpCallbackDecode.setLoadMsg("查询支付结果中……");
        RxVolley.post(AppApi.getUrl(AppApi.queryorderApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }
}
