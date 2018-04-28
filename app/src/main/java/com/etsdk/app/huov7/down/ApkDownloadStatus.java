package com.etsdk.app.huov7.down;

/**
 * Created by liu hong liang on 2017/1/10.
 * apk下载状态
 *
 */

public class ApkDownloadStatus {
    public static final int INITIAL=100;//初始状态
    public static final int WAIT=101;//等待
    public static final int DOWNLOADING=102;//下载中
    public static final int PAUSE=103;//暂停
    public static final int INSTALL=104;//等待安装,已完成
    public static final int OPEN=105;//已安装，打开
    public static final int RETRY=106;//重试

}
