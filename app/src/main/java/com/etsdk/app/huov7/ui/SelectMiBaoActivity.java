package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectMiBaoActivity extends ImmerseActivity {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.iv_title_down)
    ImageView ivTitleDown;
    @BindView(R.id.huo_sdk_rl_title)
    RelativeLayout huoSdkRlTitle;
    @BindView(R.id.tv_mibao_phone)
    TextView tvMibaoPhone;
    @BindView(R.id.ll_mibao_phone)
    LinearLayout llMibaoPhone;
    @BindView(R.id.tv_mibao_email)
    TextView tvMibaoEmail;
    @BindView(R.id.ll_mibao_email)
    LinearLayout llMibaoEmail;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mi_bao);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("找回密码");
        username = getIntent().getStringExtra("username");
        String mobile = getIntent().getStringExtra("mobile");
        String email = getIntent().getStringExtra("email");
        if(TextUtils.isEmpty(mobile)){
            llMibaoPhone.setVisibility(View.GONE);
        }else{
            llMibaoPhone.setVisibility(View.VISIBLE);
            tvMibaoPhone.setText("通过密保手机"+mobile);
        }
        if(TextUtils.isEmpty(email)){
            llMibaoEmail.setVisibility(View.GONE);
        }else{
            llMibaoEmail.setVisibility(View.VISIBLE);
            tvMibaoEmail.setText("通过密保邮箱"+email);
        }
    }
    @OnClick({R.id.iv_titleLeft, R.id.ll_mibao_phone, R.id.ll_mibao_email})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.ll_mibao_phone:
                FindPwdByPhoneActivity.start(mActivity,username);
                break;
            case R.id.ll_mibao_email:
                FindPwdByEmailActivity.start(mActivity,username);
                break;
        }
    }
    public static void start(Context context, String username, String mobile, String email) {
        Intent starter = new Intent(context, SelectMiBaoActivity.class);
        starter.putExtra("username",username);
        starter.putExtra("mobile",mobile);
        starter.putExtra("email",email);
        context.startActivity(starter);
    }
    public static Intent getIntent(Context context) {
        Intent starter = new Intent(context, SelectMiBaoActivity.class);
        return starter;
    }
}
