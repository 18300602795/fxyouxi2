package com.etsdk.app.huov7.down;

import com.etsdk.app.huov7.base.AileApplication;
import com.etsdk.app.huov7.model.InstallApkRecord;
import com.game.sdk.util.ChannelNewUtil;
import com.liang530.application.BaseApplication;
import com.liang530.log.L;
import com.liang530.log.T;
import com.liang530.utils.BaseAppUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;

import java.io.File;
import java.util.Set;

/**
 * Created by liu hong liang on 2017/1/10.
 * 全局的下载监听器
 */

public class GlobalDownloadListener extends FileDownloadListener {
    private String gameId;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
    public GlobalDownloadListener(String gameId){
        this.gameId=gameId;
    }

    public void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        Set<ApklDownloadListener> fileDownloadListenerList = TasksManager.getImpl().getDownListenerMap().get(gameId);
        if(fileDownloadListenerList==null) return ;
        TasksManagerModel taskModelByGameId = TasksManager.getImpl().getTaskModelByGameId(gameId);
        if(taskModelByGameId == null){
            return;//若被删除会出现这种情况
        }
        for(ApklDownloadListener fileDownloadListener:fileDownloadListenerList){//通知所有注册的监听
            if(fileDownloadListener!=null){
                fileDownloadListener.pending(taskModelByGameId,soFarBytes,totalBytes);
            }
        }
    }

    public void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        Set<ApklDownloadListener> fileDownloadListenerList = TasksManager.getImpl().getDownListenerMap().get(gameId);
        if(fileDownloadListenerList==null) return ;
        TasksManagerModel taskModelByGameId = TasksManager.getImpl().getTaskModelByGameId(gameId);
        if(taskModelByGameId == null){
            return;//若被删除会出现这种情况
        }
        taskModelByGameId.setSpeed(task.getSpeed());
        for(ApklDownloadListener fileDownloadListener:fileDownloadListenerList){//通知所有注册的监听
            if(fileDownloadListener!=null){
                fileDownloadListener.progress(taskModelByGameId,soFarBytes,totalBytes);
            }
        }
    }

    @Override
    protected void completed(BaseDownloadTask task) {
        Set<ApklDownloadListener> fileDownloadListenerList = TasksManager.getImpl().getDownListenerMap().get(gameId);
        if(fileDownloadListenerList==null) return ;
        TasksManagerModel taskModelByGameId = TasksManager.getImpl().getTaskModelByGameId(gameId);
        if(taskModelByGameId == null){
            return;//若被删除会出现这种情况
        }
        for(ApklDownloadListener fileDownloadListener:fileDownloadListenerList){//通知所有注册的监听
            if(fileDownloadListener!=null){
                fileDownloadListener.completed(taskModelByGameId);
            }
        }
        //自动调起安装
        //记录是从app盒子安装的
        AileApplication instance = (AileApplication) AileApplication.getInstance();
        String packageNameByApkFile = BaseAppUtil.getPackageNameByApkFile(instance, task.getPath());
        int versionCodeFromApkFile = ChannelNewUtil.getVersionCodeFromApkFile(instance, task.getPath());
        InstallApkRecord installApkRecord=new InstallApkRecord(versionCodeFromApkFile,System.currentTimeMillis());
        instance.getInstallingApkList().put(packageNameByApkFile,installApkRecord);
        L.e("hongliang","记录的版本号为："+versionCodeFromApkFile);
        try {
            BaseAppUtil.installApk(BaseApplication.getInstance(),new File(task.getPath()));
        } catch (Exception e) {
            e.printStackTrace();
            T.s(BaseApplication.getInstance(), "安装失败");
        }
    }
    public void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        Set<ApklDownloadListener> fileDownloadListenerList = TasksManager.getImpl().getDownListenerMap().get(gameId);
        if(fileDownloadListenerList==null) return ;
        TasksManagerModel taskModelByGameId = TasksManager.getImpl().getTaskModelByGameId(gameId);
        if(taskModelByGameId!=null){
            for(ApklDownloadListener fileDownloadListener:fileDownloadListenerList){//通知所有注册的监听
                if(fileDownloadListener!=null){
                    fileDownloadListener.paused(taskModelByGameId,soFarBytes,totalBytes);
                }
            }
        }

    }

    public void error(BaseDownloadTask task, Throwable e) {
        Set<ApklDownloadListener> fileDownloadListenerList = TasksManager.getImpl().getDownListenerMap().get(gameId);
        if(fileDownloadListenerList==null) return ;
        TasksManagerModel taskModelByGameId = TasksManager.getImpl().getTaskModelByGameId(gameId);
        if(taskModelByGameId == null){
            return;//若被删除会出现这种情况
        }
        for(ApklDownloadListener fileDownloadListener:fileDownloadListenerList){//通知所有注册的监听
            if(fileDownloadListener!=null){
                fileDownloadListener.error(taskModelByGameId,e);
            }
        }
    }

    @Override
    protected void warn(BaseDownloadTask task) {

    }
}
