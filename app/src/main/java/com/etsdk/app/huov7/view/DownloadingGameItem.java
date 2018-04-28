package com.etsdk.app.huov7.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.down.ApkDownloadStatus;
import com.etsdk.app.huov7.down.BaseGameDownModel;
import com.etsdk.app.huov7.down.TasksManager;
import com.etsdk.app.huov7.down.TasksManagerModel;
import com.etsdk.app.huov7.model.DownStatusChangeEvent;
import com.etsdk.app.huov7.model.DownTaskDeleteEvent;
import com.etsdk.app.huov7.model.NetConnectEvent;
import com.etsdk.app.huov7.ui.dialog.DownloadingDeleteDialog;
import com.etsdk.app.huov7.util.GameViewUtil;
import com.etsdk.app.huov7.util.IGameLayout;
import com.liang530.log.L;
import com.liang530.utils.BaseTextUtil;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liu hong liang on 2016/12/6.
 */

public class DownloadingGameItem extends FrameLayout implements View.OnClickListener, IGameLayout {
    private static final String TAG = DownloadingGameItem.class.getSimpleName();
    @BindView(R.id.v_line)
    View vLine;
    @BindView(R.id.tv_game_name)
    TextView tvGameName;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.tv_down_status)
    TextView tvDownStatus;
    @BindView(R.id.iv_down_status)
    ImageView ivDownStatus;
    @BindView(R.id.tv_down_size)
    TextView tvDownSize;
    @BindView(R.id.tv_down_speed)
    TextView tvDownSpeed;
    @BindView(R.id.ll_down_option)
    LinearLayout llDownOption;


    private TasksManagerModel tasksManagerModel;
    private boolean isHotRank = false;
    private DownloadingDeleteDialog downloadingDeleteDialog;

    public DownloadingGameItem(Context context) {
        super(context);
        initUI();
    }

    public DownloadingGameItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public DownloadingGameItem(Context context, AttributeSet attrs, int defStyleAttr) {
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
        LayoutInflater.from(getContext()).inflate(R.layout.adapter_downing_manage, this, true);
        ButterKnife.bind(this);
        downloadingDeleteDialog = new DownloadingDeleteDialog();
    }

    public void showLine(boolean showLine) {
        if (showLine) {
            vLine.setVisibility(VISIBLE);
        } else {
            vLine.setVisibility(GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDwonFileStatusChange(DownStatusChangeEvent change) {
        if (tasksManagerModel == null) return;
        if (tasksManagerModel.getGameId() != null && tasksManagerModel.getGameId().equals(change.gameId)) {
            updateUI(change);
            L.e("start", "收到状态改变：" + tasksManagerModel.getGameName());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetConnectEvent(NetConnectEvent event) {
        if (tasksManagerModel == null) return;
        tasksManagerModel = TasksManager.getImpl().getTaskModelByGameId(tasksManagerModel.getGameId());
        if (tasksManagerModel == null) {
            return;
        }
        byte status = FileDownloader.getImpl().getStatus(tasksManagerModel.getUrl(), tasksManagerModel.getPath());
        L.e("start", "收到通知：" + tasksManagerModel.getGameName());
        if (event.type == NetConnectEvent.TYPE_START) {
            if (status == FileDownloadStatus.error || status == FileDownloadStatus.paused) {
                L.e("start", "恢复下载：" + tasksManagerModel.getGameName());
                GameViewUtil.start(this, tasksManagerModel.getOnlyWifi());
            }
        } else {
            if (status == FileDownloadStatus.progress || status == FileDownloadStatus.started || status == FileDownloadStatus.connected) {
                L.e("start", "暂停下载：" + tasksManagerModel.getGameName());
                FileDownloader.getImpl().pause(tasksManagerModel.getId());
            }
        }
    }

    private void updateUI(DownStatusChangeEvent change) {
        if (tasksManagerModel == null) return;
        if (change != null) {
            if (!TextUtils.isEmpty(change.downcnt) && !"0".equals(change.downcnt)) {//不是null不是0
                tasksManagerModel.setDowncnt(change.downcnt);
                String downloadNum = BaseTextUtil.isEmpty(tasksManagerModel.getDowncnt()) ? "0" : tasksManagerModel.getDowncnt();
//                tvTypeCount.setText(getTypeStr(gameBean.getType()) + " | " + downloadNum + "次下载");
            }
        }
        int status = TasksManager.getImpl().getStatus(tasksManagerModel.getGameId());
        if (status == ApkDownloadStatus.PAUSE) {
            ivDownStatus.setImageResource(R.mipmap.resume);
        } else {
            ivDownStatus.setImageResource(R.mipmap.pause);
        }
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(tasksManagerModel.getGameId()));
        restoreDownloadProgressListener();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
        //重新显示到窗口，更新ui
        updateUI(null);
        L.d(TAG, "EventBus register");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
            L.d(TAG, "EventBus unregister");
        }
        if(tasksManagerModel!=null){
//            TasksManager.getImpl().removeDownloadListenerById(tasksManagerModel.getGameId(), downloadListener);
        }
    }

    @Override
    public void setBaseGameDownModel(BaseGameDownModel baseGameDownModel) {
        this.tasksManagerModel = (TasksManagerModel) baseGameDownModel;
        updateUI(null);
        tvGameName.setText(tasksManagerModel.getGameName());
    }

    /**
     * 恢复下载监听,或者注册下载监听
     */
    private void restoreDownloadProgressListener() {
        if (tasksManagerModel != null) {//没有下载过，不用恢复
            int status = TasksManager.getImpl().getStatus(tasksManagerModel.getGameId());
            if (status == ApkDownloadStatus.INITIAL ||
                    status == ApkDownloadStatus.INSTALL ||
                    status == ApkDownloadStatus.OPEN || status == ApkDownloadStatus.RETRY) {
                progressBar.setProgress(100);
            } else {
                progressBar.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
            }
//            TasksManager.getImpl().addDownloadListenerById(tasksManagerModel.getGameId(), downloadListener);
        }
    }


    @OnClick({R.id.ll_down_option, R.id.iv_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_down_option:
                GameViewUtil.clickDownload(this);
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

//    /**
//     * 下载进度监听器
//     */
//    ApklDownloadListener downloadListener = new ApklDownloadListener() {
//        @Override
//        public void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//            if (progressBar != null&&tasksManagerModel!=null) {
//                progressBar.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
//                tvDownSpeed.setText(BaseFileUtil.formatFileSize(task.getSpeed()*1024) + "k/s");
//
//                tvDownSize.setText(BaseFileUtil.formatFileSize(TasksManager.getImpl().getSoFar(task.getId()))+"/"
//                        +BaseFileUtil.formatFileSize(TasksManager.getImpl().getTotal(task.getId())));
//            }
//            L.d(TAG, "下载中：" + task.getId() + "  进度：" + task.getLargeFileSoFarBytes() + "--> " + task.getLargeFileSoFarBytes());
//        }
//    };


    @Override
    public BaseGameDownModel getBaseGameDownModel() {
        return tasksManagerModel;
    }
}
