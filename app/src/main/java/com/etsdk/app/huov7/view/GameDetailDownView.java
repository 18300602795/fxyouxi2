package com.etsdk.app.huov7.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.down.ApklDownloadListener;
import com.etsdk.app.huov7.down.DownloadHelper;
import com.etsdk.app.huov7.down.TasksManager;
import com.etsdk.app.huov7.down.TasksManagerModel;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.GameDownRequestBean;
import com.etsdk.app.huov7.model.GameDownResult;
import com.etsdk.app.huov7.model.NotifyShareOkRequestBean;
import com.etsdk.app.huov7.model.ShareResultBean;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.etsdk.app.huov7.sharesdk.ShareDataEvent;
import com.etsdk.app.huov7.sharesdk.ShareUtil;
import com.etsdk.app.huov7.ui.LoginActivityV1;
import com.etsdk.app.huov7.ui.WebViewActivity;
import com.etsdk.app.huov7.ui.dialog.DownAddressSelectDialogUtil;
import com.game.sdk.SdkConstant;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.log.L;
import com.liang530.log.T;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created by liu hong liang on 2017/1/19.
 */

public class GameDetailDownView extends FrameLayout implements ApklDownloadListener {


    private static final String TAG = GameDetailDownView.class.getSimpleName();
    @BindView(R.id.pb_down)
    ProgressBar pbDown;
    @BindView(R.id.tv_down_status)
    TextView tvDownStatus;
    @BindView(R.id.tv_share)
    LinearLayout tvShare;
    private GameBean gameBean;
    private ShareResultBean.DateBean shareResult;
    private UserInfoResultBean resultBean;

    public GameDetailDownView(Context context) {
        super(context);
        initUI();
    }

    public GameDetailDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public GameDetailDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    private void initUI() {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            setLayoutParams(layoutParams);
        } else {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        LayoutInflater.from(getContext()).inflate(R.layout.detail_bottom_game_down, this, true);
        ButterKnife.bind(this);
    }

    public void setGameBean(GameBean gameBean) {
        this.gameBean = gameBean;
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
        if ("下载".equals(tvDownStatus.getText().toString().trim()) && !TextUtils.isEmpty(gameBean.getSize())) {
            tvDownStatus.setText("下载（" + gameBean.getSize() + "）");
        }
        pbDown.setProgress(100);
        TasksManager.getImpl().addDownloadListenerById(gameBean.getGameid(), this);
//        getShareData(false);//暂时不要分享2017/5/24
//        getUserInfoData(false);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (gameBean == null) return;
        TasksManager.getImpl().addDownloadListenerById(gameBean.getGameid(), this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (gameBean == null) return;
        TasksManager.getImpl().removeDownloadListenerById(gameBean.getGameid(), this);
    }

    @Override
    public void pending(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {
//        L.e(TAG, tasksManagerModel.getGameName()+" pending");
        pbDown.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(tasksManagerModel.getGameId()));
    }

    @Override
    public void progress(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {
//        L.e(TAG, tasksManagerModel.getGameName()+" progress");
        pbDown.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
        tvDownStatus.setText(TasksManager.getImpl().getProgress(tasksManagerModel.getId()) + "%");
    }

    @Override
    public void completed(TasksManagerModel tasksManagerModel) {
//        L.e(TAG, tasksManagerModel.getGameName()+" completed");
        pbDown.setProgress(100);
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(tasksManagerModel.getGameId()));
    }

    @Override
    public void paused(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {
//        L.e(TAG, tasksManagerModel.getGameName()+" paused");
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));

        pbDown.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
    }

    @Override
    public void error(TasksManagerModel tasksManagerModel, Throwable e) {
//        L.e(TAG, tasksManagerModel.getGameName()+" error");
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
        pbDown.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
    }

    @Override
    public void prepareDown(TasksManagerModel tasksManagerModel, boolean noWifiHint) {
//        L.e(TAG, tasksManagerModel.getGameName()+" prepareDown");
        if (tasksManagerModel == null) {
            tasksManagerModel = new TasksManagerModel();
            tasksManagerModel.setGameId(gameBean.getGameid());
            tasksManagerModel.setGameIcon(gameBean.getIcon());
            tasksManagerModel.setGameName(gameBean.getGamename());
            tasksManagerModel.setOnlyWifi(noWifiHint == true ? 0 : 1);
            tasksManagerModel.setGameType(gameBean.getType());
            getDownUrl(tasksManagerModel);
        } else {
            DownloadHelper.start(tasksManagerModel);
        }
    }

