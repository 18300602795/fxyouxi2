package com.etsdk.app.huov7.down;

import android.text.TextUtils;

import com.etsdk.app.huov7.base.AileApplication;
import com.etsdk.app.huov7.model.InstallApkRecord;
import com.etsdk.app.huov7.util.AileConstants;
import com.game.sdk.util.ChannelNewUtil;
import com.liang530.application.BaseApplication;
import com.liang530.log.L;
import com.liang530.log.SP;
import com.liang530.log.T;
import com.liang530.utils.BaseAppUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadList;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;

/**
 * Created by LiuHongLiang on 2017/1/12.
 */

public class DownloadHelper {

    private static final String TAG = DownloadHelper.class.getSimpleName();

    public static void onClick(String gameId, ApklDownloadListener apklDownloadListener){
        if(gameId==null) return;
        //使用设置中的开关全局控制是否非wifi下载
//        int onlyWifi = tasksManagerModel.getOnlyWifi();//1
        int onlyWifi= SP.getBoolean(AileConstants.SP_4G_DOWN,false)?0:1;
        //判断是否新下载
        TasksManagerModel tasksManagerModel = TasksManager.getImpl().getTaskModelByGameId(gameId);
        boolean wifiConnected = BaseAppUtil.isWifiConnected(BaseApplication.getInstance());
        if(tasksManagerModel==null){
            if(!BaseAppUtil.isOnline(BaseApplication.getInstance())){
                T.s(BaseApplication.getInstance(),"网络不通，请稍后再试！");
            }else{
                apklDownloadListener.prepareDown(tasksManagerModel,!wifiConnected&&onlyWifi==1);
            }
            return;
        }
        int status = TasksManager.getImpl().getStatus(tasksManagerModel.getGameId());

        switch (status) {
            case ApkDownloadStatus.DOWNLOADING://下载中
                pause(tasksManagerModel.getId());
                break;
            case ApkDownloadStatus.PAUSE://暂停

                apklDownloadListener.prepareDown(tasksManagerModel,!wifiConnected&&onlyWifi==1);
                break;
            case ApkDownloadStatus.INITIAL://初始状态，被外部删除了apk
                apklDownloadListener.prepareDown(tasksManagerModel,!wifiConnected&&onlyWifi==1);
                break;
            case ApkDownloadStatus.INSTALL://安装
                installOrOpen(tasksManagerModel);
                break;
            case ApkDownloadStatus.OPEN://打开
                installOrOpen(tasksManagerModel);
                break;
            case ApkDownloadStatus.RETRY://重试
                apklDownloadListener.prepareDown(tasksManagerModel,!wifiConnected&&onlyWifi==1);
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
        if(BaseAppUtil.isInstallApp(BaseApplication.getInstance(),tasksManagerModel.getPackageName())
                &&tasksManagerModel.getInstalled()==1){
            BaseAppUtil.openAppByPackageName(BaseApplication.getInstance(),tasksManagerModel.getPackageName());
        }else{
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

    public static synchronized void start(TasksManagerModel tasksManagerModel){
        try {
            if(tasksManagerModel.getId()!=0){//已经下载过了
                createDownTask(tasksManagerModel).start();
            }else{//没有下载过，或者已经被删除了
                //需要重新插入下载数据到数据库
                tasksManagerModel= TasksManager.getImpl().addTask(tasksManagerModel.getGameId(), tasksManagerModel.getGameName(), tasksManagerModel.getGameIcon(), tasksManagerModel.getUrl(),tasksManagerModel.getOnlyWifi(), tasksManagerModel.getGameType());
                createDownTask(tasksManagerModel).start();
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
    private static BaseDownloadTask createDownTask(TasksManagerModel baseGameDownModel){

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
}
