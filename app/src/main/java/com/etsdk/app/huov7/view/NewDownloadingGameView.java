package com.etsdk.app.huov7.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.down.BaseDownView;
import com.etsdk.app.huov7.down.DownloadHelper;
import com.etsdk.app.huov7.down.TasksManager;
import com.etsdk.app.huov7.down.TasksManagerModel;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.DownStatusChangeEvent;
import com.etsdk.app.huov7.model.DownTaskDeleteEvent;
import com.etsdk.app.huov7.model.GameDownRequestBean;
import com.etsdk.app.huov7.model.GameDownResult;
import com.etsdk.app.huov7.ui.DownloadManagerActivity;
import com.etsdk.app.huov7.ui.SettingActivity;
import com.etsdk.app.huov7.ui.WebViewActivity;
import com.etsdk.app.huov7.ui.dialog.DownAddressSelectDialogUtil;
import com.etsdk.app.huov7.ui.dialog.DownloadingDeleteDialog;
import com.etsdk.app.huov7.ui.dialog.Open4gDownHintDialog;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.log.T;
import com.liang530.utils.BaseFileUtil;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liu hong liang on 2017/1/13.
 */

public class NewDownloadingGameView extends BaseDownView {
    @BindView(R.id.v_line)
    View vLine;
    @BindView(R.id.tv_game_name)
    TextView tvGameName;
    @BindView(R.id.tv_down_size)
    TextView tvDownSize;
    @BindView(R.id.tv_down_speed)
    TextView tvDownSpeed;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.iv_down_status)
    ImageView ivDownStatus;
    @BindView(R.id.tv_down_status)
    TextView tvDownStatus;
    @BindView(R.id.ll_down_option)
    LinearLayout llDownOption;
    @BindView(R.id.iv_game_img)
    RoundedImageView ivGameImg;
    private TasksManagerModel tasksManagerModel;
    private DownloadingDeleteDialog downloadingDeleteDialog;

    Context context;
    public NewDownloadingGameView(Context context) {
        super(context);
        this.context = context;
        initUI();
    }

    public NewDownloadingGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initUI();
    }

    public NewDownloadingGameView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        LayoutInflater.from(getContext()).inflate(R.layout.adapter_downing_manage, this, true);
        ButterKnife.bind(this);
        downloadingDeleteDialog = new DownloadingDeleteDialog();
    }

    public void setTasksManagerModel(TasksManagerModel tasksManagerModel) {
        this.tasksManagerModel = tasksManagerModel;
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(tasksManagerModel.getGameId()));
        progressBar.setProgress(100);
        tvDownSpeed.setText(BaseFileUtil.formatFileSize(tasksManagerModel.getSpeed() * 1024) + "/s");
        tvDownSize.setText(BaseFileUtil.formatFileSize(TasksManager.getImpl().getSoFar(tasksManagerModel.getId())) + "/"
                + BaseFileUtil.formatFileSize(TasksManager.getImpl().getTotal(tasksManagerModel.getId())));
        TasksManager.getImpl().addDownloadListenerById(tasksManagerModel.getGameId(), this);
        tvGameName.setText(tasksManagerModel.getGameName());
//        GlideDisplay.display(ivGameImg,tasksManagerModel.getGameIcon(),R.mipmap.ic_launcher);
        Glide.with(context).load(tasksManagerModel.getGameIcon()).placeholder(R.mipmap.ic_launcher).into(ivGameImg);
    }

    @OnClick({R.id.ll_down_option, R.id.iv_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_down_option:
                DownloadHelper.onClick(tasksManagerModel.getGameId(), this);
                break;
            case R.id.iv_delete:
                downloadingDeleteDialog.showDialog(getContext(), new DownloadingDeleteDialog.ConfirmDialogListener() {
                    @Override
                    public void ok() {
                        TasksManager.getImpl().deleteTaskByModel(tasksManagerModel);
                        //通知刷新
                        EventBus.getDefault().post(new DownStatusChangeEvent(tasksManagerModel.getId(), tasksManagerModel.getGameId(), null));
                        EventBus.getDefault().post(new DownTaskDeleteEvent(tasksManagerModel));

                    }

                    @Override
                    public void cancel() {

                    }
                });
                break;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (tasksManagerModel == null) return;
        TasksManager.getImpl().addDownloadListenerById(tasksManagerModel.getGameId(), this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (tasksManagerModel == null) return;
        TasksManager.getImpl().removeDownloadListenerById(tasksManagerModel.getGameId(), this);
    }

    @Override
    public void pending(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {
        progressBar.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
//        tvDownSpeed.setText(BaseFileUtil.formatFileSize(task.getSpeed()*1024) + "k/s");
        tvDownSize.setText(BaseFileUtil.formatFileSize(TasksManager.getImpl().getSoFar(tasksManagerModel.getId())) + "/"
                + BaseFileUtil.formatFileSize(TasksManager.getImpl().getTotal(tasksManagerModel.getId())));
        ivDownStatus.setImageResource(R.mipmap.pause);
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(tasksManagerModel.getGameId()));
    }

    @Override
    public void progress(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(tasksManagerModel.getGameId()));
        progressBar.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
        tvDownSpeed.setText(BaseFileUtil.formatFileSize(tasksManagerModel.getSpeed() * 1024) + "/s");
        tvDownSize.setText(BaseFileUtil.formatFileSize(TasksManager.getImpl().getSoFar(tasksManagerModel.getId())) + "/"
                + BaseFileUtil.formatFileSize(TasksManager.getImpl().getTotal(tasksManagerModel.getId())));
        ivDownStatus.setImageResource(R.mipmap.pause);
    }

    @Override
    public void completed(TasksManagerModel tasksManagerModel) {
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(tasksManagerModel.getGameId()));
        progressBar.setProgress(100);
        ivDownStatus.setVisibility(INVISIBLE);
        Context context = getContext();
        if(context instanceof DownloadManagerActivity){
            ((DownloadManagerActivity) context).updateDownListData();
        }
    }

    @Override
    public void paused(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(tasksManagerModel.getGameId()));
        progressBar.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
        ivDownStatus.setImageResource(R.mipmap.resume);
    }

    @Override
    public void error(TasksManagerModel tasksManagerModel, Throwable e) {
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(tasksManagerModel.getGameId()));
        progressBar.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
        ivDownStatus.setImageResource(R.mipmap.resume);
    }

    @Override
    public void prepareDown(TasksManagerModel tasksManagerModel, boolean noWifiHint) {
        if(noWifiHint){//需要提示跳转到设置去打开非wifi下载
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
            return;
        } else {
            DownloadHelper.start(tasksManagerModel);
        }
    }

    @Override
    public void netOff() {

    }

    @Override
    public void delete() {

    }

    @Override
    public void netRecover() {

    }

    @Override
    public void installSuccess() {
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(tasksManagerModel.getGameId()));
        progressBar.setProgress(100);
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
                                tasksManagerModel.setUrl(gameDown.getUrl());
                                tasksManagerModel.setDowncnt(data.getDowncnt() + "");
                                DownloadHelper.start(tasksManagerModel);
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
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.gameDownApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }
}
