package com.etsdk.app.huov7.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.down.BaseDownView;
import com.etsdk.app.huov7.down.TasksManager;
import com.etsdk.app.huov7.down.TasksManagerModel;
import com.etsdk.app.huov7.ui.DownloadManagerActivityNew;
import com.liang530.log.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 下载管理--》下载页面--》顶部正在下载条目View
 * 2017/5/05.
 */

public class DownloadingGamesView extends BaseDownView {

    @BindView(R.id.tv_game_name)
    TextView tvGameName;
    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.tv_down_num)
    TextView tvDownNum;
    @BindView(R.id.iv_go)
    ImageView ivGo;
    private List<TasksManagerModel> tasksManagerModelList = new ArrayList<>();

    public DownloadingGamesView(Context context) {
        super(context);
        initUI();
    }

    public DownloadingGamesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public DownloadingGamesView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        LayoutInflater.from(getContext()).inflate(R.layout.view_games_downing, this, true);
        ButterKnife.bind(this);
        initTaskList();
    }

    public void initTaskList() {
        tasksManagerModelList = TasksManager.getImpl().getAllDownloadingTasks();
        L.e("正在下载 "+tasksManagerModelList);
        if (tasksManagerModelList.size() == 0) {
            tvProgress.setText("");
            tvGameName.setText("无下载任务");
            tvDownNum.setText("0");
            ivGo.setVisibility(INVISIBLE);
            return;
        }
//        TasksManager.getImpl().addDownloadListenerById(tasksManagerModelList.get(0).getGameId(), this);
        for(TasksManagerModel model: tasksManagerModelList){
            TasksManager.getImpl().addDownloadListenerById(model.getGameId(), this);
        }
        tvGameName.setText(tasksManagerModelList.get(0).getGameName());
        tvDownNum.setText(tasksManagerModelList.size() + "");
        tvProgress.setText("已下载 " + TasksManager.getImpl().getProgress(tasksManagerModelList.get(0).getId()) + "%");
        ivGo.setVisibility(VISIBLE);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (tasksManagerModelList.size() == 0) return;
        TasksManager.getImpl().addDownloadListenerById(tasksManagerModelList.get(0).getGameId(), this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (tasksManagerModelList.size() == 0) return;
        TasksManager.getImpl().removeDownloadListenerById(tasksManagerModelList.get(0).getGameId(), this);
    }

    @Override
    public void pending(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {
        tvProgress.setText("已下载 0%");
        tvGameName.setText("准备中..");
        tvDownNum.setText(TasksManager.getImpl().getAllDownloadingTasks().size() + "");
    }

    @Override
    public void progress(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {
        TasksManagerModel model = TasksManager.getImpl().getFirstDownloadingTasks();
        String progress = model == null ? "" : "已下载 " + TasksManager.getImpl().getProgress(model.getId()) + "%";
        String name = model == null ? "无下载任务" : model.getGameName();
        tvProgress.setText(progress);
        tvGameName.setText(name);
        tvDownNum.setText(TasksManager.getImpl().getAllDownloadingTasks().size() + "");
    }

    @Override
    public void completed(TasksManagerModel tasksManagerModel) {
        L.e("正在下载 完成 "+tasksManagerModel.getGameName());
//        if (TasksManager.getImpl().getAllDownloadingTasks().size() == 0) {
//            tvProgress.setText("");
//            tvGameName.setText("无下载任务");
//            tvDownNum.setText("0");
//            ivGo.setVisibility(INVISIBLE);
//        } else {
            initTaskList();
//        }
        updateDownLoadManagerActivity();
    }

    @Override
    public void delete() {
//        if (TasksManager.getImpl().getAllDownloadingTasks().size() == 0) {
//            tvProgress.setText("");
//            tvGameName.setText("无下载任务");
//            tvDownNum.setText("0");
//            ivGo.setVisibility(INVISIBLE);
//        } else {
            initTaskList();
//        }
        updateDownLoadManagerActivity();
    }

    /**
     * 当前是下载管理界面，需要更新界面
     */
    private void updateDownLoadManagerActivity() {
        Context context = getContext();
        L.e(" 当前是下载管理界面，需要更新界面");
        if (context instanceof DownloadManagerActivityNew) {
            L.e(" 当前是下载管理界面，需要更新界面,调用了更新方法");
            ((DownloadManagerActivityNew) context).updateDownListData();
        }
    }

    public int getDownloadingSize() {
        return tasksManagerModelList.size();
    }

}
