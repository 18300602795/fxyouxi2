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
import com.etsdk.app.huov7.model.LoginResultBean;
import com.etsdk.app.huov7.model.UserNameRegisterRequestBean;
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


public class UserNameRegisterActivityV1 extends ImmerseActivity {


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
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username_register_v1);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleLeft.setCompoundDrawables(null,null,null,null);
        tvTitleLeft.setText("< 返回登录");
        tvTitleRight.setText("手机注册 >");
        tvTitleName.setText("用户注册");
        btnSubmit.setText("注册");
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, UserNameRegisterActivityV1.class);
        context.startActivity(starter);
    }

    public static Intent getIntent(Context context) {
        Intent starter = new Intent(context, UserNameRegisterActivityV1.class);
        return starter;
    }

    @OnClick({R.id.tv_titleLeft, R.id.tv_title_right, R.id.huo_sdk_img_show_pwd, R.id.btn_submit, R.id.tv_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_titleLeft:
                finish();
                break;
            case R.id.tv_title_right:
                PhoneRegisterActivityV1.start(mActivity);
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
            case R.id.btn_submit:
                submitRegisterByAccount();
                break;
            case R.id.tv_agreement:
                WebViewActivity.start(mActivity, "平台用户协议", AppApi.getUrl(AppApi.agreementRegisterUrl));
                break;
        }
    }

    public static boolean isSimplePassword(String password) {
        if (TextUtils.isDigitsOnly(password)) {
            char tempCh = '0';
            for (int i = 0; i < password.length(); i++) {
                if (i == 0) {
                    tempCh = password.charAt(i);
                } else {
                    if (((int) tempCh + 1) != ((int) (password.charAt(i)))) {
                        L.e("hongliang", ((int) tempCh + 1) + " " + ((int) (password.charAt(i))));
                        return false;
                    }
                    tempCh = password.charAt(i);
                }
            }
            return true;
        }
        return false;
    }

    private void submitRegisterByAccount() {
        final String account = huoSdkEtAccount.getText().toString().trim();
        final String password = huoSdkEtPwd.getText().toString().trim();
        if(BaseTextUtil.isMobileNumber(account)){
            T.s(mActivity,"账号只能由6位以上英文加数字组成");
            return;
        }
        Pattern p = Pattern.compile("([a-zA-Z0-9]{6,16})");
        if (!p.matcher(account).matches()) {
            T.s(mActivity,"账号只能由6至12位英文或数字组成");
            return;
        }
        if (!p.matcher(password).matches()) {
            T.s(mContext,"密码只能由6至12位英文或数字组成");
            return;
        }
        if(isSimplePassword(password)){
            T.s(mActivity,"亲，密码太简单，请重新输入");
            return;
        }
        UserNameRegisterRequestBean userNameRegisterRequestBean=new UserNameRegisterRequestBean();
        userNameRegisterRequestBean.setUsername(account);
        userNameRegisterRequestBean.setPassword(password);
        HttpParamsBuild httpParamsBuild=new HttpParamsBuild(GsonUtil.getGson().toJson(userNameRegisterRequestBean));
        L.e("333", GsonUtil.getGson().toJson(userNameRegisterRequestBean));
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
}
