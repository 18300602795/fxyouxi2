package com.etsdk.app.huov7.view;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.BuildConfig;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.LoginRequestBean;
import com.etsdk.app.huov7.model.LoginResultBean;
import com.etsdk.app.huov7.sharesdk.ThirdLoginUtil;
import com.etsdk.app.huov7.ui.ForgetPwdActivity;
import com.etsdk.app.huov7.ui.MainActivity;
import com.etsdk.app.huov7.util.ViewStackManager;
import com.game.sdk.db.impl.UserLoginInfodao;
import com.game.sdk.domain.UserInfo;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.control.LoginControl;
import com.liang530.log.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created by liu hong liang on 2017/2/6.
 */

public class LoginView extends FrameLayout {

    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_forgetPwd)
    TextView tvForgetPwd;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.iv_qq)
    ImageView ivQq;
    @BindView(R.id.iv_weixin)
    ImageView ivWeixin;
    @BindView(R.id.iv_weibo)
    ImageView ivWeibo;
    @BindView(R.id.ll_third_login)
    LinearLayout llThirdLogin;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.ll_loginView)
    LinearLayout llLoginView;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    private Activity mActivity;
    private ViewStackManager viewStackManager;
    private PlatformActionListener loginListener;

    public LoginView(Context context) {
        super(context);
        initUI();
    }

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public LoginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    private void initUI() {
        mActivity = (Activity) getContext();
        viewStackManager = ViewStackManager.getInstance(mActivity);
        LayoutInflater.from(getContext()).inflate(R.layout.view_login, this);
        ButterKnife.bind(this);
        UserInfo userInfoLast = UserLoginInfodao.getInstance(mActivity).getUserInfoLast();
        if (userInfoLast != null && !TextUtils.isEmpty(userInfoLast.username)) {
            etAccount.setText(userInfoLast.username);
            etPassword.setText(userInfoLast.password);
        }
        //第三方登录，根据是否有对应参数显示，默认隐藏
        if (ThirdLoginUtil.isQQEnable() || ThirdLoginUtil.isWeiXinEnable() || ThirdLoginUtil.isWeiBoEnable()) {
            llThirdLogin.setVisibility(VISIBLE);
        }
        if (ThirdLoginUtil.isQQEnable()) {
            ivQq.setVisibility(VISIBLE);
        }
        if (ThirdLoginUtil.isWeiXinEnable()) {
            ivWeixin.setVisibility(VISIBLE);
        }
        if (ThirdLoginUtil.isWeiBoEnable()) {
            ivWeibo.setVisibility(VISIBLE);
        }
    }

    public void setThirdLoginListener(PlatformActionListener loginListener) {
        this.loginListener = loginListener;
    }

    @OnClick({R.id.btn_login, R.id.tv_forgetPwd, R.id.tv_register, R.id.iv_qq, R.id.iv_weibo, R.id.iv_weixin,R.id.iv_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                sumitLogin();
                break;
            case R.id.tv_forgetPwd:
                ForgetPwdActivity.start(mActivity);
                break;
            case R.id.tv_register:
                viewStackManager.showView(viewStackManager.getViewByClass(RegisterView.class));
                break;
            case R.id.iv_qq:
                ThirdLoginUtil.initQQ(BuildConfig.qq_appid, BuildConfig.qq_appkey);
                ThirdLoginUtil.loginByThird(ThirdLoginUtil.LOGIN_QQ, loginListener);
                break;
            case R.id.iv_weibo:
                ThirdLoginUtil.initXinNan(BuildConfig.wb_appid, BuildConfig.wb_appkey, BuildConfig.wb_directurl);
                ThirdLoginUtil.loginByThird(ThirdLoginUtil.LOGIN_XLWB, loginListener);
                break;
            case R.id.iv_weixin:
                ThirdLoginUtil.initWx(BuildConfig.wx_appid, BuildConfig.wx_appkey);
                ThirdLoginUtil.loginByThird(ThirdLoginUtil.LOGIN_WX, loginListener);
                break;
            case R.id.iv_close:
                mActivity.finish();
                MainActivity.start(mActivity,0);
                break;
        }
    }
    private List<LoginResultBean.UserName> getTestList(){
        List<LoginResultBean.UserName> userNameList=new ArrayList<>();
        userNameList.add(new LoginResultBean.UserName("aaaa123"));
        userNameList.add(new LoginResultBean.UserName("bbbb123"));
        userNameList.add(new LoginResultBean.UserName("fdsaxxfs"));
        return userNameList;
    }
    private void sumitLogin() {
        final String account = etAccount.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
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
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<LoginResultBean>(getContext(), httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(LoginResultBean data) {
                if (data != null) {
//                    data.setUserlist(getTestList());
                    if(data.getUserlist()!=null&&data.getUserlist().size()>1){
                        SelectAccountView selectAccountView = (SelectAccountView) viewStackManager.getViewByClass(SelectAccountView.class);
//                        selectAccountView.setUserNameList(dialog, data.getUserlist(),password);
                        //填对话框选择账号进行登陆
                        viewStackManager.showView(viewStackManager.getViewByClass(SelectAccountView.class));
                        return;
                    }
                    LoginControl.saveToken(data.getUser_token());
                    T.s(mActivity, "登陆成功");
                    //接口回调通知
                    //保存账号到数据库
                    if (!UserLoginInfodao.getInstance(mActivity).findUserLoginInfoByName(account)) {
                        UserLoginInfodao.getInstance(mActivity).saveUserLoginInfo(account, password);
                    } else {
                        UserLoginInfodao.getInstance(mActivity).deleteUserLoginByName(account);
                        UserLoginInfodao.getInstance(mActivity).saveUserLoginInfo(account, password);
                    }
                    ((Activity) getContext()).finish();
                    MainActivity.start(mActivity, 4);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        httpCallbackDecode.setLoadMsg("正在登录...");
        RxVolley.post(AppApi.getUrl(AppApi.loginApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }
}
