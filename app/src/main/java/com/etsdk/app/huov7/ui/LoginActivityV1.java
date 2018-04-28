package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etsdk.app.huov7.BuildConfig;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.LoginRequestBean;
import com.etsdk.app.huov7.model.LoginResultBean;
import com.etsdk.app.huov7.model.ShowMsg;
import com.etsdk.app.huov7.model.ThirdAuthRequestBean;
import com.etsdk.app.huov7.sharesdk.ThirdLoginUtil;
import com.etsdk.app.huov7.ui.dialog.SelectAccountLoginDialog;
import com.etsdk.app.huov7.ui.dialog.SelectAccountLoginPop;
import com.game.sdk.db.impl.UserLoginInfodao;
import com.game.sdk.domain.UserInfo;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.control.LoginControl;
import com.liang530.log.L;
import com.liang530.log.T;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created by liu hong liang on 2017/6/21.
 * 登陆界面v1版
 */
public class LoginActivityV1 extends ImmerseActivity implements PlatformActionListener, SelectAccountLoginPop.SelectAccountListener {

    @BindView(R.id.tv_titleLeft)
    TextView tvTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.huo_sdk_rl_title)
    RelativeLayout huoSdkRlTitle;
    @BindView(R.id.huo_sdk_iv_user)
    ImageView huoSdkIvUser;
    @BindView(R.id.huo_sdk_et_account)
    EditText huoSdkEtAccount;
    @BindView(R.id.huo_sdk_iv_pwd)
    ImageView huoSdkIvPwd;
    @BindView(R.id.huo_sdk_img_show_pwd)
    ImageView huoSdkImgShowPwd;
    @BindView(R.id.huo_sdk_et_pwd)
    EditText huoSdkEtPwd;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.tv_forgetPwd)
    TextView tvForgetPwd;
    @BindView(R.id.huo_sdk_cb_record)
    CheckBox huoSdkCbRecord;
    @BindView(R.id.ll_third_login)
    LinearLayout llThirdLogin;
    @BindView(R.id.iv_qq)
    ImageView ivQq;
    @BindView(R.id.iv_weixin)
    ImageView ivWeixin;
    @BindView(R.id.iv_weibo)
    ImageView ivWeibo;
    @BindView(R.id.huo_sdk_iv_selectAccount)
    ImageView huoSdkIvSelectAccount;
    SelectAccountLoginPop selectAccountLoginPop;
    @BindView(R.id.rl_login_account)
    RelativeLayout rlLoginAccount;
    private SelectAccountLoginDialog selectAccountLoginDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_v1);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("登录");
        tvTitleLeft.setText("");
        tvTitleRight.setText("注册 >");
        selectAccountLoginPop = new SelectAccountLoginPop(rlLoginAccount, this);
        UserInfo userInfoLast = UserLoginInfodao.getInstance(mActivity).getUserInfoLast();
        if (userInfoLast != null && !TextUtils.isEmpty(userInfoLast.username)) {
            huoSdkEtAccount.setText(userInfoLast.username);
            huoSdkEtPwd.setText(userInfoLast.password);
        }
        //第三方登录，根据是否有对应参数显示，默认隐藏
        if (ThirdLoginUtil.isQQEnable() || ThirdLoginUtil.isWeiXinEnable() || ThirdLoginUtil.isWeiBoEnable()) {
            llThirdLogin.setVisibility(View.VISIBLE);
        }
        if (ThirdLoginUtil.isQQEnable()) {
            ivQq.setVisibility(View.VISIBLE);
        }
        if (ThirdLoginUtil.isWeiXinEnable()) {
            ivWeixin.setVisibility(View.VISIBLE);
        }
        if (ThirdLoginUtil.isWeiBoEnable()) {
            ivWeibo.setVisibility(View.VISIBLE);
        }
        selectAccountLoginDialog = new SelectAccountLoginDialog(new SelectAccountLoginDialog.SelectAccountListener() {
            @Override
            public void onSelectAccount(String username) {
                String password = huoSdkEtPwd.getText().toString().trim();
                sumitLogin(username, password);
            }
        });
    }

    private List<LoginResultBean.UserName> getTestList() {
        List<LoginResultBean.UserName> userNameList = new ArrayList<>();
        userNameList.add(new LoginResultBean.UserName("xxxx1231"));
        userNameList.add(new LoginResultBean.UserName("bbbb123"));
        userNameList.add(new LoginResultBean.UserName("fdsaxxfs"));
        return userNameList;
    }

    private void sumitLogin(final String account, final String password) {
        if (TextUtils.isEmpty(account)) {
            T.s(mActivity, "用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            T.s(mActivity, "密码不能为空");
            return;
        }
        final LoginRequestBean loginRequestBean = new LoginRequestBean();
        loginRequestBean.setUsername(account);
        loginRequestBean.setPassword(password);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(loginRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<LoginResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(LoginResultBean data) {
                if (data != null) {
//                    data.setUserlist(getTestList());
                    if (data.getUserlist() != null && data.getUserlist().size() > 1) {
                        selectAccountLoginDialog.showAccountLoginDialog(LoginActivityV1.this, data.getUserlist());
                        //填对话框选择账号进行登陆
                        return;
                    }
                    LoginControl.saveToken(data.getUser_token());
                    T.s(mActivity, "登陆成功");
                    //接口回调通知
                    //保存账号到数据库
                    if (huoSdkCbRecord.isChecked()) {
                        //找到了先删除后保存
                        if (UserLoginInfodao.getInstance(mActivity).findUserLoginInfoByName(account)) {
                            UserLoginInfodao.getInstance(mActivity).deleteUserLoginByName(account);
                        }
                        UserLoginInfodao.getInstance(mActivity).saveUserLoginInfo(account, password);
                    } else {
                        //如果找到了，将密码
                        if (UserLoginInfodao.getInstance(mActivity).findUserLoginInfoByName(account)) {
                            UserLoginInfodao.getInstance(mActivity).deleteUserLoginByName(account);
                            UserLoginInfodao.getInstance(mActivity).saveUserLoginInfo(account, "");
                        }
                    }
                    finish();
                    EventBus.getDefault().post(true);
                    EventBus.getDefault().post(new ShowMsg(true));
//                    MainActivity.start(mActivity, 4);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        httpCallbackDecode.setLoadMsg("正在登录...");
        RxVolley.post(AppApi.getUrl(AppApi.loginApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    @OnClick({R.id.tv_titleLeft, R.id.tv_title_right, R.id.huo_sdk_iv_selectAccount, R.id.huo_sdk_img_show_pwd, R.id.btn_submit, R.id.tv_forgetPwd, R.id.iv_qq, R.id.iv_weixin, R.id.iv_weibo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_titleLeft:
                EventBus.getDefault().post(false);
                finish();
                break;
            case R.id.tv_title_right:
                PhoneRegisterActivityV1.start(mActivity);
                break;
            case R.id.huo_sdk_img_show_pwd:
                if (huoSdkImgShowPwd.isSelected()) {
                    huoSdkEtPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    huoSdkEtPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                huoSdkImgShowPwd.setSelected(!huoSdkImgShowPwd.isSelected());
                break;
            case R.id.btn_submit:
                String account = huoSdkEtAccount.getText().toString().trim();
                String password = huoSdkEtPwd.getText().toString().trim();
                sumitLogin(account, password);
                break;
            case R.id.tv_forgetPwd:
                ForgetPwdInputAccountActivity.start(mActivity);
                break;
            case R.id.iv_qq:
                ThirdLoginUtil.initQQ(BuildConfig.qq_appid, BuildConfig.qq_appkey);
                ThirdLoginUtil.loginByThird(ThirdLoginUtil.LOGIN_QQ, this);
                break;
            case R.id.iv_weibo:
                ThirdLoginUtil.initXinNan( BuildConfig.wb_appid, BuildConfig.wb_appkey, BuildConfig.wb_directurl);
                ThirdLoginUtil.loginByThird(ThirdLoginUtil.LOGIN_XLWB, this);
                break;
            case R.id.iv_weixin:
                ThirdLoginUtil.initWx(BuildConfig.wx_appid, BuildConfig.wx_appkey);
                ThirdLoginUtil.loginByThird(ThirdLoginUtil.LOGIN_WX, this);
                break;
            case R.id.huo_sdk_iv_selectAccount:
                huoSdkIvSelectAccount.setImageResource(R.mipmap.huosdk_up2);
                selectAccountLoginPop.show();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            EventBus.getDefault().post(false);
        }
        return super.onKeyDown(keyCode, event);
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

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivityV1.class);
        context.startActivity(starter);
    }

    public static Intent getIntent(Context context) {
        Intent starter = new Intent(context, LoginActivityV1.class);
        return starter;
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


    @Override
    public void selectedAccount(UserInfo userInfo) {
        huoSdkEtAccount.setText(userInfo.username);
        huoSdkEtPwd.setText(userInfo.password);
    }

    @Override
    public void deletedAccount(UserInfo userInfo) {
        if (huoSdkEtAccount.getText().toString().equals(userInfo.username)) {
            huoSdkEtAccount.setText("");
            huoSdkEtPwd.setText("");
        }
    }

    @Override
    public void dismissPop() {
        huoSdkIvSelectAccount.setImageResource(R.mipmap.huosdk_down2);
    }

}
