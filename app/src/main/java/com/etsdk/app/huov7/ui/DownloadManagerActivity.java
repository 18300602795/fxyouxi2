package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.DownUnInstallListAdapter;
import com.etsdk.app.huov7.adapter.DownloadingListAdapter;
import com.etsdk.app.huov7.base.AileApplication;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.down.ApkDownloadStatus;
import com.etsdk.app.huov7.down.TasksManager;
import com.etsdk.app.huov7.down.TasksManagerModel;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.DownInstallSuccessEvent;
import com.etsdk.app.huov7.model.DownTaskDeleteEvent;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.GameBeanList;
import com.etsdk.app.huov7.model.InstallApkRecord;
import com.etsdk.app.huov7.provider.GameItemViewProvider;
import com.game.sdk.util.ChannelNewUtil;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.application.BaseApplication;
import com.liang530.log.L;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.liang530.utils.BaseAppUtil;
import com.liulishuo.filedownloader.FileDownloader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class DownloadManagerActivity extends ImmerseActivity {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.tv_typeName)
    TextView tvTypeName;
    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.cur_hot_rcy_Game)
    RecyclerView curHotRcyGame;
    @BindView(R.id.activity_down_load_manager)
    LinearLayout activityDownLoadManager;
    @BindView(R.id.recy_downing)
    RecyclerView recyDowning;
    @BindView(R.id.recy_unInstall)
    RecyclerView recyUnInstall;
    @BindView(R.id.tv_uninstall_count)
    TextView tvUninstallCount;
    @BindView(R.id.tv_all_pause)
    TextView tvAllPause;
    @BindView(R.id.tv_all_install)
    TextView tvAllInstall;
    @BindView(R.id.ll_like_game)
    LinearLayout llLikeGame;
    private DownloadingListAdapter downloadingListAdapter;
    private DownUnInstallListAdapter downUnInstallListAdapter;
    private List<TasksManagerModel> downloadingModelList = new ArrayList();
    private List<TasksManagerModel> downUnInstallModelList = new ArrayList();
    private Items hotGameItems =new Items();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_manager);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("下载管理");
        recyDowning.setLayoutManager(new LinearLayoutManager(this));
        recyDowning.setNestedScrollingEnabled(false);
        downloadingListAdapter = new DownloadingListAdapter(downloadingModelList);
        recyDowning.setAdapter(downloadingListAdapter);
        recyUnInstall.setLayoutManager(new GridLayoutManager(this, 4));
        recyUnInstall.setNestedScrollingEnabled(false);
        downUnInstallListAdapter = new DownUnInstallListAdapter(downUnInstallModelList);
        recyUnInstall.setAdapter(downUnInstallListAdapter);
        tvUninstallCount.setText(downUnInstallModelList == null ? "" : downloadingModelList.size() + "");
        curHotRcyGame.setLayoutManager(new LinearLayoutManager(this));
        curHotRcyGame.setNestedScrollingEnabled(false);
        MultiTypeAdapter hotGameAdapter=new MultiTypeAdapter(hotGameItems);
        hotGameAdapter.register(GameBean.class,new GameItemViewProvider());
        curHotRcyGame.setAdapter(hotGameAdapter);
        updateDownListData();
        getHotGameListData();
    }

    /**
     * 更新下载中下载完成列表数据
     */
    public void updateDownListData() {
        downloadingModelList.clear();
        downUnInstallModelList.clear();
        List<TasksManagerModel> allTasks = TasksManager.getImpl().getAllTasks();
        for (TasksManagerModel tasksManagerModel : allTasks) {
            int status = TasksManager.getImpl().getStatus(tasksManagerModel.getGameId());
            if (status == ApkDownloadStatus.INSTALL) {
                downUnInstallModelList.add(tasksManagerModel);
            } else if (status != ApkDownloadStatus.OPEN && status != ApkDownloadStatus.INITIAL) {
                downloadingModelList.add(tasksManagerModel);
            }
        }
        downloadingListAdapter.notifyDataSetChanged();
        downUnInstallListAdapter.notifyDataSetChanged();
        tvUninstallCount.setText(downUnInstallModelList == null ? "" : downUnInstallModelList.size() + "");
    }

    /**
     * 接收删除事件
     *
     * @param deleteEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownTaskDeleteEvent(DownTaskDeleteEvent deleteEvent) {
        for(TasksManagerModel tasksManagerModel:downloadingModelList){
            if(deleteEvent.tasksManagerModel!=null&&deleteEvent.tasksManagerModel.getGameId()!=null
                    &&deleteEvent.tasksManagerModel.getGameId().equals(tasksManagerModel.getGameId())){
                downloadingModelList.remove(tasksManagerModel);
            }
        }
        downloadingListAdapter.notifyDataSetChanged();
    }

    /**
     * 接收安装成功事件
     *
     * @param installSuccessEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownInstallSuccessEvent(DownInstallSuccessEvent installSuccessEvent) {
        for(TasksManagerModel tasksManagerModel:downUnInstallModelList){
            if(installSuccessEvent.tasksManagerModel!=null&&installSuccessEvent.tasksManagerModel.getGameId()!=null
                    &&installSuccessEvent.tasksManagerModel.getGameId().equals(tasksManagerModel.getGameId())){
                downUnInstallModelList.remove(tasksManagerModel);
            }
        }
        downUnInstallListAdapter.notifyDataSetChanged();
        tvUninstallCount.setText(downUnInstallModelList == null ? "" : downUnInstallModelList.size() + "");
    }
    @OnClick({R.id.iv_titleLeft,R.id.tv_titleRight, R.id.tv_more, R.id.tv_all_pause, R.id.tv_all_install})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
            case R.id.tv_more:
                GameListActivity.start(mContext,"实时热门下载", false, false ,2,0,0,0,0,0,0,null);
                break;
            case R.id.tv_all_pause:
                FileDownloader.getImpl().pauseAll();
                break;
            case R.id.tv_all_install:
                installAll();
                break;
        }
    }
    private void installAll(){
        for(TasksManagerModel tasksManagerModel:downUnInstallModelList){
            //进行安装
            //记录是从app盒子安装的
            AileApplication instance = (AileApplication) AileApplication.getInstance();
            String packageNameByApkFile = BaseAppUtil.getPackageNameByApkFile(instance, tasksManagerModel.getPath());
            int versionCodeFromApkFile = ChannelNewUtil.getVersionCodeFromApkFile(instance, tasksManagerModel.getPath());
            InstallApkRecord installApkRecord=new InstallApkRecord(versionCodeFromApkFile,System.currentTimeMillis());
            instance.getInstallingApkList().put(packageNameByApkFile,installApkRecord);
            L.e("hongliang","记录的版本号为："+versionCodeFromApkFile);
            BaseAppUtil.installApk(BaseApplication.getInstance(),new File(tasksManagerModel.getPath()));
        }
    }
    private void getHotGameListData(){
        final HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.gameListApi);
        httpParams.put("hot","2");
        httpParams.put("page",1);
        httpParams.put("offset",5);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.gameListApi),new HttpJsonCallBackDialog<GameBeanList>(){
            @Override
            public void onDataSuccess(GameBeanList data) {
                if(data!=null&&data.getData()!=null&&data.getData().getList()!=null){
                    hotGameItems.clear();
                    hotGameItems.addAll(data.getData().getList());
                    curHotRcyGame.getAdapter().notifyDataSetChanged();
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, DownloadManagerActivityNew.class);
        context.startActivity(starter);
    }
}
