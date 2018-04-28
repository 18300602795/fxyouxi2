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

/**
 * Created by jim on 2017/5/19.
 * 解绑邮箱
 */
public class AuthEmailActivity extends ImmerseActivity {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.tv_show_phone)
    TextView tvShowPhone;
    @BindView(R.id.et_authCode)
    EditText etAuthCode;
    @BindView(R.id.tv_getAuthCode)
    TextView tvGetAuthCode;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.activity_update_phone)
    LinearLayout activityUpdatePhone;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone1);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("解绑邮箱");
        email = getIntent().getStringExtra("email");
        tvShowPhone.setText(String.format(getString(R.string.update_email_show), email));
        tvDesc.setText("需要先验证已绑定邮箱，才能换绑新邮箱，请先验证");
    }

    @OnClick({R.id.tv_getAuthCode, R.id.btn_submit, R.id.iv_titleLeft, R.id.tv_titleRight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_getAuthCode:
                sendCode();
                break;
            case R.id.btn_submit:
                unBindEmail();
                break;
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
        }
    }

    private void sendCode() {
        final String account = email;
        if (!isEmailtext(account)) {
            T.s(mActivity, "请输入正确邮箱");
            return;
        }
        EmailCodeSendRequestBean emailCodeSendRequestBean = new EmailCodeSendRequestBean();
        emailCodeSendRequestBean.setEmail(email);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(emailCodeSendRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<AuthSmsCodeResultBean>(mActivity, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(AuthSmsCodeResultBean data) {
                if (data != null) {
                    //开始计时控件
                    T.s(mContext, "已发送验证码到邮箱：" + email + "\n请注意查收");
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

    private void unBindEmail() {
        final String account = email;
        String authCode = etAuthCode.getText().toString().trim();
        if (!isEmailtext(account)) {
            T.s(mActivity, "请输入正确邮箱");
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
                    T.s(mContext, "解绑成功");
                    finish();
                } else {
                    T.s(mContext, "验证失败");
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.emailUnbind), httpParamsBuild.getHttpParams(), httpCallbackDecode);
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

    public static void start(Context context, String email) {
        Intent starter = new Intent(context, AuthEmailActivity.class);
        starter.putExtra("email", email);
        context.startActivity(starter);
    }

    private boolean isEmailtext(String email) {
        Pattern p = Pattern.compile("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
