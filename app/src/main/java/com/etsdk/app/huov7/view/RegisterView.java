package com.etsdk.app.huov7.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.LoginResultBean;
import com.etsdk.app.huov7.model.RegisterMobileRequestBean;
import com.etsdk.app.huov7.model.SmsSendRequestBean;
import com.etsdk.app.huov7.model.SmsSendResultBean;
import com.etsdk.app.huov7.model.UserNameRegisterRequestBean;
import com.etsdk.app.huov7.ui.MainActivity;
import com.etsdk.app.huov7.ui.WebViewActivity;
import com.etsdk.app.huov7.util.ViewStackManager;
import com.game.sdk.db.impl.UserLoginInfodao;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.L;
import com.game.sdk.log.T;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.control.LoginControl;
import com.liang530.utils.BaseTextUtil;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liu hong liang on 2017/2/7.
 */

public class RegisterView extends FrameLayout {

    @BindView(R.id.tv_register_title)
    TextView tvRegisterTitle;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_phonePassword)
    EditText etPhonePassword;
    @BindView(R.id.et_authCode)
    EditText etAuthCode;
    @BindView(R.id.tv_getAuthCode)
    TextView tvGetAuthCode;
    @BindView(R.id.ll_phone_register)
    LinearLayout llPhoneRegister;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_confirmPwd)
    EditText etConfirmPwd;
    @BindView(R.id.ll_account_register)
    LinearLayout llAccountRegister;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    @BindView(R.id.tv_switch_register)
    TextView tvSwitchRegister;
    private Activity mActivity;
    private ViewStackManager viewStackManager;
    private boolean isPhoneRegister;
    private Handler mHandler=new Handler();

    public RegisterView(Context context) {
        super(context);
        initUI();
    }

    public RegisterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public RegisterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    private void initUI() {
        mActivity = (Activity) getContext();
        viewStackManager = ViewStackManager.getInstance(mActivity);
        LayoutInflater.from(getContext()).inflate(R.layout.view_register, this);
        ButterKnife.bind(this);
        switchRegisterUI(true);
    }

    private void switchRegisterUI(boolean isPhoneRegister) {
        this.isPhoneRegister = isPhoneRegister;
        if (isPhoneRegister) {
            llPhoneRegister.setVisibility(View.VISIBLE);
            llAccountRegister.setVisibility(View.GONE);
            tvSwitchRegister.setText("使用平台账号注册 ");
            tvRegisterTitle.setText("手机号注册");
        } else {
            llPhoneRegister.setVisibility(View.GONE);
            llAccountRegister.setVisibility(View.VISIBLE);
            tvSwitchRegister.setText("使用手机号注册 ");
            tvRegisterTitle.setText("用户注册");
        }
    }
    @OnClick({ R.id.tv_getAuthCode, R.id.btn_register, R.id.tv_agreement,
            R.id.tv_switch_register,R.id.tv_backLogin,R.id.iv_close})
    public void onClick(View view) {
        switch (view.getId()) {
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
                WebViewActivity.start(mActivity,"平台用户协议", AppApi.getUrl(AppApi.agreementRegisterUrl));
                break;
            case R.id.tv_switch_register:
                switchRegisterUI(!isPhoneRegister);
                break;
            case R.id.tv_backLogin:
                viewStackManager.showView(viewStackManager.getViewByClass(LoginView.class));
                break;
            case R.id.iv_close:
                mActivity.finish();
                MainActivity.start(mActivity,0);
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
                        L.e("hongliang",((int)tempCh+1)+" "+((int)(password.charAt(i))));
                        return false;
                    }
                    tempCh=password.charAt(i);
                }
            }
            return true;
        }
        return false;
    }

    private void submitRegisterByAccount() {
        final String account = etAccount.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String confirmPwd = etConfirmPwd.getText().toString().trim();
        if(BaseTextUtil.isMobileNumber(account)){
            T.s(mActivity,"账号只能由6位以上英文加数字组成");
            return;
        }
        Pattern p = Pattern.compile("([a-zA-Z0-9]{6,16})");
        if (!p.matcher(account).matches()) {
            T.s(mActivity,"账号只能由6至12位英文或数字组成");
            return;
        }
        if (password.length()<6) {
            T.s(mActivity,"密码由6位以上英文或数字组成");
            return;
        }
        if(isSimplePassword(password)){
            T.s(mActivity,"亲，密码太简单，请重新输入");
            return;
        }
        if(!password.equals(confirmPwd)){
            T.s(mActivity,"两次输入密码不一致");
            return;
        }
        UserNameRegisterRequestBean userNameRegisterRequestBean=new UserNameRegisterRequestBean();
        userNameRegisterRequestBean.setUsername(account);
        userNameRegisterRequestBean.setPassword(password);
        HttpParamsBuild httpParamsBuild=new HttpParamsBuild(GsonUtil.getGson().toJson(userNameRegisterRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<LoginResultBean>(mActivity, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(LoginResultBean data) {
                if(data!=null){
                    LoginControl.saveToken(data.getUser_token());
//                    T.s(loginActivity,"登陆成功："+data.getCp_user_token());
                    T.s(mActivity,"注册成功");
                    //接口回调通知
                    //保存账号到数据库
                    if (!UserLoginInfodao.getInstance(mActivity).findUserLoginInfoByName(account)) {
                        UserLoginInfodao.getInstance(mActivity).saveUserLoginInfo(account, password);
                    } else {
                        UserLoginInfodao.getInstance(mActivity).deleteUserLoginByName(account);
                        UserLoginInfodao.getInstance(mActivity).saveUserLoginInfo(account, password);
                    }
                    mActivity.finish();
                    MainActivity.start(mActivity,0);
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
            T.s(mActivity,"请输入正确的手机号");
            return;
        }
        if (password.length()<6) {
            T.s(mActivity,"密码由6位以上英文或数字组成");
            return;
        }
        if(isSimplePassword(password)){
            T.s(mActivity,"亲，密码太简单，请重新输入");
            return;
        }
        if(TextUtils.isEmpty(authCode)){
            T.s(mActivity, "请先输入验证码");
            return;
        }
        RegisterMobileRequestBean registerMobileRequestBean=new RegisterMobileRequestBean();
        registerMobileRequestBean.setMobile(account);
        registerMobileRequestBean.setPassword(password);
        registerMobileRequestBean.setSmscode(authCode);
        registerMobileRequestBean.setSmstype(SmsSendRequestBean.TYPE_REGISTER);
        HttpParamsBuild httpParamsBuild=new HttpParamsBuild(GsonUtil.getGson().toJson(registerMobileRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<LoginResultBean>(mActivity, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(LoginResultBean data) {
                if(data!=null){
                    LoginControl.saveToken(data.getUser_token());
                    T.s(mActivity,"注册成功");
                    //接口回调通知
                    //保存账号到数据库
                    if (!UserLoginInfodao.getInstance(mActivity).findUserLoginInfoByName(account)) {
                        UserLoginInfodao.getInstance(mActivity).saveUserLoginInfo(account, password);
                    } else {
                        UserLoginInfodao.getInstance(mActivity).deleteUserLoginByName(account);
                        UserLoginInfodao.getInstance(mActivity).saveUserLoginInfo(account, password);
                    }
                    mActivity.finish();
                    MainActivity.start(mActivity,3);
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
            T.s(mActivity,"请输入正确的手机号");
            return;
        }
        SmsSendRequestBean smsSendRequestBean=new SmsSendRequestBean();
        smsSendRequestBean.setMobile(account);
        smsSendRequestBean.setSmstype(SmsSendRequestBean.TYPE_REGISTER);
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
