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
import com.etsdk.app.huov7.model.RegisterMobileRequestBean;
import com.etsdk.app.huov7.model.RegisterResultBean;
import com.etsdk.app.huov7.model.SmsSendRequestBean;
import com.etsdk.app.huov7.model.SmsSendResultBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.T;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.utils.BaseTextUtil;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPwdActivity extends ImmerseActivity {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_authCode)
    EditText etAuthCode;
    @BindView(R.id.tv_getAuthCode)
    TextView tvGetAuthCode;
    @BindView(R.id.ll_phone_register)
    LinearLayout llPhoneRegister;
    @BindView(R.id.et_phonePassword)
    EditText etPhonePassword;
    @BindView(R.id.et_confirmPwd)
    EditText etConfirmPwd;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("找回密码");
    }

    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight, R.id.tv_getAuthCode, R.id.btn_submit})
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
            case R.id.btn_submit:
                submitUpdatePassword();
                break;
        }
    }

    private void submitUpdatePassword() {
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
        registerMobileRequestBean.setSmstype(SmsSendRequestBean.TYPE_UPDATE_PWD);
        HttpParamsBuild httpParamsBuild=new HttpParamsBuild(GsonUtil.getGson().toJson(registerMobileRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<RegisterResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(RegisterResultBean data) {
                T.s(mContext,"修改成功，请重新登陆");
                finish();
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.passwordFindApi), httpParamsBuild.getHttpParams(),httpCallbackDecode);

    }
    private void sendSms() {
        final String account = etPhone.getText().toString().trim();
        if(!BaseTextUtil.isMobileNumber(account)){
            T.s(mContext,"请输入正确的手机号");
            return;
        }
        SmsSendRequestBean smsSendRequestBean=new SmsSendRequestBean();
        smsSendRequestBean.setMobile(account);
        smsSendRequestBean.setSmstype(SmsSendRequestBean.TYPE_UPDATE_INFO);
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
    public static void start(Context context) {
        Intent starter = new Intent(context, ForgetPwdActivity.class);
        context.startActivity(starter);
    }
}
