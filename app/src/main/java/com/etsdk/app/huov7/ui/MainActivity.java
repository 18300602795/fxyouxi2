package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.Toast;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.MainVpAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.down.TasksManager;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.ListRequestBean;
import com.etsdk.app.huov7.model.MessageRequestBean;
import com.etsdk.app.huov7.model.ShowMsg;
import com.etsdk.app.huov7.model.StartupResultBean;
import com.etsdk.app.huov7.model.SwitchFragmentEvent;
import com.etsdk.app.huov7.model.TabEntity;
import com.etsdk.app.huov7.ui.fragment.MainGameFragment;
import com.etsdk.app.huov7.ui.fragment.MainMineFragmentNew2;
import com.etsdk.app.huov7.update.UpdateVersionDialog;
import com.etsdk.app.huov7.update.UpdateVersionService;
import com.etsdk.app.huov7.view.widget.AileTabView;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.game.sdk.http.HttpNoLoginCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.L;
import com.game.sdk.util.GsonUtil;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.control.LoginControl;
import com.liang530.log.T;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends ImmerseActivity {

    @BindView(R.id.main_vp)
    ViewPager mainVp;
    @BindView(R.id.main_tab)
    AileTabView mainTab;
    private MainVpAdapter mainVpAdapter;
    ArrayList<CustomTabEntity> tabEntityList = new ArrayList<CustomTabEntity>();
    private int position;
    private boolean isRed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        TasksManager.getImpl().init();
        setupUI();
        getPageData();
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
//        String url = "http://down.idielian.com/sdkgame//appand_100/205/appand_100.apk";
//        String agent = "app_100_1013";
//        //先去掉最后一个斜杠后面的字符
//        int lastOne = url.lastIndexOf("/");
//        url = url.substring(0, lastOne);
//        //去掉倒数第二斜杠后面的字符
//        url = url + "/" + agent + ".apk";
//        L.i("333", "url：" + url);
//        mainVp.toggleLock();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            position = intent.getIntExtra("position", 0);
            switchFragment(position);
        }
    }

    /**
     * 在fragment之前收到切换通知
     *
     * @param switchFragmentEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN, priority = 2)
    public void onSwitchFragmentEvent(SwitchFragmentEvent switchFragmentEvent) {
        if (this.getClass().getName().equals(switchFragmentEvent.activityClassName)) {
            switchFragment(switchFragmentEvent.positions[0]);
            if (switchFragmentEvent.positions.length == 1) {//没有后续切换，中断
                EventBus.getDefault().removeStickyEvent(switchFragmentEvent);
            }
        }
    }

    /**
     * 切换到游戏fragment
     *
     * @param position 要切换到游戏fragment中的Tab位置
     */
    public void switchGameFragment(final int position) {
        mainTab.setCurrentTab(1);
        mainVp.setCurrentItem(1, false);
        Fragment item = mainVpAdapter.getItem(1);
        if (item instanceof MainGameFragment) {
            ((MainGameFragment) item).switchFragment(position);
        }
    }

    /**
     * 切换到开服开测，并切换到开测tab
     *
     * @param
     */
    public void switchGameTestFragment() {
        mainTab.setCurrentTab(1);
        mainVp.setCurrentItem(1, false);
        Fragment item = mainVpAdapter.getItem(1);
        if (item instanceof MainGameFragment) {
            ((MainGameFragment) item).switchStartTestFragment();
        }
    }

    /**
     * 主界面切换到position位置的fragment
     *
     * @param position
     */
    public void switchFragment(int position) {
//        findTabImgView(position).setImageResource(tab_bg_select[position]);
        mainTab.setCurrentTab(position);
        mainVp.setCurrentItem(position, false);
    }

    private void setupUI() {
        if (getIntent() != null) {
            position = getIntent().getIntExtra("position", 0);
        }
        mainVpAdapter = new MainVpAdapter(getSupportFragmentManager());
        mainVp.setOffscreenPageLimit(3);

        mainTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 2) {
                    if (!LoginControl.isLogin()) {
                        LoginActivity.start(mContext);
                        mainTab.setCurrentTab(mainVp.getCurrentItem());
                    }
                }
                mainVp.setCurrentItem(position, false);
            }

            @Override
            public void onTabReselect(int position) {
                mainVpAdapter.reloadFragment(position);
            }
        });
        mainVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mainTab.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        tabEntityList.add(new TabEntity("首页", R.mipmap.tab_icon_tj_s, R.mipmap.tab_icon_tj_us));
        tabEntityList.add(new TabEntity("游戏", R.mipmap.tab_icon_game_s, R.mipmap.tab_icon_game_us));
        tabEntityList.add(new TabEntity("交易", R.mipmap.huo_jiaoyi2, R.mipmap.huo_jiaoyi));
        tabEntityList.add(new TabEntity("资讯", R.mipmap.zixun_s, R.mipmap.zixun_us));
        tabEntityList.add(new TabEntity("我的", R.mipmap.tab_icon_my_s, R.mipmap.tab_icon_my_us));
        mainTab.setTabSpaceEqual(true);
        mainTab.setTabData(tabEntityList);
        mainVp.setAdapter(mainVpAdapter);
        switchFragment(position);
        handleUpdate();
    }

    /**
     * 处理版本更新信息
     */
    private void handleUpdate() {
        final boolean showCancel;
        final StartupResultBean.UpdateInfo updateInfo = EventBus.getDefault().getStickyEvent(StartupResultBean.UpdateInfo.class);
        if (updateInfo != null) {//有更新
            if ("1".equals(updateInfo.getUp_status())) {//强制更新
                showCancel = false;
            } else if ("2".equals(updateInfo.getUp_status())) {//选择更新
                showCancel = true;
            } else {
                return;
            }
            if (TextUtils.isEmpty(updateInfo.getUrl()) ||
                    (!updateInfo.getUrl().startsWith("http") && !updateInfo.getUrl().startsWith("https"))) {
                return;//url不可用
            }
            new UpdateVersionDialog().showDialog(mContext, showCancel, updateInfo.getContent(), new UpdateVersionDialog.ConfirmDialogListener() {
                @Override
                public void ok() {
                    Intent intent = new Intent(mContext, UpdateVersionService.class);
                    intent.putExtra("url", updateInfo.getUrl());
                    mContext.startService(intent);
                    T.s(mContext, "开始下载,请在下载完成后确认安装！");
                    if (!showCancel) {//是强更则关闭界面
                        finish();
                    }
                }

                @Override
                public void cancel() {
                }
            });
        }
    }

    public static void start(Context context, int position) {
        Intent starter = new Intent(context, MainActivity.class);
        starter.putExtra("position", position);
        context.startActivity(starter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //更新我的页面的用户信息
        int currentItem = mainVp.getCurrentItem();
        if (currentItem == 4) {
            ((MainMineFragmentNew2) mainVpAdapter.getItem(currentItem)).updateData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // 退出时间
    private long currentBackPressedTime = 0;
    // 退出间隔
    private static final int BACK_PRESSED_INTERVAL = 2000;

    //重写onBackPressed()方法,继承自退出的方法
    @Override
    public void onBackPressed() {
        // 判断时间间隔
        if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
            currentBackPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
        } else {
            // 退出
            finish();
        }
    }

    public void getPageData() {
        final ListRequestBean baseRequestBean = new ListRequestBean();
        baseRequestBean.setPage(1 + "");
        baseRequestBean.setOffset("20");
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpNoLoginCallbackDecode httpCallbackDecode = new HttpNoLoginCallbackDecode<MessageRequestBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(MessageRequestBean data) {
                int num = 0;
                if (data != null && data.getList() != null) {
                    for (int i = 0; i < data.getList().size(); i++) {
                        if ("1".equals(data.getList().get(i).getReaded())) {
                            num++;
                        } else if (i == data.getList().size() - 1) {
                        }
                    }
                }
                if (num == 0) {
                    EventBus.getDefault().postSticky(new ShowMsg(false, false));
                } else {
                    EventBus.getDefault().postSticky(new ShowMsg(true, false, String.valueOf(num)));
                }
            }

            @Override
            public void onFailure(String code, String msg) {
//                super.onFailure(code, msg);
                EventBus.getDefault().postSticky(new ShowMsg(false, false));
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userMsgListApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgShow(ShowMsg showMsg) {
        if (showMsg.isUp()) {
            L.i("333", "更新信息");
            getPageData();
        }
        if (showMsg.isShow()) {
            mainTab.updateTip(4, true, showMsg.getNum());
        } else {
            mainTab.updateTip(4, false, "0");
        }
    }

}
