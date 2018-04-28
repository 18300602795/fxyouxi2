package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.RegisterResultBean;
import com.etsdk.app.huov7.model.UpdatePwdRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.T;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdatePwdActivity extends ImmerseActivity {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.et_oldPassword)
    EditText etOldPassword;
    @BindView(R.id.et_phonePassword)
    EditText etPhonePassword;
    @BindView(R.id.et_confirmPwd)
    EditText etConfirmPwd;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("密码修改");
    }

    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
            case R.id.btn_submit:
                submitUpdatePassword();
                break;
        }
    }
    private void submitUpdatePassword() {
        final String oldPassword =etOldPassword.getText().toString().trim();
        final String password = etPhonePassword.getText().toString().trim();
        final String confirmPwd = etConfirmPwd.getText().toString().trim();
        Pattern p = Pattern.compile("([a-zA-Z0-9]{6,16})");
        if (TextUtils.isEmpty(oldPassword)) {
            T.s(mContext,"请输入原密码");
            return;
        }
        if (!p.matcher(password).matches()) {
            T.s(mContext,"密码只能由6至12位英文或数字组成");
            return;
        }
        if(!password.equals(confirmPwd)){
            T.s(mContext,"两次输入密码不一致");
            return;
        }
        UpdatePwdRequestBean updatePwdRequestBean=new UpdatePwdRequestBean();
        updatePwdRequestBean.setOld_pwd(oldPassword);
        updatePwdRequestBean.setNew_pwd(password);
        HttpParamsBuild httpParamsBuild=new HttpParamsBuild(GsonUtil.getGson().toJson(updatePwdRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<RegisterResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(RegisterResultBean data) {
                T.s(mContext,"修改成功");
                finish();
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.passwordUpdateApi), httpParamsBuild.getHttpParams(),httpCallbackDecode);
    }
    public static void start(Context context) {
        Intent starter = new Intent(context, UpdatePwdActivity.class);
        context.startActivity(starter);
    }
}
