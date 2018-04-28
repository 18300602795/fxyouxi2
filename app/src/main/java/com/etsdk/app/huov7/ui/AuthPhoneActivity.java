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

/**
 * Created by liu hong liang on 2017/2/23.
 * 更新手机号之验证手机号
 */
public class AuthPhoneActivity extends ImmerseActivity {

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
    private String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone1);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("更换手机号");
        phone=getIntent().getStringExtra("phone");
        tvShowPhone.setText(String.format(getString(R.string.update_phone_show),phone));
    }

    @OnClick({ R.id.tv_getAuthCode, R.id.btn_submit,R.id.iv_titleLeft,R.id.tv_titleRight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_getAuthCode:
                sendSms();
                break;
            case R.id.btn_submit:
                authPhone();
                break;
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
        }
    }
    private void sendSms() {
        final String account = phone;
        if(!BaseTextUtil.isMobileNumber(account)){
            T.s(mActivity,"请输入正确的手机号");
            return;
        }
        SmsSendRequestBean smsSendRequestBean=new SmsSendRequestBean();
        smsSendRequestBean.setMobile(account);
        smsSendRequestBean.setSmstype(SmsSendRequestBean.TYPE_UPDATE_INFO);
        HttpParamsBuild httpParamsBuild=new HttpParamsBuild(GsonUtil.getGson().toJson(smsSendRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<SmsSendResultBean>(mActivity, httpParamsBuild.getAuthkey()) {
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
    private void authPhone() {
        final String account =phone;
        String authCode = etAuthCode.getText().toString().trim();
        if(!BaseTextUtil.isMobileNumber(account)){
            T.s(mContext,"请输入正确的手机号");
            return;
        }
        if(TextUtils.isEmpty(authCode)){
            T.s(mContext, "请先输入验证码");
            return;
        }
        RegisterMobileRequestBean registerMobileRequestBean=new RegisterMobileRequestBean();
        registerMobileRequestBean.setMobile(account);
        registerMobileRequestBean.setSmscode(authCode);
        registerMobileRequestBean.setSmstype(SmsSendRequestBean.TYPE_UPDATE_INFO);
        HttpParamsBuild httpParamsBuild=new HttpParamsBuild(GsonUtil.getGson().toJson(registerMobileRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<AuthSmsCodeResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(AuthSmsCodeResultBean data) {
                if(data!=null&&"2".equals(data.getStatus())){
                    BindPhoneActivity.start(mContext);
                    finish();
                }else{
                    T.s(mContext,"验证失败");
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.userPhoneVerifyApi), httpParamsBuild.getHttpParams(),httpCallbackDecode);
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
    public static void start(Context context,String phone) {
        Intent starter = new Intent(context, AuthPhoneActivity.class);
        starter.putExtra("phone",phone);
        context.startActivity(starter);
    }
}
