package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.AccountInfoRequestBean;
import com.etsdk.app.huov7.model.AccountResultBean;
import com.etsdk.app.huov7.ui.dialog.SelectAccountLoginDialog;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.log.T;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPwdInputAccountActivity extends ImmerseActivity {

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
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.tv_error_hint)
    TextView tvErrorHint;
    AccountResultBean accountResultBean;
    String errorHint = "账号不存在或者账号错误，请重新输入！";
    String errorHintHtml = "<html><head><title></title></head><body>"
            + "<font color=\"#fd0004\">对不起您没有绑定手机号或邮箱，请选择</font>"
            + "<font color=\"#0000ff\">联系客服</font>"
            + "<font color=\"#fd0004\">找回</font>"
            + "</body></html>";
    @BindView(R.id.ll_error_hint)
    LinearLayout llErrorHint;
    private SelectAccountLoginDialog selectAccountLoginDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd_input_account);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("忘记密码");
        tvTitleLeft.setText("");
        tvTitleRight.setVisibility(View.GONE);
        String html = "<html><head><title></title></head><body>"
                + "<font color=\"#fd0004\">对不起您没有绑定手机号或邮箱，请选择</font>"
                + "<font color=\"#0000ff\">联系客服</font>"
                + "<font color=\"#fd0004\">找回</font>"
                + "</body></html>";
        tvErrorHint.setText(Html.fromHtml(html));
        selectAccountLoginDialog = new SelectAccountLoginDialog(new SelectAccountLoginDialog.SelectAccountListener() {
            @Override
            public void onSelectAccount(String username) {
                huoSdkEtAccount.setText(username);
                getAccountInfoByNet();
            }
        });
    }

    public void getAccountInfoByNet() {
        String account = huoSdkEtAccount.getText().toString();
        if (TextUtils.isEmpty(account)) {
            T.s(mActivity, "请先输入账号");
            return;
        }
        final AccountInfoRequestBean accountInfoRequestBean = new AccountInfoRequestBean();
        accountInfoRequestBean.setUsername(account);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(accountInfoRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<AccountResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(AccountResultBean data) {
            }

            @Override
            public void onDataSuccess(AccountResultBean data, String code, String msg) {
                if ("300".equals(code)) {
                    tvErrorHint.setText(errorHint);
                    llErrorHint.setVisibility(View.VISIBLE);
                } else {
                    if (data != null) {
                        updateAccountInfo(data);
                    } else {
                        tvErrorHint.setText(Html.fromHtml(errorHintHtml));
                        llErrorHint.setVisibility(View.VISIBLE);
                    }
                }

            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.accountInfoApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);


    }

    private void updateAccountInfo(AccountResultBean accountResultBean) {
        this.accountResultBean = accountResultBean;
        if (accountResultBean.getUserlist() != null && accountResultBean.getUserlist().size() > 1) {
            selectAccountLoginDialog.showAccountLoginDialog(mActivity, accountResultBean.getUserlist());
            //填对话框选择账号进行登陆
            return;
        }
        if (TextUtils.isEmpty(accountResultBean.getEmail()) && TextUtils.isEmpty(accountResultBean.getMobile())) {
            tvErrorHint.setText(errorHint);
            llErrorHint.setVisibility(View.VISIBLE);
        } else {
            SelectMiBaoActivity.start(mActivity,huoSdkEtAccount.getText().toString().trim(),accountResultBean.getMobile(),accountResultBean.getEmail());
            finish();
        }
    }

    @OnClick({R.id.tv_error_hint, R.id.btn_submit, R.id.tv_titleLeft})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_error_hint:
                ServiceActivity.start(mActivity);
                break;
            case R.id.btn_submit:
                getAccountInfoByNet();
                break;
            case R.id.tv_titleLeft:
                finish();
                break;
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ForgetPwdInputAccountActivity.class);
        context.startActivity(starter);
    }

    public static Intent getIntent(Context context) {
        Intent starter = new Intent(context, ForgetPwdInputAccountActivity.class);
        return starter;
    }

    @OnClick(R.id.tv_titleLeft)
    public void onViewClicked() {
    }
}