    private void getDownUrl(final TasksManagerModel tasksManagerModel) {
        final GameDownRequestBean gameDownRequestBean = new GameDownRequestBean();
        gameDownRequestBean.setGameid(tasksManagerModel.getGameId());
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(gameDownRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<GameDownResult>(getContext(), httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(final GameDownResult data) {
                if (data != null) {
                    if (data.getList() != null && data.getList().size() != 0) {
                        if (data.getList().size() == 1) {//只有一个直接下载
                            GameDownResult.GameDown gameDown = data.getList().get(0);
                            if ("1".equals(gameDown.getType())) {//可以直接下载
                                if (!TextUtils.isEmpty(gameDown.getUrl())) {
                                    tasksManagerModel.setUrl(gameDown.getUrl());
                                    tasksManagerModel.setDowncnt(data.getDowncnt() + "");
                                    DownloadHelper.start(tasksManagerModel);
                                } else {
                                    T.s(getContext(), "暂无下载地址");
                                }
                            } else {//跳转到网页下载
                                WebViewActivity.start(getContext(), "游戏下载", gameDown.getUrl());
                            }
                        } else {//多个下载地址，弹出选择
                            //弹出对话框，进行地址选择
                            DownAddressSelectDialogUtil.showAddressSelectDialog(getContext(), data.getList(), new DownAddressSelectDialogUtil.SelectAddressListener() {
                                @Override
                                public void downAddress(String url) {
                                    tasksManagerModel.setUrl(url);
                                    tasksManagerModel.setDowncnt(data.getDowncnt() + "");
                                    DownloadHelper.start(tasksManagerModel);
                                }
                            });
                        }
                    } else {
                        T.s(getContext(), "暂无下载地址");
                    }
                } else {
                    T.s(getContext(), "暂无下载地址");
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.gameDownApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    @Override
    public void netOff() {

    }

    @Override
    public void delete() {
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
        pbDown.setProgress(100);

    }

    @Override
    public void netRecover() {

    }

    @Override
    public void installSuccess() {
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
        pbDown.setProgress(100);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @OnClick({R.id.pb_down, R.id.tv_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pb_down:
                DownloadHelper.onClick(gameBean.getGameid(), this);
                break;
            case R.id.tv_share:
                L.i("333", "开始分享");
                if (resultBean == null) {
//                    getShareData(true);
                    getUserInfoData(true);
//                    T.s(getContext(), "分享失败，请稍后再试");
                } else {
//                    showShare();
                    share();
                }
                break;
        }
    }

    private void share() {
        String url = "http://tui.idielian.com/agent.php/Front/Appdown/prelink/name/";
        if (resultBean.getName().equals("官网")) {
            url = "http://fxyouxi.com/index.html#/home";
        } else {
            String name_code = Base64.encodeToString(resultBean.getName().getBytes(), Base64.DEFAULT);
            url = url + name_code;
        }
        L.i("333", "downlink：" + url);
        ShareUtil.showShare(getContext(), gameBean.getGamename(), gameBean.getWelfare() == null ? gameBean.getOneword() : gameBean.getWelfare(), gameBean.getIcon(), url);
    }


    private void notifyShareOk(String shareid) {
        final NotifyShareOkRequestBean notifyShareOkRequestBean = new NotifyShareOkRequestBean();
        notifyShareOkRequestBean.setShareid(shareid);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(notifyShareOkRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<ShareResultBean>(getContext(), httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(ShareResultBean data) {
            }
        };
        httpCallbackDecode.setShowTs(false);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.shareNotifyApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    public void getUserInfoData(final boolean isShare) {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserInfoResultBean>(getContext(), httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                if (data != null) {
                    resultBean = data;
                    if (isShare) {
                        share();
                    }
                }
            }

            @Override
            public void onFailure(String code, String msg) {

            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userDetailApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    private void getShareData(final boolean isShow) {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.shareDetailApi);
        httpParams.put("gameid", SdkConstant.HS_APPID);
        L.i("333", "gameid：" + AppApi.getUrl(AppApi.shareDetailApi));
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.shareDetailApi), new HttpJsonCallBackDialog<ShareResultBean>() {
            @Override
            public void onDataSuccess(ShareResultBean data) {
                if (data != null && data.getData() != null) {
                    shareResult = data.getData();
                    if (isShow) {
//                        share();
                    }
                } else {
                    LoginActivityV1.start(getContext());
                }
            }
        });
    }

    private void showShare() {
        ShareDataEvent event = new ShareDataEvent();
        event.text = shareResult.getSharetext();
        event.title = shareResult.getTitle();
        event.titleUrl = shareResult.getUrl();
        event.url = shareResult.getUrl();
        event.resouceId = R.mipmap.ic_launcher;
        ShareUtil.setCurrentShareTitle("邀请好友");
        new ShareUtil().oneKeyShare(getContext().getApplicationContext(), event, new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                L.e(TAG, "分享成功！：" + hashMap);
                notifyShareOk(shareResult.getShareid());
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                L.e(TAG, "失败！");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                L.e(TAG, "取消！");
            }
        });
    }
}
