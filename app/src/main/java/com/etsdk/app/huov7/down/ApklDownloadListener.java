package com.etsdk.app.huov7.down;

/**
 * Created by liu hong liang on 2017/1/10.
 * 全局的下载监听器
 * TasksManager设置是否移动网络下载
 */

public interface ApklDownloadListener<T>{
    
    public void pending(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes);

    public void progress(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes);


    public void completed(TasksManagerModel tasksManagerModel);

    public void paused(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes);

    public void error(TasksManagerModel tasksManagerModel, Throwable e);

    public void prepareDown(TasksManagerModel tasksManagerModel, boolean noWifiHint);

    public void netOff();

    public void delete();

    public void netRecover();
    public void installSuccess();
}
