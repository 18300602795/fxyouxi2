package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.DownCompliteListAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.down.ApkDownloadStatus;
import com.etsdk.app.huov7.down.TasksManager;
import com.etsdk.app.huov7.down.TasksManagerModel;
import com.etsdk.app.huov7.model.DownInstallSuccessEvent;
import com.etsdk.app.huov7.ui.dialog.DownloadingDeleteDialog;
import com.etsdk.app.huov7.view.DownloadingGamesView;
import com.liang530.log.L;
import com.liang530.log.T;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DownloadManagerActivityNew extends ImmerseActivity implements DownCompliteListAdapter.SelectListener, DownloadingDeleteDialog.ConfirmDialogListener {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.iv_edit)
    TextView ivEdit;
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.downloading_games)
    DownloadingGamesView downloadingGames;
    @BindView(R.id.rcy_download_complite)
    RecyclerView rcyDownloadComplite;
    @BindView(R.id.tv_select_all)
    TextView tvSelectAll;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.ll_edit_menu)
    LinearLayout llEditMenu;
    @BindView(R.id.activity_down_load_manager)
    LinearLayout activityDownLoadManager;
    private DownCompliteListAdapter downCompliteListAdapter;
    private List<TasksManagerModel> downCompliteModelList = new ArrayList();
    private DownloadingDeleteDialog deleteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_manager_new);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("下载");
        rcyDownloadComplite.setLayoutManager(new LinearLayoutManager(mContext));
        rcyDownloadComplite.setNestedScrollingEnabled(false);
        downCompliteListAdapter = new DownCompliteListAdapter(downCompliteModelList, this);
        rcyDownloadComplite.setAdapter(downCompliteListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDownListData();
    }

    /**
     * 更新下载中下载完成列表数据
     */
    public void updateDownListData() {
        if (rcyDownloadComplite.isComputingLayout()) {
            L.e(TAG + " 更新下载中下载完成列表数据, rcy为准备好，返回");
            return;
        }
        L.e(TAG + " 更新下载中下载完成列表数据");
        downCompliteModelList.clear();
        List<TasksManagerModel> allTasks = TasksManager.getImpl().getAllTasks();
        for (TasksManagerModel tasksManagerModel : allTasks) {
            int status = TasksManager.getImpl().getStatus(tasksManagerModel.getGameId());
            if (status == ApkDownloadStatus.INSTALL) {
                downCompliteModelList.add(tasksManagerModel);
            }
        }
        downCompliteListAdapter.notifyDataSetChanged();
        //无数据隐藏编辑按钮
        if(downCompliteListAdapter.getItemCount() == 1){
            enableEdit(false);
        }else{
            enableEdit(true);
        }
    }

    /**
     * 接收安装成功事件
     *
     * @param installSuccessEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownInstallSuccessEvent(DownInstallSuccessEvent installSuccessEvent) {
        for (TasksManagerModel tasksManagerModel : downCompliteModelList) {
            if (installSuccessEvent.tasksManagerModel != null && installSuccessEvent.tasksManagerModel.getGameId() != null
                    && installSuccessEvent.tasksManagerModel.getGameId().equals(tasksManagerModel.getGameId())) {
                downCompliteModelList.remove(tasksManagerModel);
            }
        }
        downCompliteListAdapter.notifyDataSetChanged();
        //无数据隐藏编辑按钮
        if(downCompliteListAdapter.getItemCount() == 1){
            enableEdit(false);
        }else{
            enableEdit(true);
        }
    }

    @OnClick({R.id.iv_titleLeft, R.id.downloading_games, R.id.iv_edit, R.id.tv_edit,
                R.id.tv_select_all, R.id.tv_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.iv_edit:
            case R.id.tv_edit:
                if(downCompliteListAdapter.getItemCount()>1) {//有已下载完成记录
                    togleEdit();
                }
                break;
            case R.id.downloading_games:
                if (downloadingGames.getDownloadingSize() > 0) {
                    DownloadingManagerActivity.start(mContext);
                }
                break;
            case R.id.tv_select_all:
                togleSelectAll();
                break;
            case R.id.tv_delete:
                if(downCompliteListAdapter.getSelectedSize() == 0){
                    T.s(mContext, "请选择要删除的游戏");
                }else {
                    deleteDialog = new DownloadingDeleteDialog();
                    deleteDialog.showDialog(this, this);
                }
                break;
        }
    }

    /**
     * 编辑菜单的显示与隐藏
     */
    private void togleEdit() {
        downCompliteListAdapter.togleShowEdit();
        if (downCompliteListAdapter.isShowEdit()) {
            ivEdit.setVisibility(View.GONE);
            tvEdit.setVisibility(View.VISIBLE);
            llEditMenu.setVisibility(View.VISIBLE);
        } else {
            ivEdit.setVisibility(View.VISIBLE);
            tvEdit.setVisibility(View.GONE);
            llEditMenu.setVisibility(View.GONE);
        }
    }

    /**
     * 编辑按钮（有无下载完成任务时）
     */
    private void enableEdit(boolean enable){
        downCompliteListAdapter.hideEdit();
        if(!enable) {//无下载任务不显示
            ivEdit.setVisibility(View.GONE);
            tvEdit.setVisibility(View.GONE);
            llEditMenu.setVisibility(View.GONE);
        }else{
            ivEdit.setVisibility(View.VISIBLE);
            tvEdit.setVisibility(View.GONE);
            llEditMenu.setVisibility(View.GONE);
        }
    }

    /**
     * 全选反选的切换
     */
    private void togleSelectAll() {
        downCompliteListAdapter.togleSelectAll();
    }

    /**
     * 删除选中任务
     */
    private void delete() {
        downCompliteListAdapter.deleteSelected();
        togleEdit();
        if (downCompliteListAdapter.getItemCount() == 1) {
            enableEdit(false);
        } else {
            enableEdit(true);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        downCompliteListAdapter.release();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, DownloadManagerActivityNew.class);
        context.startActivity(starter);
    }

    @Override
    public void onSelect(boolean isSelectAll) {
        if(isSelectAll){
            tvSelectAll.setText("反选");
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.yes);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
            tvSelectAll.setCompoundDrawables(drawable,null,null,null);
        }else{
            tvSelectAll.setText("全选");
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.no);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
            tvSelectAll.setCompoundDrawables(drawable,null,null,null);
        }
    }

    @Override
    public void ok() {
        delete();
    }

    @Override
    public void cancel() {
        if(deleteDialog!=null){
            deleteDialog.dismiss();
        }
    }
}
