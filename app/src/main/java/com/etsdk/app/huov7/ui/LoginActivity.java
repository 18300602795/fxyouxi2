package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.LoginResultBean;
import com.etsdk.app.huov7.model.ThirdAuthRequestBean;
import com.etsdk.app.huov7.util.ViewStackManager;
import com.etsdk.app.huov7.view.LoginView;
import com.etsdk.app.huov7.view.RegisterView;
import com.etsdk.app.huov7.view.SelectAccountView;
import com.game.sdk.HuosdkManager;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.control.LoginControl;
import com.liang530.log.L;
import com.liang530.log.T;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

public class LoginActivity extends ImmerseActivity implements PlatformActionListener {

    @BindView(R.id.loginView)
    LoginView loginView;
    @BindView(R.id.registerView)
    RegisterView registerView;
    @BindView(R.id.selectAccountView)
    SelectAccountView selectAccountView;
    private ViewStackManager viewStackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setupUI();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setupUI();
    }

    private void setupUI() {
        //请求初始化，获取一个新的token
        HuosdkManager.getInstance().gotoStartup(3);
        viewStackManager = ViewStackManager.getInstance(this);
        viewStackManager.clear();
        viewStackManager.addBackupView(loginView);
        viewStackManager.addBackupView(registerView);
        viewStackManager.addBackupView(selectAccountView);
        showView(0);
        loginView.setThirdLoginListener(this);
    }

    private void showView(int type) {
        viewStackManager.showView(loginView);
    }

    @Override
    public void onBackPressed() {
        if (ViewStackManager.isLastView()) {
            super.onBackPressed();
            MainActivity.start(mContext, 0);
        } else {
            viewStackManager.removeTopView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewStackManager.clear();
    }


    public static void start(Context context) {
//        Intent starter = new Intent(context, LoginActivity.class);
//        context.startActivity(starter);
        LoginActivityV1.start(context);
    }

    /**
     * 第三方登录回调
     */
    @Override
    public void onComplete(final Platform plat, final int action, HashMap<String, Object> res) {
        L.e(TAG, "第三方登录 完成");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (action == Platform.ACTION_USER_INFOR) {
                    String platformNname = plat.getDb().getPlatformNname();
                    int userfrom = 2;
                    if ("QQ".equals(platformNname)) {
                        userfrom = 2;
                    } else if ("Wechat".equals(platformNname)) {
                        userfrom = 3;
                    } else if ("SinaWeibo".equals(platformNname)) {
                        userfrom = 4;
                    }
                    ThirdAuthRequestBean requestBean = new ThirdAuthRequestBean();
                    requestBean.setOpenid(plat.getDb().getUserId());
                    requestBean.setAccess_token(plat.getDb().getToken());
                    requestBean.setExpires_date(plat.getDb().getExpiresTime() / 1000);
                    requestBean.setUserfrom(userfrom);
                    requestBean.setPortrait("111");
                    requestBean.setNickname(plat.getDb().getUserName());
                    requestBean.setHead_img(plat.getDb().getUserIcon());
                    HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
                    HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<LoginResultBean>(mContext, httpParamsBuild.getAuthkey()) {
                        @Override
                        public void onDataSuccess(LoginResultBean data) {
                            if (data != null) {
                                LoginControl.saveToken(data.getUser_token());
                                T.s(mActivity, "登陆成功");
                                //接口回调通知
                                mActivity.finish();
                                MainActivity.start(mActivity, 4);
                            }
                        }
                    };
                    httpCallbackDecode.setShowTs(true);
                    httpCallbackDecode.setLoadingCancel(false);
                    httpCallbackDecode.setShowLoading(true);
                    httpCallbackDecode.setLoadMsg("正在登录...");
                    RxVolley.post(AppApi.getUrl(AppApi.loginThird), httpParamsBuild.getHttpParams(), httpCallbackDecode);
                } else {
                    Toast.makeText(mContext, "登陆失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 第三方登录回调
     *
     * @param platform
     * @param i
     * @param throwable
     */
    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        L.e(TAG, "第三方登录 错误 " + throwable.getMessage());
    }

    /**
     * 第三方登录回调
     *
     * @param platform
     * @param i
     */
    @Override
    public void onCancel(Platform platform, int i) {
        L.e(TAG, "第三方登录 取消");
    }
}
