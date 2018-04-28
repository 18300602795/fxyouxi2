package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.LoginResultBean;
import com.etsdk.app.huov7.model.RegisterMobileRequestBean;
import com.etsdk.app.huov7.model.SmsSendRequestBean;
import com.etsdk.app.huov7.model.SmsSendResultBean;
import com.etsdk.app.huov7.model.UserNameRegisterRequestBean;
import com.game.sdk.db.impl.UserLoginInfodao;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.T;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.control.LoginControl;
import com.liang530.utils.BaseTextUtil;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends ImmerseActivity {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_phonePassword)
    EditText etPhonePassword;
    @BindView(R.id.et_authCode)
    EditText etAuthCode;
    @BindView(R.id.tv_getAuthCode)
    TextView tvGetAuthCode;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_confirmPwd)
    EditText etConfirmPwd;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    @BindView(R.id.tv_switch_register)
    TextView tvSwitchRegister;
    @BindView(R.id.ll_phone_register)
    LinearLayout llPhoneRegister;
    @BindView(R.id.ll_account_register)
    LinearLayout llAccountRegister;
    @BindView(R.id.tv_register_title)
    TextView tvRegisterTitle;
    private boolean isPhoneRegister = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("注册");
        switchRegisterUI(isPhoneRegister);
    }

    private void switchRegisterUI(boolean isPhoneRegister) {
        this.isPhoneRegister = isPhoneRegister;
        if (isPhoneRegister) {
            llPhoneRegister.setVisibility(View.VISIBLE);
            llAccountRegister.setVisibility(View.GONE);
            tvSwitchRegister.setText("使用平台账号注册 >");
            tvRegisterTitle.setText("手机号注册");
        } else {
            llPhoneRegister.setVisibility(View.GONE);
            llAccountRegister.setVisibility(View.VISIBLE);
            tvSwitchRegister.setText("使用手机号注册 >");
            tvRegisterTitle.setText("账号注册");
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, RegisterActivity.class);
        context.startActivity(starter);
    }

    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight, R.id.tv_getAuthCode, R.id.btn_register, R.id.tv_agreement, R.id.tv_switch_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
            case R.id.tv_getAuthCode:
                sendSms();
                break;
            case R.id.btn_register:
                if(isPhoneRegister){
                    submitRegisterByPhone();
                }else{
                    submitRegisterByAccount();
                }
                break;
            case R.id.tv_agreement:
                WebViewActivity.start(mContext,"平台协议",AppApi.getUrl(AppApi.agreementPlatformUrl));
                break;
            case R.id.tv_switch_register:
                switchRegisterUI(!isPhoneRegister);
                break;
        }
    }

    private void submitRegisterByAccount() {
        final String account = etAccount.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String confirmPwd = etConfirmPwd.getText().toString().trim();
        Pattern p = Pattern.compile("([a-zA-Z0-9]{6,16})");
        if (!p.matcher(account).matches()) {
            T.s(mContext,"账号只能由6至12位英文或数字组成");
            return;
        }
        if (!p.matcher(password).matches()) {
            T.s(mContext,"密码只能由6至12位英文或数字组成");
            return;
        }
        if(!password.equals(confirmPwd)){
            T.s(mContext,"两次输入密码不一致");
            return;
        }
        UserNameRegisterRequestBean userNameRegisterRequestBean=new UserNameRegisterRequestBean();
        userNameRegisterRequestBean.setUsername(account);
        userNameRegisterRequestBean.setPassword(password);
        HttpParamsBuild httpParamsBuild=new HttpParamsBuild(GsonUtil.getGson().toJson(userNameRegisterRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<LoginResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(LoginResultBean data) {
                if(data!=null){
                    LoginControl.saveToken(data.getUser_token());
//                    T.s(loginActivity,"登陆成功："+data.getCp_user_token());
                    T.s(mContext,"注册成功");
                    //接口回调通知
                    //保存账号到数据库
                    if (!UserLoginInfodao.getInstance(mContext).findUserLoginInfoByName(account)) {
                        UserLoginInfodao.getInstance(mContext).saveUserLoginInfo(account, password);
                    } else {
                        UserLoginInfodao.getInstance(mContext).deleteUserLoginByName(account);
                        UserLoginInfodao.getInstance(mContext).saveUserLoginInfo(account, password);
                    }
                    finish();
                    MainActivity.start(mContext,0);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        httpCallbackDecode.setLoadMsg("注册中...");
        RxVolley.post(AppApi.getUrl(AppApi.registerUserNameApi), httpParamsBuild.getHttpParams(),httpCallbackDecode);
    }

    private void submitRegisterByPhone() {
        final String account =etPhone.getText().toString().trim();
        final String password = etPhonePassword.getText().toString().trim();
        String authCode = etAuthCode.getText().toString().trim();
        if(!BaseTextUtil.isMobileNumber(account)){
            T.s(mContext,"请输入正确的手机号");
            return;
        }
        Pattern p = Pattern.compile("([a-zA-Z0-9]{6,16})");
        if (!p.matcher(password).matches()) {
            T.s(mContext,"密码只能由6至12位英文或数字组成");
            return;
        }
        if(TextUtils.isEmpty(authCode)){
            T.s(mContext, "请先输入验证码");
            return;
        }
        RegisterMobileRequestBean registerMobileRequestBean=new RegisterMobileRequestBean();
        registerMobileRequestBean.setMobile(account);
        registerMobileRequestBean.setPassword(password);
        registerMobileRequestBean.setSmscode(authCode);
        registerMobileRequestBean.setSmstype(SmsSendRequestBean.TYPE_REGISTER);
        HttpParamsBuild httpParamsBuild=new HttpParamsBuild(GsonUtil.getGson().toJson(registerMobileRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<LoginResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(LoginResultBean data) {
                if(data!=null){
                    LoginControl.saveToken(data.getUser_token());
                    T.s(mContext,"注册成功");
                    //接口回调通知
                    //保存账号到数据库
                    if (!UserLoginInfodao.getInstance(mContext).findUserLoginInfoByName(account)) {
                        UserLoginInfodao.getInstance(mContext).saveUserLoginInfo(account, password);
                    } else {
                        UserLoginInfodao.getInstance(mContext).deleteUserLoginByName(account);
                        UserLoginInfodao.getInstance(mContext).saveUserLoginInfo(account, password);
                    }
                    finish();
                    MainActivity.start(mContext,0);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        httpCallbackDecode.setLoadMsg("注册中...");
        RxVolley.post(AppApi.getUrl(AppApi.registerMobileApi), httpParamsBuild.getHttpParams(),httpCallbackDecode);

    }

    private void sendSms() {
        final String account = etPhone.getText().toString().trim();
        if(!BaseTextUtil.isMobileNumber(account)){
            T.s(mContext,"请输入正确的手机号");
            return;
        }
        SmsSendRequestBean smsSendRequestBean=new SmsSendRequestBean();
        smsSendRequestBean.setMobile(account);
        smsSendRequestBean.setSmstype(SmsSendRequestBean.TYPE_REGISTER);
        HttpParamsBuild httpParamsBuild=new HttpParamsBuild(GsonUtil.getGson().toJson(smsSendRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<SmsSendResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(SmsSendResultBean data) {
                if(data!=null){
                    //开始计时控件
                    startCodeTime(60);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        httpCallbackDecode.setLoadMsg("发送中...");
        RxVolley.post(AppApi.getUrl(AppApi.smsSendApi), httpParamsBuild.getHttpParams(),httpCallbackDecode);
    }
    private void startCodeTime(int time) {
        tvGetAuthCode.setTag(time);
        if(time<=0){
            tvGetAuthCode.setText("获取验证码");
            tvGetAuthCode.setClickable(true);
            return;
        }else{
            tvGetAuthCode.setClickable(false);
            tvGetAuthCode.setText(time+"秒");
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int delayTime = (int) tvGetAuthCode.getTag();
                startCodeTime(--delayTime);

            }
        },1000);
    }
}
