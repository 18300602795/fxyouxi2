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
import com.etsdk.app.huov7.model.EmailCodeSendRequestBean;
import com.etsdk.app.huov7.model.RegisterEmailRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.T;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindEmailActivity extends ImmerseActivity {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.et_phone)
    EditText etEmail;
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
        setContentView(R.layout.activity_bind_email);
        ButterKnife.bind(this);
        setupUI();

    }

    private void setupUI() {
        tvTitleName.setText("绑定邮箱");
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
                bindEmail();
                break;
            case R.id.tv_getAuthCode:
                sendCode();
                break;
        }
    }

    private void bindEmail() {
        final String email = etEmail.getText().toString().trim();
        String authCode = etAuthCode.getText().toString().trim();
        if (!isEmailtext(email)) {
            T.s(mContext, "请输入正确的邮箱");
            return;
        }
        if (TextUtils.isEmpty(authCode)) {
            T.s(mContext, "请先输入验证码");
            return;
        }
        RegisterEmailRequestBean registerEmailRequestBean = new RegisterEmailRequestBean();
        registerEmailRequestBean.setCode(authCode);
        registerEmailRequestBean.setEmail(email);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(registerEmailRequestBean));
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
        RxVolley.post(AppApi.getUrl(AppApi.registerEmailApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);

    }

    private void sendCode() {
        final String email = etEmail.getText().toString().trim();
        if (!isEmailtext(email)) {
            T.s(mContext, "请输入正确的邮箱");
            return;
        }
        EmailCodeSendRequestBean emailCodeSendRequestBean = new EmailCodeSendRequestBean();
        emailCodeSendRequestBean.setEmail(email);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(emailCodeSendRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<AuthSmsCodeResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(AuthSmsCodeResultBean data) {
                if (data != null) {
                    //开始计时控件
                    T.s(BindEmailActivity.this, "已发送验证码到邮箱："+email+"\n请注意查收");
                    startCodeTime(60);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        httpCallbackDecode.setLoadMsg("发送中...");
        RxVolley.post(AppApi.getUrl(AppApi.emailCodeSend), httpParamsBuild.getHttpParams(), httpCallbackDecode);
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
        Intent starter = new Intent(context, BindEmailActivity.class);
        context.startActivity(starter);
    }

    public static Intent getIntent(Context context) {
        Intent starter = new Intent(context, BindEmailActivity.class);
        return starter;
    }

    /**
     * ^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$
     * @param email
     * @return
     */
    private boolean isEmailtext(String email){
        Pattern p = Pattern.compile("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        Matcher m = p.matcher(email);
        return m.matches();
    }

}
