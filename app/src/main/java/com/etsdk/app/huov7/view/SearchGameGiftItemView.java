package com.etsdk.app.huov7.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.etsdk.app.huov7.ui.DownloadManagerActivity;
import com.etsdk.app.huov7.ui.GiftListActivity;
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

public class SearchGameGiftItemView extends BaseDownView {
    private static final String TAG = SearchGameGiftItemView.class.getSimpleName();
    @BindView(R.id.v_line)
    View vLine;
    @BindView(R.id.tv_hot_rank)
    TextView tvHotRank;
    @BindView(R.id.ll_time_line)
    LinearLayout llTimeLine;
    @BindView(R.id.iv_game_img)
    RoundedImageView ivGameImg;
    @BindView(R.id.tv_game_name)
    TextView tvGameName;
    @BindView(R.id.tv_rate)
    TextView tvRate;
    @BindView(R.id.tv_gift)
    TextView tvGift;
    @BindView(R.id.tv_source)
    TextView tvSource;
    @BindView(R.id.ll_game_name)
    LinearLayout llGameName;
    @BindView(R.id.gameTagView)
    GameTagView gameTagView;
    @BindView(R.id.tv_oneword)
    TextView tvOneword;
    @BindView(R.id.pb_down)
    ProgressBar pbDown;
    @BindView(R.id.tv_down_status)
    TextView tvDownStatus;
    @BindView(R.id.tv_size)
    TextView tvSize;
    @BindView(R.id.btn_gift)
    Button btnGift;
    @BindView(R.id.v_time_line)
    View vTimeLine;
    @BindView(R.id.game_list_item)
    RelativeLayout gameListItem;

    private GameBean gameBean;//游戏本身属性
    private boolean isHotRank;

    Context context;
    public SearchGameGiftItemView(Context context) {
        super(context);
        this.context = context;
        initUI();
    }

    public SearchGameGiftItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initUI();
    }

    public SearchGameGiftItemView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        LayoutInflater.from(getContext()).inflate(R.layout.item_search_game_gift_item, this, true);
        ButterKnife.bind(this);
    }

    public void setGameBean(GameBean gameBean) {
        if (!"0".equals(gameBean.getGiftcnt())) {
            tvSize.setTextColor(getResources().getColor(R.color.text_red));
            tvSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tvSize.setText("礼包数*"+gameBean.getGiftcnt());
        } else {
            tvSize.setTextColor(getResources().getColor(R.color.text_gray));
            tvSize.setText("暂无礼包");
            tvSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }
        this.gameBean = gameBean;
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
        pbDown.setProgress(100);
        TasksManager.getImpl().addDownloadListenerById(gameBean.getGameid(), this);
        tvGameName.setText(gameBean.getGamename());
        tvOneword.setText(gameBean.getOneword());
//        GlideDisplay.display(ivGameImg, gameBean.getIcon(), R.mipmap.ic_launcher);
        Glide.with(context).load(gameBean.getIcon()).placeholder(R.mipmap.ic_launcher).into(ivGameImg);
        gameTagView.setGameType(gameBean.getType());

        //来源
//        if(){
//            tvSource.setVisibility(VISIBLE);
//            tvSource.setText();
//        }else{
//            tvSource.setVisibility(GONE);
//        }
    }

    /**
     * 设置游戏状态信息显示，如开服信息，或者开测信息(主要在开服列表和开测列表设置)
     */
    public void setGameStatusInfo(String statusInfo, Integer color) {
        tvOneword.setText(statusInfo);
        if (color != null) {
            tvOneword.setTextColor(color);
        }
    }

    public void setIsHotRank(boolean isHotRank, int position) {
        this.isHotRank = isHotRank;
        if (isHotRank) {
            tvHotRank.setVisibility(VISIBLE);
            if (position <= 3) {
                if (position == 1) {
                    tvHotRank.setBackgroundResource(R.mipmap.no1);
                } else if (position == 2) {
                    tvHotRank.setBackgroundResource(R.mipmap.no2);
                } else {
                    tvHotRank.setBackgroundResource(R.mipmap.no3);
                }
//                layoutParams.width = layoutParams.height = BaseAppUtil.dip2px(tvHotRank.getContext(), 40);
                tvHotRank.setText("");
            } else {
                tvHotRank.setText(position + "");
                tvHotRank.setBackgroundColor(Color.WHITE);
            }
        } else {
            tvHotRank.setVisibility(GONE);
        }
    }

    public void showLine(boolean showLine) {
        if (showLine) {
            vLine.setVisibility(VISIBLE);
        } else {
            vLine.setVisibility(GONE);
        }
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
        tvDownStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
        pbDown.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
        updateDownLoadManagerActivity();
    }

    @Override
    public void progress(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {
//        L.e(TAG, tasksManagerModel.getAccont()+" progress");
//        tvDownStatus.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
        tvDownStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
        tvDownStatus.setText(BaseFileUtil.formatFileSize(tasksManagerModel.getSpeed() * 1024) + "/s");
        pbDown.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
    }

    @Override
    public void completed(TasksManagerModel tasksManagerModel) {
//        L.e(TAG, tasksManagerModel.getAccont()+" completed");
        tvDownStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
        pbDown.setProgress(100);
        updateDownLoadManagerActivity();
    }

    @Override
    public void paused(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {
//        L.e(TAG, tasksManagerModel.getAccont()+" paused");
        tvDownStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));

        pbDown.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
    }

    @Override
    public void error(TasksManagerModel tasksManagerModel, Throwable e) {
//        L.e(TAG, tasksManagerModel.getAccont()+" error");
        tvDownStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
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

    /**
     * 当前是下载管理界面，需要更新界面
     */
    private void updateDownLoadManagerActivity() {
        Context context = getContext();
        if (context instanceof DownloadManagerActivity) {
            ((DownloadManagerActivity) context).updateDownListData();
        }
    }

    @Override
    public void netOff() {

    }

    @Override
    public void delete() {
        tvDownStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
        pbDown.setProgress(100);

    }

    @Override
    public void netRecover() {

    }

    @Override
    public void installSuccess() {
        tvDownStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
        pbDown.setProgress(100);
    }

    @OnClick({R.id.game_list_item})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.game_list_item:
                if (gameBean == null||"0".equals(gameBean.getGiftcnt())) {
                    return;
                }
                GiftListActivity.start(getContext(), gameBean.getGamename(), gameBean.getGameid(), 0, 0, 0, 0);
//                GameDetailV2Activity.start(getContext(), gameBean.getGameid());
//                NewGameDetailActivity.start(getContext(),gameBean.getGameid());
                break;
        }
    }
}
