package com.etsdk.app.huov7.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.down.BaseDownView;
import com.etsdk.app.huov7.down.DownloadHelper;
import com.etsdk.app.huov7.down.TasksManager;
import com.etsdk.app.huov7.down.TasksManagerModel;
import com.etsdk.app.huov7.ui.DownloadingManagerActivity;
import com.etsdk.app.huov7.ui.GameDetailV2Activity;
import com.etsdk.app.huov7.util.GameViewUtil;
import com.liang530.log.L;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.etsdk.app.huov7.R.id.tv_down_status;

/**
 * 2017/5/6.
 */

public class DownloadingListItem extends BaseDownView {
    private static final String TAG = DownloadingListItem.class.getSimpleName();
    @BindView(R.id.v_line)
    View vLine;
    @BindView(R.id.iv_game_img)
    RoundedImageView ivGameImg;
    @BindView(R.id.tv_game_name)
    TextView tvGameName;
    @BindView(R.id.gameTagView)
    GameTagView gameTagView;
    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.speed_tv)
    TextView tvSpeed;
    @BindView(R.id.pb_down)
    ProgressBar pbDown;
    @BindView(tv_down_status)
    public TextView tvDownStatus;
    @BindView(R.id.v_time_line)
    View vTimeLine;
    @BindView(R.id.game_list_item)
    RelativeLayout gameListItem;
    @BindView(R.id.iv_check)
    ImageView ivCheck;
    private double lastBytes;
    private long time;

    private TasksManagerModel model;
    public boolean isEdit = false;

    Context context;
    public DownloadingListItem(Context context) {
        super(context);
        this.context = context;
        initUI();
    }

    public DownloadingListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initUI();
    }

    public DownloadingListItem(Context context, AttributeSet attrs, int defStyleAttr) {
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
        LayoutInflater.from(getContext()).inflate(R.layout.item_download_list, this, true);
        ButterKnife.bind(this);
    }

    public void setModel(TasksManagerModel model) {
        this.model = model;
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(model.getGameId()));
        pbDown.setProgress(100);
        TasksManager.getImpl().addDownloadListenerById(model.getGameId(), this);
        tvGameName.setText(model.getGameName());
        tvSpeed.setVisibility(GONE);
//        GlideDisplay.display(ivGameImg, model.getGameIcon(), R.mipmap.ic_launcher);
        Glide.with(context).load(model.getGameIcon()).placeholder(R.mipmap.ic_launcher).into(ivGameImg);
        tvProgress.setText(TasksManager.getImpl().getDescText(model.getGameId()));

        gameTagView.setGameType(model.getGameType());
    }

    public void showLine(boolean showLine) {
        if (showLine) {
            vLine.setVisibility(VISIBLE);
        } else {
            vLine.setVisibility(GONE);
        }
    }

    /**
     * 是否显示左边复选图标
     * @param show
     */
    public void showCheck(boolean show){
        isEdit = show;
        if(show){
            ivCheck.setVisibility(VISIBLE);
        }else{
            ivCheck.setVisibility(GONE);
            model.isSelected = false;
        }
    }

    /**
     * 是否被选中
     * @param check
     */
    public void setCheck(boolean check){
        if(check){
            ivCheck.setBackgroundResource(R.mipmap.yes);
        }else{
            ivCheck.setBackgroundResource(R.mipmap.no);
        }
    }

    public boolean isCheck(){
        return model.isSelected;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (model == null) return;
        TasksManager.getImpl().addDownloadListenerById(model.getGameId(), this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (model == null) return;
        TasksManager.getImpl().removeDownloadListenerById(model.getGameId(), this);
    }

    @Override
    public void pending(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {
        pbDown.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
        tvProgress.setText(TasksManager.getImpl().getDescText(model.getGameId()));
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(model.getGameId()));
        updateDownLoadManagerActivity();
    }

    @Override
    public void progress(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {
        tvSpeed.setVisibility(VISIBLE);
        if (TasksManager.getImpl().getSoFar(tasksManagerModel.getId()) == 0){
            lastBytes = 0;
        }else if (TasksManager.getImpl().getSoFar(tasksManagerModel.getId()) == soFarBytes){
            if (lastBytes == 0){
                lastBytes = soFarBytes;
            }
        }
        if (time == 0){
            time = System.currentTimeMillis();
        }
        double difBytes = soFarBytes - lastBytes;
        float difTime = ((System.currentTimeMillis() - time) / (float)1000);
        lastBytes = soFarBytes;
        time = System.currentTimeMillis();
        if (difBytes > 0){
            if (difTime == 0){
                tvSpeed.setText(GameViewUtil.getFormatSize(difBytes) + "/s");
            }else {
                tvSpeed.setText(GameViewUtil.getFormatSize(difBytes / difTime) + "/s");
            }

        }else {
            tvSpeed.setText(tvSpeed.getText().toString());
        }
        pbDown.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
        tvProgress.setText(TasksManager.getImpl().getDescText(model.getGameId()) + " " +
                TasksManager.getImpl().getProgress(tasksManagerModel.getId()) + "%");
        tvDownStatus.setText(TasksManager.getImpl().getProgress(tasksManagerModel.getId()) + "%");
    }

    @Override
    public void completed(TasksManagerModel tasksManagerModel) {
        pbDown.setProgress(100);
        tvProgress.setText(TasksManager.getImpl().getDescText(model.getGameId()));
        updateDownLoadManagerActivity();
        tvSpeed.setVisibility(GONE);
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(model.getGameId()));
    }

    @Override
    public void paused(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(model.getGameId()));
        tvProgress.setText(TasksManager.getImpl().getDescText(model.getGameId()));
        tvSpeed.setText("0.00KB/s");
        pbDown.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
    }

    @Override
    public void error(TasksManagerModel tasksManagerModel, Throwable e) {
        pbDown.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
        tvProgress.setText(TasksManager.getImpl().getDescText(model.getGameId()));
        tvSpeed.setVisibility(GONE);
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(model.getGameId()));
    }

    /**
     * 当前是下载管理界面，需要更新界面
     */
    private void updateDownLoadManagerActivity() {
        Context context = getContext();
        L.e(TAG + " 当前是下载管理界面，需要更新界面" + context);
        if (context instanceof DownloadingManagerActivity) {
            L.e(TAG + " 当前是下载管理界面，需要更新界面,调用了更新方法");
            ((DownloadingManagerActivity) context).updateDownListData();
        }
    }

    @Override
    public void netOff() {

    }

    @Override
    public void delete() {
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(model.getGameId()));
        pbDown.setProgress(100);

    }

    @Override
    public void netRecover() {

    }

    @Override
    public void installSuccess() {
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(model.getGameId()));
        pbDown.setProgress(100);
        tvProgress.setText(TasksManager.getImpl().getDescText(model.getGameId()));
    }

//    @OnClick({R.id.tv_down_status, R.id.game_list_item})
    public void onClick(View view) {
        switch (view.getId()) {
            case tv_down_status:
                if (model == null) {
                    return;
                }
                if(isEdit){//编辑状态，
                    setCheck(!model.isSelected);
                    model.isSelected = !model.isSelected;
                }else {//未处于编辑状态
                    DownloadHelper.onClick(model.getGameId(), this);
                }
                break;
            case R.id.game_list_item:
                if (model == null) {
                    return;
                }
                if(!isEdit) {//未处于编辑状态，跳转
                    GameDetailV2Activity.start(getContext(), model.getGameId());
                }else{//编辑状态，
                    setCheck(!model.isSelected);
                    model.isSelected = !model.isSelected;
                }
                break;
        }
    }

}
