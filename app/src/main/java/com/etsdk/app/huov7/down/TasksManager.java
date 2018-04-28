package com.etsdk.app.huov7.down;

import android.content.Context;
import android.util.SparseArray;

import com.liang530.application.BaseApplication;
import com.liang530.log.L;
import com.liang530.utils.BaseAppUtil;
import com.liang530.utils.BaseFileUtil;
import com.liulishuo.filedownloader.FileDownloadConnectListener;
import com.liulishuo.filedownloader.FileDownloadMonitor;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TasksManager {
    private static final String TAG = TasksManager.class.getSimpleName();

    private final static class HolderClass {
        private final static TasksManager INSTANCE = new TasksManager();
    }

    private static SparseArray<String> statusMap = new SparseArray();
    private static SparseArray<String> descMap = new SparseArray();

    static {
        statusMap.put(ApkDownloadStatus.INITIAL, "下载");
        statusMap.put(ApkDownloadStatus.DOWNLOADING, "暂停");
        statusMap.put(ApkDownloadStatus.PAUSE, "继续");
        statusMap.put(ApkDownloadStatus.RETRY, "重试");
        statusMap.put(ApkDownloadStatus.INSTALL, "安装");
        statusMap.put(ApkDownloadStatus.OPEN, "启动");
        statusMap.put(ApkDownloadStatus.WAIT, "等待");

        descMap.put(ApkDownloadStatus.INITIAL, "初始化");
        descMap.put(ApkDownloadStatus.DOWNLOADING, "正在下载");
        descMap.put(ApkDownloadStatus.PAUSE, "已暂停");
        descMap.put(ApkDownloadStatus.RETRY, "正在重试");
        descMap.put(ApkDownloadStatus.INSTALL, "下载完成");
        descMap.put(ApkDownloadStatus.OPEN, "已安装");
        descMap.put(ApkDownloadStatus.WAIT, "等待");
    }

    private TasksManagerDBController dbController;
    private FileDownloadConnectListener listener;
    private Map<String, Set<ApklDownloadListener>> downListenerMap = new HashMap();

    public static TasksManager getImpl() {
        return HolderClass.INSTANCE;
    }

    private TasksManager() {
        dbController = new TasksManagerDBController();
    }

    /**
     * 初始化下载服务，不可以在application中调用，可在启动的第一个activity调用
     */
    public void init() {
        if (!FileDownloader.getImpl().isServiceConnected()) {
            FileDownloader.getImpl().bindService();
            if (listener != null) {
                FileDownloader.getImpl().removeServiceConnectListener(listener);
            }
            listener = new FileDownloadConnectListener() {
                @Override
                public void connected() {
                    L.d(TAG, "下载服务已经连接成功！");
                }

                @Override
                public void disconnected() {
                    L.d(TAG, "下载服务解除连接！");
                }
            };
            FileDownloader.getImpl().addServiceConnectListener(listener);
        }

        FileDownloadUtils.setDefaultSaveRootPath(BaseFileUtil.getApplicationPath(FileDownloadHelper.getAppContext(), "1tsdkDownload"));
        //注册全局的下载监听
        if (FileDownloadMonitor.getMonitor() == null) {
            FileDownloadMonitor.setGlobalMonitor(GlobalMonitor.getImpl());
        }
    }

    /**
     * 根据文件下载地址返回一个下载id
     *
     * @param url
     * @return
     */
    public int getDownloadId(String url) {
        return FileDownloadUtils.generateId(url, FileDownloadUtils.getDefaultSaveFilePath(url));
    }

    /**
     * 解除服务监听
     */
    private void unregisterServiceConnectionListener() {
        FileDownloader.getImpl().removeServiceConnectListener(listener);
        listener = null;
    }

    /**
     * 下载服务是否已经连接上，只有连接上了，才能获取下载过的文件状态和下载信息
     *
     * @return
     */
    public boolean isReady() {
        return FileDownloader.getImpl().isServiceConnected();
    }

    /**
     * 判断是否下载完成
     *
     * @param status
     * @return
     */
    public boolean isDownloaded(int status) {
        return status == FileDownloadStatus.completed;
    }

    /**
     * 是否是下载成功，并且安装了
     *
     * @param context
     * @param gameId
     * @return
     */
    public boolean isDownOkAndInstalled(Context context, String gameId) {
        TasksManagerModel taskModelByGame = dbController.getTaskModelByGameId(gameId);
        int id = taskModelByGame.getId();
        byte status = FileDownloader.getImpl().getStatus(id, taskModelByGame.getPath());
        if (status == FileDownloadStatus.completed) {
            if (BaseAppUtil.isInstallApp(context, taskModelByGame.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public TasksManagerModel getTaskModelByKeyVal(String key, String value) {
        return dbController.getTaskModelByKeyVal(key, value);
    }

    public TasksManagerModel getTaskModelById(int id) {
        return dbController.getTaskModelByKeyVal(TasksManagerModel.ID, id + "");
    }

    public TasksManagerModel addTask(String gameId, String gameName, String gameIcon, String url, int onlyWifi, String gameType) {
        try {
            return dbController.addTask(gameId, gameName, gameIcon, url, onlyWifi, gameType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public TasksManagerModel getTaskModelByGameId(String gameId) {
        return dbController.getTaskModelByGameId(gameId);
    }

    public List<TasksManagerModel> getAllTasks() {
        return dbController.getAllTasks();
    }

    public List<TasksManagerModel> getAllDownloadingTasks() {
        List<TasksManagerModel> allTask = dbController.getAllTasks();
        List<TasksManagerModel> downloadingTask = new ArrayList<>();
        for (TasksManagerModel model : allTask) {
            int status = getStatus(model.getGameId());
            if (status != ApkDownloadStatus.INSTALL && status != ApkDownloadStatus.OPEN) {
                downloadingTask.add(model);
            }
        }
        return downloadingTask;
    }

    public TasksManagerModel getFirstDownloadingTasks() {
        List<TasksManagerModel> allTask = dbController.getAllTasks();
        for (TasksManagerModel model : allTask) {
            int status = getStatus(model.getGameId());
            if (status != ApkDownloadStatus.INSTALL && status != ApkDownloadStatus.OPEN) {
                return model;
            }
        }
        return null;
    }

    public boolean updateTask(TasksManagerModel tasksManagerModel) {
        try {
            return dbController.updateTask(tasksManagerModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteTaskByModel(TasksManagerModel tasksManagerModel) {
        FileDownloader.getImpl().clear(tasksManagerModel.getId(), tasksManagerModel.getPath());
        boolean result = dbController.deleteTaskById(tasksManagerModel.getId());
        Set<ApklDownloadListener> fileDownloadListenerList = downListenerMap.get(tasksManagerModel.getGameId());
        if (fileDownloadListenerList != null) {
            for (ApklDownloadListener apklDownloadListener : fileDownloadListenerList) {
                apklDownloadListener.delete();
            }
        }
        return result;
    }

    public boolean deleteDbTaskaById(int id) {
        String[] args = {String.valueOf(id)};
        return dbController.deleteTaskById(id);
    }

    public boolean deleteDbTaskByGameId(String gameId) {
        return dbController.deleteTaskByGameId(gameId);
    }

    /**
     * 获取下载状态对应操作的文字
     *
     * @param gameId 游戏id
     * @return 下载状态文字
     */
    public String getStatusText(String gameId) {
        int status = getStatus(gameId);
        return statusMap.get(status);
    }

    /**
     * 获取下载状态对应操作的文字
     *
     * @param gameId 游戏id
     * @return 下载状态文字
     */
    public String getDescText(String gameId) {
        int status = getStatus(gameId);
        return descMap.get(status);
    }

    /**
     * 获取下载状态
     *
     * @param gameId
     * @return
     */
    public int getStatus(String gameId) {
        TasksManagerModel taskModelByGameId = getTaskModelByGameId(gameId);
        if (taskModelByGameId == null) {//没有下载过
            return ApkDownloadStatus.INITIAL;
        }
        int apkStatus = 0;
        byte status = FileDownloader.getImpl().getStatus(taskModelByGameId.getId(), taskModelByGameId.getPath());
        switch (status) {
            case FileDownloadStatus.pending://等待
                apkStatus = ApkDownloadStatus.WAIT;
                break;
            case FileDownloadStatus.INVALID_STATUS://无效,被外部删除了apk
                apkStatus = ApkDownloadStatus.INITIAL;
                break;
            case FileDownloadStatus.started://下载中
                apkStatus = ApkDownloadStatus.DOWNLOADING;
                break;
            case FileDownloadStatus.connected://下载中
                apkStatus = ApkDownloadStatus.DOWNLOADING;
                break;
            case FileDownloadStatus.progress://下载中
                apkStatus = ApkDownloadStatus.DOWNLOADING;
                break;
            case FileDownloadStatus.paused://暂停
                apkStatus = ApkDownloadStatus.PAUSE;
                break;
            case FileDownloadStatus.completed://安装
                apkStatus = ApkDownloadStatus.INSTALL;
                break;
            case FileDownloadStatus.blockComplete://安装
                apkStatus = ApkDownloadStatus.INSTALL;
                break;
            case FileDownloadStatus.retry://重试
                apkStatus = ApkDownloadStatus.RETRY;
                break;
            case FileDownloadStatus.error://重试
                apkStatus = ApkDownloadStatus.RETRY;
                break;
            case FileDownloadStatus.warn://已经在下载或者在队列中了，丢失下载进度和状态记录，进行重新连接
                apkStatus = ApkDownloadStatus.DOWNLOADING;
                break;
            default:
                apkStatus = ApkDownloadStatus.INITIAL;
                break;
        }
        if (apkStatus == ApkDownloadStatus.INSTALL || apkStatus == ApkDownloadStatus.INITIAL) {//下载标志完成
            //安装过
            if (taskModelByGameId.getInstalled() == 1) {
                if (BaseAppUtil.isInstallApp(BaseApplication.getInstance(), taskModelByGameId.getPackageName())) {//系统中存在
                    return ApkDownloadStatus.OPEN;
                }//被用户卸载，即没有安装
            }
            File file = new File(taskModelByGameId.getPath());
            if (file.exists()) {//可以安装
                return ApkDownloadStatus.INSTALL;
            } else {
                return ApkDownloadStatus.INITIAL;//重新下载
            }
        }
        return apkStatus;
    }

    public long getTotal(final int id) {
        long total = FileDownloader.getImpl().getTotal(id);
        if (total <= 0) {
            TasksManagerModel taskModelById = TasksManager.getImpl().getTaskModelById(id);
            if (taskModelById != null && taskModelById.getGameSize() != null) {
                try {
                    return Long.parseLong(taskModelById.getGameSize());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } else if (taskModelById != null) {
                File file = new File(taskModelById.getPath());
                if (file.exists() && file.isFile()) {
                    return file.length();
                }
            }
        }
        return total;
    }

    public int getProgress(final int id) {
        long total = getTotal(id);
        long soFar = getSoFar(id);
        if (total <= 0) return 0;
        return (int) (soFar * 100. / total);
    }


    public long getSoFar(final int id) {
        return FileDownloader.getImpl().getSoFar(id);
    }


    /**
     * 移除下载监听，通过id，必须任务已经在下载队列中
     *
     * @param gameId               游戏id
     * @param fileDownloadListener 下载监听
     */
    public void removeDownloadListenerById(String gameId, ApklDownloadListener fileDownloadListener) {
        Set<ApklDownloadListener> fileDownloadListenerList = downListenerMap.get(gameId);
        if (fileDownloadListenerList == null) {
            fileDownloadListenerList = new HashSet();
            downListenerMap.put(gameId, fileDownloadListenerList);
        }
        fileDownloadListenerList.remove(fileDownloadListener);
    }

    /**
     * 添加下载监听，通过gameId
     *
     * @param gameId               游戏id
     * @param fileDownloadListener 下载监听
     */
    public void addDownloadListenerById(String gameId, ApklDownloadListener fileDownloadListener) {
        Set<ApklDownloadListener> fileDownloadListenerList = downListenerMap.get(gameId);
        if (fileDownloadListenerList == null) {
            fileDownloadListenerList = new HashSet();
            downListenerMap.put(gameId, fileDownloadListenerList);
        }
        fileDownloadListenerList.add(fileDownloadListener);
        L.e("gameId=" + gameId + " 添加了监听，当前监听个数：" + downListenerMap.size());
        //进行一次初始化回调
        int status = getStatus(gameId);
        TasksManagerModel taskModelByGameId = getTaskModelByGameId(gameId);
        switch (status) {
            case ApkDownloadStatus.DOWNLOADING:
                fileDownloadListener.progress(taskModelByGameId, 0, 0);
                break;
            case ApkDownloadStatus.WAIT:
                fileDownloadListener.pending(taskModelByGameId, 0, 0);
                break;
            case ApkDownloadStatus.INSTALL:
                fileDownloadListener.completed(taskModelByGameId);
                break;
            case ApkDownloadStatus.OPEN:
                fileDownloadListener.installSuccess();
                break;
            case ApkDownloadStatus.RETRY:
                fileDownloadListener.error(taskModelByGameId, null);
                break;
            case ApkDownloadStatus.PAUSE:
                fileDownloadListener.paused(taskModelByGameId, 0, 0);
                break;
        }

    }

    public Map<String, Set<ApklDownloadListener>> getDownListenerMap() {
        return downListenerMap;
    }


}