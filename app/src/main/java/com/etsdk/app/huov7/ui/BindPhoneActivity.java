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
import com.etsdk.app.huov7.model.AuthSmsCodeResultBean;
import com.etsdk.app.huov7.model.RegisterMobileRequestBean;
import com.etsdk.app.huov7.model.SmsSendRequestBean;
import com.etsdk.app.huov7.model.SmsSendResultBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.T;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.utils.BaseTextUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindPhoneActivity extends ImmerseActivity {

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
    @BindView(R.id.btn_bind)
    Button btnBind;
    @BindView(R.id.activity_bind_phone)
    LinearLayout activityBindPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        ButterKnife.bind(this);
        setupUI();

    }

    private void setupUI() {
        tvTitleName.setText("绑定手机号");
    }


    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight, R.id.btn_bind, R.id.tv_getAuthCode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
            case R.id.btn_bind:
                bindPhone();
                break;
            case R.id.tv_getAuthCode:
                sendSms();
                break;
        }
    }

    private void bindPhone() {
        final String account = etPhone.getText().toString().trim();
        String authCode = etAuthCode.getText().toString().trim();
        if (!BaseTextUtil.isMobileNumber(account)) {
            T.s(mContext, "请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(authCode)) {
            T.s(mContext, "请先输入验证码");
            return;
        }
        RegisterMobileRequestBean registerMobileRequestBean = new RegisterMobileRequestBean();
        registerMobileRequestBean.setMobile(account);
        registerMobileRequestBean.setSmscode(authCode);
        registerMobileRequestBean.setSmstype(SmsSendRequestBean.TYPE_FIND_PWD);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(registerMobileRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<AuthSmsCodeResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(AuthSmsCodeResultBean data) {
                if (data != null && "2".equals(data.getStatus())) {
                    T.s(mContext, "绑定成功");
                    finish();
                } else {
                    T.s(mContext, "绑定失败");
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.phoneBindApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);

    }

    private void sendSms() {
        final String account = etPhone.getText().toString().trim();
        if (!BaseTextUtil.isMobileNumber(account)) {
            T.s(mContext, "请输入正确的手机号");
            return;
        }
        SmsSendRequestBean smsSendRequestBean = new SmsSendRequestBean();
        smsSendRequestBean.setMobile(account);
        smsSendRequestBean.setSmstype(SmsSendRequestBean.TYPE_FIND_PWD);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(smsSendRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<SmsSendResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(SmsSendResultBean data) {
                if (data != null) {
                    //开始计时控件
                    startCodeTime(60);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        httpCallbackDecode.setLoadMsg("发送中...");
        RxVolley.post(AppApi.getUrl(AppApi.smsSendApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    private void startCodeTime(int time) {
        tvGetAuthCode.setTag(time);
        if (time <= 0) {
            tvGetAuthCode.setText("获取验证码");
            tvGetAuthCode.setClickable(true);
            return;
        } else {
            tvGetAuthCode.setClickable(false);
            tvGetAuthCode.setText(time + "秒");
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int delayTime = (int) tvGetAuthCode.getTag();
                startCodeTime(--delayTime);

            }
        }, 1000);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, BindPhoneActivity.class);
        context.startActivity(starter);
    }

    public static Intent getIntent(Context context) {
        Intent starter = new Intent(context, BindPhoneActivity.class);
        return starter;
    }

}
