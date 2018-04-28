package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameDetail;
import com.etsdk.app.huov7.view.NewListGameItem;
import com.etsdk.app.huov7.view.WebView4Scroll;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.log.L;
import com.liang530.log.T;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.liang530.utils.BaseAppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liu hong liang on 2016/8/31.
 * 网页activity
 */
public class WebViewActivity extends ImmerseActivity {
    //是	INT	1 全屏无状态栏无导航栏 2 全屏无状态栏 有导航栏 3 全屏有状态栏无导航栏 4 全屏有状态栏有导航栏
    public static final int TYPE_NO_STATUS_NO_TITLE = 1;
    public static final int TYPE_NO_STATUS_TITLE = 2;
    public static final int TYPE_STATUS_NO_TITLE = 3;
    public static final int TYPE_STATUS_TITLE = 4;
    public final static String WINDOW_TYPE = "window_type";
    public final static String TITLE_NAME = "titleName";
    public final static String URL = "url";
    public final static String WEB_DATA = "webData";
    public final static String URL_PARAMS = "urlParams";
    public final static String GAME_ID = "gameid";
    @BindView(R.id.webView)
    WebView4Scroll webView;
    String titleName;
    String url;
    String urlParams;
    String gameid;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.fl_game)
    FrameLayout flGame;
    @BindView(R.id.progressBar1)
    ProgressBar progressBar1;
    private WebSettings webSettings;
    private String webData;//网页内容
    private int windowType;

    private class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        setupUI();
    }

    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
        }
    }

    //    private CustomProgressDialog dialog;
//
    private void getWebViewData() {
        // WebView加载web资源
        webSettings = webView.getSettings();
        webView.setDownloadListener(new MyWebViewDownLoadListener());
        // 设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        // 设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放

        webSettings.setLoadWithOverviewMode(true);

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // //支持通过JS打开新窗口

        webView.requestFocusFromTouch();

        webSettings.setSupportZoom(true); // 支持缩放

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (url.contains("http:") || url.contains("https:") || url.contains("ftp:")) {
                    return false;
                } else {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    return true;
                }
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
                                       @Override
                                       public void onReceivedTitle(WebView view, String title) {
                                           super.onReceivedTitle(view, title);
                                           if (!TextUtils.isEmpty(titleName)) {
                                               tvTitleName.setText(titleName);
                                           }
                                       }

                                       @Override
                                       public void onProgressChanged(WebView view, int newProgress) {
                                           super.onProgressChanged(view, newProgress);
                                           if (newProgress == 100) {
                                               progressBar1.setVisibility(View.GONE);//加载完网页进度条消失
                                           } else {
                                               progressBar1.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                                               progressBar1.setProgress(newProgress);//设置进度值
                                           }
                                       }
                                   }
        );
        if (TextUtils.isEmpty(webData)) {
            if (TextUtils.isEmpty(urlParams)) {
                webView.loadUrl(url);
            } else {//是安全请求网页
                if (urlParams.startsWith("?")) {
                    urlParams = urlParams.substring(1);
                }
                webView.postUrl(url, urlParams.getBytes());
            }
        } else {
            AppApi.loadMobileHtmlContent(webView, webData);
        }
        L.d(TAG, "url=" + url);
        L.d(TAG, "webData=" + webData);
    }

    private void setupUI() {
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        if (getIntent() != null) {
            titleName = getIntent().getStringExtra(TITLE_NAME);
            url = getIntent().getStringExtra(URL);
            webData = getIntent().getStringExtra(WEB_DATA);
            urlParams = getIntent().getStringExtra(URL_PARAMS);
            gameid = getIntent().getStringExtra(GAME_ID);
            windowType = getIntent().getIntExtra(WINDOW_TYPE, 4);
        }
        //设置是否需要状态栏
        if (windowType == TYPE_NO_STATUS_NO_TITLE || windowType == TYPE_STATUS_NO_TITLE) {
            changeTitleStatus(false);
        } else {
            changeTitleStatus(true);
        }
        if (!TextUtils.isEmpty(titleName)) {
            tvTitleName.setText(titleName);
        }
        if (gameid != null) {
            getGameDetailData();
        }
        if (TextUtils.isEmpty(url) && TextUtils.isEmpty(webData)) {
            T.s(getApplicationContext(), "无效的请求地址");
        } else {
            getWebViewData();
        }
        swrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
                swrefresh.setRefreshing(false);
            }
        });
        webView.setSwipeRefreshLayout(swrefresh);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);//禁掉滑动finish，会和webview冲突
    }

    // 设置回退
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();// goBack()表示返回WebView的上一页面
            return true;
        }
        finish();
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {//防止内存泄漏
            webView.setVisibility(View.GONE);
            webView.removeAllViews();
            ViewParent parent = webView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(webView);
            }
            webView.destroy();
            webView = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
        }
    }

    private void getGameDetailData() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.gameDetail);
        httpParams.put("gameid", gameid);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.gameDetail), new HttpJsonCallBackDialog<GameDetail>() {
            @Override
            public void onDataSuccess(GameDetail data) {
                if (data != null && data.getData() != null) {
                    flGame.setVisibility(View.VISIBLE);
                    NewListGameItem gameView = new NewListGameItem(mContext);
                    gameView.setGameBean(data.getData());
                    flGame.addView(gameView);
                }
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
            }
        });
    }

    public static void start(Context context, String titleName, String url) {
        if (!BaseAppUtil.isOnline(context)) {
            T.s(context, "网络不通，请稍后再试！");
            return;
        }
        L.e("webactivity url: " + url);
        Intent starter = new Intent(context, WebViewActivity.class);
        starter.putExtra(TITLE_NAME, titleName);
        starter.putExtra(URL, url);
        context.startActivity(starter);
    }

    public static void start(Context context, String titleName, String url, String gameId) {
        if (!BaseAppUtil.isOnline(context)) {
            T.s(context, "网络不通，请稍后再试！");
            return;
        }
        Intent starter = new Intent(context, WebViewActivity.class);
        starter.putExtra(TITLE_NAME, titleName);
        starter.putExtra(URL, url);
        starter.putExtra(GAME_ID, gameId);
        context.startActivity(starter);
    }

    public static void startByWebData(Context context, String titleName, String webData) {
        if (!BaseAppUtil.isOnline(context)) {
            T.s(context, "网络不通，请稍后再试！");
            return;
        }
        Intent starter = new Intent(context, WebViewActivity.class);
        starter.putExtra(TITLE_NAME, titleName);
        starter.putExtra(WEB_DATA, webData);
        context.startActivity(starter);
    }

    public static void startPrivateUrl(Context context, String titleName, String url, String urlParams) {
        if (!BaseAppUtil.isOnline(context)) {
            T.s(context, "网络不通，请稍后再试！");
            return;
        }
        Intent starter = new Intent(context, WebViewActivity.class);
        starter.putExtra(TITLE_NAME, titleName);
        starter.putExtra(URL, url);
        starter.putExtra(URL_PARAMS, urlParams);
        context.startActivity(starter);
    }

    public static void start(Context context, String titleName, String url, int windowType) {
        Intent starter = new Intent(context, WebViewActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        starter.putExtra(TITLE_NAME, titleName);
        starter.putExtra(URL, url);
        starter.putExtra(WINDOW_TYPE, windowType);
        context.startActivity(starter);
    }
}
