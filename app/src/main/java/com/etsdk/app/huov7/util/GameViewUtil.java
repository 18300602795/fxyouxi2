package com.etsdk.app.huov7.util;

import android.text.TextUtils;

import com.etsdk.app.huov7.base.AileApplication;
import com.etsdk.app.huov7.down.ApkDownloadStatus;
import com.etsdk.app.huov7.down.BaseGameDownModel;
import com.etsdk.app.huov7.down.GlobalDownloadListener;
import com.etsdk.app.huov7.down.GlobalMonitor;
import com.etsdk.app.huov7.down.TasksManager;
import com.etsdk.app.huov7.down.TasksManagerModel;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.InstallApkRecord;
import com.game.sdk.util.ChannelNewUtil;
import com.liang530.application.BaseApplication;
import com.liang530.log.L;
import com.liang530.log.T;
import com.liang530.utils.BaseAppUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadList;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by liu hong liang on 2016/9/29.
 */

public class GameViewUtil {

    private static final  String TAG =GameViewUtil.class.getSimpleName() ;
    private static void confirmDown(final IGameLayout iGameLayout){
//        if(!BaseAppUtil.isWifiConnected(BaseApplication.getInstance())){//弹出提示是否下载
//            DownHintDialog downHintDialog=new DownHintDialog();
//            if(iGameLayout instanceof View){
//                Context context = ((View) iGameLayout).getContext();
//                downHintDialog.showDialog(context, true, null, new DownHintDialog.ConfirmDialogListener() {
//                    @Override
//                    public void ok() {
//                        start(iGameLayout,downloadListener,0);
//                    }
//                    @Override
//                    public void cancel() {
//
//                    }
//                });
//            }
//        }else{
//            start(iGameLayout,downloadListener,1);
//        }
            start(iGameLayout,1);
    }
    /**
     * 下载按钮点击
     */
    public static synchronized void clickDownload(IGameLayout iGameLayout) {
        TasksManagerModel tasksManagerModel = TasksManager.getImpl().getTaskModelByGameId(iGameLayout.getBaseGameDownModel().getGameId());
        //判断当前状态：
        if (tasksManagerModel == null) {//没有下载过，去下载
//            if(SmartInstallUtil.requestAccessibilityService()){//先进行开启辅助功能提示
//                return;
//            }
//            start(iGameLayout,downloadListener);
            if(!BaseAppUtil.isOnline(BaseApplication.getInstance())){
                T.s(BaseApplication.getInstance(),"网络不通，请稍后再试！");
            }else{
                confirmDown(iGameLayout);
            }
            return;
        }
        int status = TasksManager.getImpl().getStatus(tasksManagerModel.getGameId());
        switch (status) {
            case ApkDownloadStatus.DOWNLOADING://下载中
                pause(tasksManagerModel.getId());
                break;
            case ApkDownloadStatus.PAUSE://暂停
                confirmDown(iGameLayout);
                break;
            case ApkDownloadStatus.INITIAL://初始状态
                confirmDown(iGameLayout);
                break;
            case ApkDownloadStatus.INSTALL://安装
                installOrOpen(tasksManagerModel);
                break;
            case ApkDownloadStatus.OPEN://打开
                installOrOpen(tasksManagerModel);
                break;
            case ApkDownloadStatus.RETRY://重试
                confirmDown(iGameLayout);
                break;
        }
    }
    private static void installOrOpen(TasksManagerModel tasksManagerModel){
        //判断包名是否已经存入，可能由于下载成功时意外没有收到回调，导致包名没有设置成功，需要重新设置
        if(TextUtils.isEmpty(tasksManagerModel.getPackageName())
                &&!TextUtils.isEmpty(tasksManagerModel.getPath())){
            File file=new File(tasksManagerModel.getPath());
            if(file.exists()){//文件存在
                String packageNameByApkFile = BaseAppUtil.getPackageNameByApkFile(BaseApplication.getInstance(), tasksManagerModel.getPath());
                tasksManagerModel.setPackageName(packageNameByApkFile);
                TasksManager.getImpl().updateTask(tasksManagerModel);
            }
        }
        //判断是启动还是安装
        if(BaseAppUtil.isInstallApp(BaseApplication.getInstance(),tasksManagerModel.getPackageName())&&tasksManagerModel.getInstalled()==1){
            BaseAppUtil.openAppByPackageName(BaseApplication.getInstance(),tasksManagerModel.getPackageName());
        }else{
            //自动调起安装
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
    private static void pause(int id) {
        FileDownloader.getImpl().pause(id);
        TasksManagerModel taskModelById = TasksManager.getImpl().getTaskModelById(id);
        if(taskModelById!=null){
            taskModelById.setUserPause(1);
            TasksManager.getImpl().updateTask(taskModelById);
        }
        taskModelById = TasksManager.getImpl().getTaskModelById(id);
        L.e("hongliang","taskModelById暂停："+taskModelById.getUserPause());
    }
    public static synchronized void start(IGameLayout iGameLayout,int onlyWifi){
        try {
            TasksManagerModel taskModelByGameId = TasksManager.getImpl().getTaskModelByGameId(iGameLayout.getBaseGameDownModel().getGameId());
            if(taskModelByGameId!=null){//已经下载过了
                iGameLayout.getBaseGameDownModel().setUrl(taskModelByGameId.getUrl());
                if(iGameLayout.getBaseGameDownModel().getUrl()==null){//url不空
                    getUrlAndDownload(iGameLayout.getBaseGameDownModel(),onlyWifi);
                }else{
                    createDownTask(iGameLayout.getBaseGameDownModel()).start();
                }
            }else{//没有下载过，或者已经被删除了
                if(iGameLayout.getBaseGameDownModel().getUrl()==null){//url不空
                    getUrlAndDownload(iGameLayout.getBaseGameDownModel(), onlyWifi);
                }else{//需要重新插入下载数据到数据库
                    BaseGameDownModel gameBean = iGameLayout.getBaseGameDownModel();
                    taskModelByGameId= TasksManager.getImpl().addTask(gameBean.getGameId(), gameBean.getGameName(), gameBean.getGameIcon(), gameBean.getUrl(),onlyWifi, gameBean.getGameType());
                    if(taskModelByGameId!=null){
                        createDownTask(iGameLayout.getBaseGameDownModel()).start();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 创建一个下载任务
     * @param baseGameDownModel
     * @return
     */
    private static BaseDownloadTask createDownTask(BaseGameDownModel baseGameDownModel){

        int downloadId = TasksManager.getImpl().getDownloadId(baseGameDownModel.getUrl());
        BaseDownloadTask.IRunningTask task = FileDownloadList.getImpl().get(downloadId);
        BaseDownloadTask baseDownloadTask=null;
        if(task!=null &&task instanceof BaseDownloadTask){//如果已经在队列中，取出来
            baseDownloadTask = (BaseDownloadTask) task;
            FileDownloadListener listener = baseDownloadTask.getListener();
            if(listener==null){
                GlobalDownloadListener globalDownloadListener = new GlobalDownloadListener(baseGameDownModel.getGameId());
                baseDownloadTask.setListener(globalDownloadListener);
            }
            L.d(TAG,"队列中已有");
        }else{
            baseDownloadTask= FileDownloader.getImpl().create(baseGameDownModel.getUrl());
            baseDownloadTask.setCallbackProgressTimes(100);
            baseDownloadTask.setMinIntervalUpdateSpeed(400);
            baseDownloadTask.setTag(GlobalMonitor.TAG_GAME_ID,baseGameDownModel.getGameId());
            //设置下载文件放置路径
            baseDownloadTask.setPath(FileDownloadUtils.getDefaultSaveFilePath(baseGameDownModel.getUrl()));
            if(TextUtils.isEmpty(baseGameDownModel.getDowncnt())){
                baseGameDownModel.setDowncnt("0");
            }
            baseDownloadTask.setTag(GlobalMonitor.TAG_GAME_DOWNLOAD_COUNT,baseGameDownModel.getDowncnt());
            GlobalDownloadListener globalDownloadListener = new GlobalDownloadListener(baseGameDownModel.getGameId());
            baseDownloadTask.setListener(globalDownloadListener);
        }
        baseGameDownModel.setId(baseDownloadTask.getId());//记录下载id
        return baseDownloadTask;
    }

    private static void getUrlAndDownload(BaseGameDownModel gameBean, final int onlyWifi) {
//        HttpParams httpParams = AppApi.getHttpParams(false);
//        DeviceBean deviceBean = DeviceUtil.getDeviceBean(BaseApplication.getInstance());

//        httpParams.put("verid", BaseAppUtil.getAppVersionCode());
//        httpParams.put("gameid", gameBean.getGameid());
//        httpParams.put("openudid", "");
//        httpParams.put("deviceid", deviceBean.getDevice_id());
//        httpParams.put("devicetype", "");
//        httpParams.put("idfa", "");
//        httpParams.put("idfv", "");
//        httpParams.put("mac", "");
//        httpParams.put("resolution", "1024*768");
//        httpParams.put("network", "WIFI");
//        httpParams.put("userua", deviceBean.getUserua());
//        NetRequest.request().setParams(httpParams).get(AppApi.GAME_DOWN,new HttpJsonCallBackDialog<DownLoadUrlBean>(){
//            @Override
//            public void onDataSuccess(DownLoadUrlBean data) {
//                if(data.getData()!=null){
//                    String url=data.getData().getUrl().trim();
//                    String downloadCount=data.getData().getDowncnt();
//                    if(gameBeanWk.get()!=null){
//                        GameBean gameBean = gameBeanWk.get();
//                        if(gameBean!=null){
//                            gameBean.setUrl(url);
//                            gameBean.setDowncnt(downloadCount);
//                            //存入数据库
//                            TasksManagerModel tasksManagerModel = TasksManager.getImpl().addTask(gameBean.getGameid(), gameBean.getGamename(), gameBean.getIcon(), gameBean.getUrl(),onlyWifi);
//                            if(tasksManagerModel!=null){
//                                createDownTask(gameBean,listenerWk.get()).start();
//                            }else{
//                                L.d(TAG,"error save fail");
//                            }
//                        }
//                    }
//                }
//            }
//        });
        gameBean.setUrl(AppApi.downApkUrl1);
        gameBean.setDowncnt("1");
        //存入数据库
        TasksManagerModel tasksManagerModel = TasksManager.getImpl().addTask(gameBean.getGameId(), gameBean.getGameName(), gameBean.getGameIcon(), gameBean.getUrl(),onlyWifi, gameBean.getGameType());
        if(tasksManagerModel!=null){
            createDownTask(gameBean).start();
        }else{
            L.d(TAG,"error save fail");
        }
    }

    //返回数组，下标1代表大小，下标2代表单位 KB/MB
    public static String getFormatSize(double size) {
        //Logger.msg(TAG, "size:"+size);
        String str = "";
        if (size >= 1024) {
            str = "KB";
            size /= 1024;
            if (size >= 1024) {
                str = "MB";
                size /= 1024;
            }
        }
        DecimalFormat formatter = new DecimalFormat("0.00");
        formatter.setGroupingSize(3);
        return (formatter.format(size) + str);
    }
}
