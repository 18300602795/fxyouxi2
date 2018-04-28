package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.PwdFindRequestBean;
import com.etsdk.app.huov7.model.RegisterResultBean;
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


public class FindPwdByPhoneActivity extends ImmerseActivity {


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
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.huo_sdk_et_account)
    EditText huoSdkEtAccount;
    @BindView(R.id.huo_sdk_iv_pwd)
    ImageView huoSdkIvPwd;
    @BindView(R.id.view_line1)
    View viewLine1;
    @BindView(R.id.huo_sdk_img_show_pwd)
    ImageView huoSdkImgShowPwd;
    @BindView(R.id.huo_sdk_et_pwd)
    EditText huoSdkEtPwd;
    @BindView(R.id.huo_sdk_iv_code)
    ImageView huoSdkIvCode;
    @BindView(R.id.view_line2)
    View viewLine2;
    @BindView(R.id.huo_sdk_btn_code)
    Button huoSdkBtnCode;
    @BindView(R.id.huo_sdk_et_code)
    EditText huoSdkEtCode;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd_by_phone);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleLeft.setText("");
        tvTitleRight.setText("");
        tvTitleName.setText("密保手机找回");
        btnSubmit.setText("提交");
        username = getIntent().getStringExtra("username");
    }

    public static void start(Context context, String username) {
        Intent starter = new Intent(context, FindPwdByPhoneActivity.class);
        starter.putExtra("username",username);
        context.startActivity(starter);
    }

    public static Intent getIntent(Context context) {
        Intent starter = new Intent(context, FindPwdByPhoneActivity.class);
        return starter;
    }

    @OnClick({R.id.tv_titleLeft,R.id.huo_sdk_img_show_pwd, R.id.huo_sdk_btn_code, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_titleLeft:
                finish();
                break;
            case R.id.huo_sdk_img_show_pwd:
                if (huoSdkImgShowPwd.isSelected()) {
                    huoSdkEtPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    huoSdkEtPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                huoSdkImgShowPwd.setSelected(!huoSdkImgShowPwd.isSelected());
                break;
            case R.id.huo_sdk_btn_code:
                sendSms();
                break;
            case R.id.btn_submit:
                submitUpdatePassword();
                break;
        }
    }
    public static boolean isSimplePassword(String password){
        if(TextUtils.isDigitsOnly(password)){
            char tempCh='0';
            for(int i=0;i<password.length();i++){
                if(i==0){
                    tempCh=password.charAt(i);
                }else{
                    if( ((int)tempCh+1) != ((int)(password.charAt(i))) ){
                        return false;
                    }
                    tempCh=password.charAt(i);
                }
            }
            return true;
        }
        return false;
    }
    private void submitUpdatePassword() {
        final String password = huoSdkEtPwd.getText().toString().trim();
        String authCode = huoSdkEtCode.getText().toString().trim();
        if (password.length()<6) {
            T.s(mActivity,"密码由6位以上英文或数字组成");
            return;
        }
        if(isSimplePassword(password)){
            T.s(mActivity,"亲，密码太简单，请重新输入");
            return;
        }
        if(TextUtils.isEmpty(authCode)){
            T.s(mContext, "请先输入验证码");
            return;
        }
        PwdFindRequestBean pwdFindRequestBean=new PwdFindRequestBean();
        pwdFindRequestBean.setUsername(username);
        pwdFindRequestBean.setMobile(huoSdkEtAccount.getText().toString().trim());
        pwdFindRequestBean.setPassword(password);
        pwdFindRequestBean.setCode(authCode);
        pwdFindRequestBean.setType(SmsSendRequestBean.TYPE_FIND_PWD);
        HttpParamsBuild httpParamsBuild=new HttpParamsBuild(GsonUtil.getGson().toJson(pwdFindRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<RegisterResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(RegisterResultBean data) {
                T.s(mContext,"修改成功，请重新登陆");
                LoginActivityV1.start(mActivity);
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.passwordFindApi), httpParamsBuild.getHttpParams(),httpCallbackDecode);

    }
    private void sendSms() {
        final String account = huoSdkEtAccount.getText().toString().trim();
        if(!BaseTextUtil.isMobileNumber(account)){
            T.s(mContext,"请输入正确的手机号");
            return;
        }
        SmsSendRequestBean smsSendRequestBean=new SmsSendRequestBean();
        smsSendRequestBean.setMobile(account);
        smsSendRequestBean.setSmstype(SmsSendRequestBean.TYPE_FIND_PWD);
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
        huoSdkBtnCode.setTag(time);
        if(time<=0){
            huoSdkBtnCode.setText("获取验证码");
            huoSdkBtnCode.setClickable(true);
            return;
        }else{
            huoSdkBtnCode.setClickable(false);
            huoSdkBtnCode.setText(time+"秒");
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int delayTime = (int) huoSdkBtnCode.getTag();
                startCodeTime(--delayTime);

            }
        },1000);
    }
}
