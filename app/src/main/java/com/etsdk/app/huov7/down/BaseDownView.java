package com.etsdk.app.huov7.down;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.liulishuo.filedownloader.BaseDownloadTask;

/**
 * Created by LiuHongLiang on 2017/1/12.
 *  基础的下载view
 */

public class BaseDownView extends FrameLayout implements ApklDownloadListener<BaseDownloadTask>{


    public BaseDownView(Context context) {
        super(context);
    }

    public BaseDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void pending(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {

    }

    @Override
    public void progress(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {

    }

    @Override
    public void completed(TasksManagerModel tasksManagerModel) {

    }

    @Override
    public void paused(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {

    }

    @Override
    public void error(TasksManagerModel tasksManagerModel, Throwable e) {

    }

    @Override
    public void prepareDown(TasksManagerModel tasksManagerModel, boolean noWifiHint) {
       DownloadHelper.start(tasksManagerModel);
    }

    @Override
    public void netOff() {

    }

    @Override
    public void delete() {

    }

    @Override
    public void netRecover() {

    }

    @Override
    public void installSuccess() {

    }
}
