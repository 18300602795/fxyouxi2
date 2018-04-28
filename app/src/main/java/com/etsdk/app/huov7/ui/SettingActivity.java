package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.down.ApkDownloadStatus;
import com.etsdk.app.huov7.down.TasksManager;
import com.etsdk.app.huov7.down.TasksManagerModel;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.util.AileConstants;
import com.liang530.log.SP;
import com.liang530.log.T;
import com.liang530.utils.BaseAppUtil;
import com.liang530.utils.BaseFileUtil;
import com.liang530.views.btn.SwitchButton;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends ImmerseActivity {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.activity_setting)
    LinearLayout activitySetting;
    @BindView(R.id.ll_aboutUs)
    LinearLayout llAboutUs;
    @BindView(R.id.rl_install_del)
    RelativeLayout rlInstallDel;
    @BindView(R.id.rl_accept_downMsg)
    RelativeLayout rlAcceptDownMsg;
    @BindView(R.id.tv_cacheSize)
    TextView tvCacheSize;
    @BindView(R.id.ll_clear_cache)
    LinearLayout llClearCache;
    @BindView(R.id.ll_account_manage)
    LinearLayout llAccountManage;
    @BindView(R.id.ll_platform_feedback)
    LinearLayout llPlatformFeedback;
    @BindView(R.id.ll_platform_agreement)
    LinearLayout llPlatformAgreement;
    @BindView(R.id.rl_open4gDown)
    RelativeLayout rlOpen4gDown;
    @BindView(R.id.sbtn_installDel)
    SwitchButton sbtnInstallDel;
    @BindView(R.id.sbtn_4gDown)
    SwitchButton sbtn4gDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("设置");
//        tvCacheSize.setText(BaseAppUtil.getCacheSize(mContext));
        tvCacheSize.setText(getCacheSize());
        //显示是否下载完删除
        boolean installDel = SP.getBoolean(AileConstants.SP_INSTALL_DEL, true);
        sbtnInstallDel.setChecked(installDel);

        installDel = SP.getBoolean(AileConstants.SP_4G_DOWN, false);//默认仅wifi下载
        sbtn4gDown.setChecked(installDel);
    }

    private String getCacheSize() {
        //获取缓存大小
        long directorySize = BaseFileUtil.getDirectorySize(this.getCacheDir());
        String defaultSaveRootPath = FileDownloadUtils.getDefaultSaveRootPath();
        directorySize += BaseFileUtil.getDirectorySize(new File(defaultSaveRootPath));
        return directorySize < 1.0D ? "没有缓存" : BaseFileUtil.formatFileSize(directorySize);
    }

    private void clearCache() {
        BaseAppUtil.clearCache(mContext);
        List<TasksManagerModel> allTasks = TasksManager.getImpl().getAllTasks();
        for (TasksManagerModel tasksManagerModel : allTasks) {
            int status = TasksManager.getImpl().getStatus(tasksManagerModel.getGameId());
            if (ApkDownloadStatus.INSTALL == status || ApkDownloadStatus.OPEN == status) {
//                TasksManager.getImpl().deleteTaskByModel(tasksManagerModel);
                File file=new File(tasksManagerModel.getPath());
                if(file.isFile()&&file.exists()){
                    file.delete();
                }
            }
        }
        if (allTasks.size() == 0) {//没有任何任务了
            BaseFileUtil.deleteFolder(FileDownloadUtils.getDefaultSaveRootPath());
        }
        T.s(mContext, "清理成功");
        tvCacheSize.setText(getCacheSize());
    }

    private void switchInstallDel() {
        boolean installDel = SP.getBoolean(AileConstants.SP_INSTALL_DEL, true);
        SP.putBoolean(AileConstants.SP_INSTALL_DEL, !installDel).commit();
        sbtnInstallDel.setChecked(!installDel);
    }
    private void switch4gDown(){
        boolean installDel = SP.getBoolean(AileConstants.SP_4G_DOWN, false);
        SP.putBoolean(AileConstants.SP_4G_DOWN, !installDel).commit();
        sbtn4gDown.setChecked(!installDel);
    }
    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight, R.id.rl_install_del, R.id.rl_accept_downMsg,
            R.id.ll_clear_cache, R.id.ll_account_manage, R.id.ll_platform_feedback, R.id.ll_platform_agreement,
            R.id.ll_aboutUs,R.id.rl_open4gDown})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
            case R.id.rl_install_del:
                switchInstallDel();
                break;
            case R.id.rl_open4gDown://切换非wifi下载
                switch4gDown();
                break;
            case R.id.rl_accept_downMsg:
                break;
            case R.id.ll_clear_cache:
                clearCache();
                break;
            case R.id.ll_account_manage:
                AccountManageActivity.start(mContext);
                break;
            case R.id.ll_platform_feedback:
                FeedBackActivity.start(mContext);
                break;
            case R.id.ll_platform_agreement:
                WebViewActivity.start(mContext, "平台协议", AppApi.getUrl(AppApi.agreementPlatformUrl));
                break;
            case R.id.ll_aboutUs:
                AboutUsActivity.start(mContext);
                break;
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, SettingActivity.class);
        context.startActivity(starter);
    }
}
