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
import com.etsdk.app.huov7.adapter.DownloadingListAdapterNew;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.down.TasksManager;
import com.etsdk.app.huov7.down.TasksManagerModel;
import com.etsdk.app.huov7.model.DownInstallSuccessEvent;
import com.etsdk.app.huov7.ui.dialog.DownloadingDeleteDialog;
import com.liang530.log.T;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 正在下载的游戏
 */
public class DownloadingManagerActivity extends ImmerseActivity implements DownloadingListAdapterNew.SelectListener, DownloadingDeleteDialog.ConfirmDialogListener {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.iv_edit)
    TextView ivEdit;
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.rcy_downloading)
    RecyclerView rcyDownloading;
    @BindView(R.id.tv_select_all)
    TextView tvSelectAll;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.ll_edit_menu)
    LinearLayout llEditMenu;
    private DownloadingListAdapterNew downloadingListAdapter;
    private List<TasksManagerModel> downloadingModelList = new ArrayList();
    private DownloadingDeleteDialog deleteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloading_manager);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("正在下载");
        rcyDownloading.setLayoutManager(new LinearLayoutManager(mContext));
        rcyDownloading.setNestedScrollingEnabled(false);
        downloadingListAdapter = new DownloadingListAdapterNew(downloadingModelList, this);
        rcyDownloading.setAdapter(downloadingListAdapter);
        updateDownListData();
    }

    /**
     * 更新下载中列表数据
     */
    public void updateDownListData() {
        if (rcyDownloading.isComputingLayout()) {
            return;
        }
        downloadingModelList.clear();
        downloadingModelList.addAll(TasksManager.getImpl().getAllDownloadingTasks());
        downloadingListAdapter.notifyDataSetChanged();
        if(downloadingListAdapter.getItemCount() == 1) {//如果没有下载，则不显示编辑按钮
            ivEdit.setVisibility(View.GONE);
            tvEdit.setVisibility(View.GONE);
            llEditMenu.setVisibility(View.GONE);
        }
    }

    /**
     * 接收安装成功事件
     *
     * @param installSuccessEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownInstallSuccessEvent(DownInstallSuccessEvent installSuccessEvent) {
        for (TasksManagerModel tasksManagerModel : downloadingModelList) {
            if (installSuccessEvent.tasksManagerModel != null && installSuccessEvent.tasksManagerModel.getGameId() != null
                    && installSuccessEvent.tasksManagerModel.getGameId().equals(tasksManagerModel.getGameId())) {
                downloadingModelList.remove(tasksManagerModel);
            }
        }
        downloadingListAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.iv_titleLeft, R.id.iv_edit, R.id.tv_edit, R.id.tv_select_all, R.id.tv_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.iv_edit:
            case R.id.tv_edit:
                togleEdit();
                break;
            case R.id.tv_select_all:
                togleSelectAll();
                break;
            case R.id.tv_delete:
                if(downloadingListAdapter.getSelectedSize() == 0){
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
        downloadingListAdapter.togleShowEdit();
        if (downloadingListAdapter.isShowEdit()) {
            ivEdit.setVisibility(View.GONE);
            tvEdit.setVisibility(View.VISIBLE);
            llEditMenu.setVisibility(View.VISIBLE);
        } else {
            if(downloadingListAdapter.getItemCount() == 1) {//如果没有下载，则不显示编辑按钮
                ivEdit.setVisibility(View.GONE);
            }else{
                ivEdit.setVisibility(View.VISIBLE);
            }
            tvEdit.setVisibility(View.GONE);
            llEditMenu.setVisibility(View.GONE);
        }
    }

    /**
     * 全选反选的切换
     */
    private void togleSelectAll() {
        downloadingListAdapter.togleSelectAll();
    }

    /**
     * 删除选中任务
     */
    private void delete() {
        downloadingListAdapter.deleteSelected();
        togleEdit();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        downloadingListAdapter.release();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, DownloadingManagerActivity.class);
        context.startActivity(starter);
    }
}
