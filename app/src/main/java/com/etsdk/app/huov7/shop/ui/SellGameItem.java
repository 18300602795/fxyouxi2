package com.etsdk.app.huov7.shop.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.down.BaseDownView;
import com.etsdk.app.huov7.down.DownloadHelper;
import com.etsdk.app.huov7.down.TasksManager;
import com.etsdk.app.huov7.down.TasksManagerModel;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.GameDownRequestBean;
import com.etsdk.app.huov7.model.GameDownResult;
import com.etsdk.app.huov7.ui.SettingActivity;
import com.etsdk.app.huov7.ui.WebViewActivity;
import com.etsdk.app.huov7.ui.dialog.DownAddressSelectDialogUtil;
import com.etsdk.app.huov7.ui.dialog.Open4gDownHintDialog;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.log.T;
import com.liang530.utils.BaseFileUtil;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liu hong liang on 2016/12/6.
 */

public class SellGameItem extends BaseDownView {
    private static final String TAG = SellGameItem.class.getSimpleName();
    @BindView(R.id.tv_game_name)
    TextView tvGameName;
    @BindView(R.id.tv_server)
    TextView tvServer;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.pb_down)
    ProgressBar pbDown;
    @BindView(R.id.tv_down_status)
    TextView tvDownStatus;
    @BindView(R.id.iv_game_icon)
    RoundedImageView ivGameIcon;
    private GameBean gameBean;//游戏本身属性
    Context context;
    private boolean isHotRank;

    public SellGameItem(Context context) {
        super(context);
        this.context = context;
        initUI();
    }

    public SellGameItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initUI();
    }

    public SellGameItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
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
        LayoutInflater.from(getContext()).inflate(R.layout.item_sell_game, this, true);
        ButterKnife.bind(this);
    }

    /**
     *
     * @param gameBean  游戏信息
     * @param servser   区服
     */
    public void setGameBean(GameBean gameBean, String servser) {
        this.gameBean = gameBean;
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
        pbDown.setProgress(100);
        TasksManager.getImpl().addDownloadListenerById(gameBean.getGameid(), this);
        tvGameName.setText(gameBean.getGamename());
//        GlideDisplay.display(ivGameIcon, gameBean.getIcon(), R.mipmap.ic_launcher);
        Glide.with(context).load(gameBean.getIcon()).placeholder(R.mipmap.ic_launcher).into(ivGameIcon);
        tvServer.setText(servser);
        tvDesc.setText(gameBean.getOneword());
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
//        L.e(TAG, tasksManagerModel.getAccont()+" pending");
        tvDownStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
        pbDown.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
    }

    @Override
    public void progress(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {
//        L.e(TAG, tasksManagerModel.getAccont()+" progress");
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
        tvDownStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP,8);
        tvDownStatus.setText(BaseFileUtil.formatFileSize(tasksManagerModel.getSpeed()*1024)+"/s");
        pbDown.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
    }

    @Override
    public void completed(TasksManagerModel tasksManagerModel) {
//        L.e(TAG, tasksManagerModel.getAccont()+" completed");
        tvDownStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
        pbDown.setProgress(100);
    }

    @Override
    public void paused(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {
//        L.e(TAG, tasksManagerModel.getAccont()+" paused");
        tvDownStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));

        pbDown.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
    }

    @Override
    public void error(TasksManagerModel tasksManagerModel, Throwable e) {
//        L.e(TAG, tasksManagerModel.getAccont()+" error");
        tvDownStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
        pbDown.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
    }

    @Override
    public void prepareDown(TasksManagerModel tasksManagerModel, boolean noWifiHint) {
//        L.e(TAG, tasksManagerModel.getAccont()+" prepareDown");
        if (noWifiHint) {//需要提示跳转到设置去打开非wifi下载
            new Open4gDownHintDialog().showDialog(getContext(), new Open4gDownHintDialog.ConfirmDialogListener() {
                @Override
                public void ok() {
                    SettingActivity.start(getContext());
                }

                @Override
                public void cancel() {

                }
            });
            return;
        }
        if (tasksManagerModel == null) {
            tasksManagerModel = new TasksManagerModel();
            tasksManagerModel.setGameId(gameBean.getGameid());
            tasksManagerModel.setGameIcon(gameBean.getIcon());
            tasksManagerModel.setGameName(gameBean.getGamename());
            tasksManagerModel.setOnlyWifi(noWifiHint == true ? 0 : 1);
            tasksManagerModel.setGameType(gameBean.getType());
//            tasksManagerModel.setUrl(gameBean.getDownlink());
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
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.gameDownApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }


    @Override
    public void netOff() {

    }

    @Override
    public void delete() {
        tvDownStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
        pbDown.setProgress(100);

    }

    @Override
    public void netRecover() {

    }

    @Override
    public void installSuccess() {
        tvDownStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
        pbDown.setProgress(100);
    }

    @OnClick({R.id.tv_down_status})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_down_status:
                if (gameBean == null) {
                    return;
                }
                DownloadHelper.onClick(gameBean.getGameid(), this);
                break;
        }
    }
}
