package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.T;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountSaveActivity extends ImmerseActivity {


    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.ll_mail)
    LinearLayout llMail;
    @BindView(R.id.ll_password)
    LinearLayout llPassword;

    private boolean bindPhoneClickable = false;
    private String phone = null;
    private String email = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_save);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("账号安全");
        tvTitleRight.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfoData();
    }

    private void updateUserInfoData(UserInfoResultBean userInfoResultBean) {
        phone = userInfoResultBean.getMobile();
        email = userInfoResultBean.getEmail();
        bindPhoneClickable = true;
    }

    public void getUserInfoData() {
        bindPhoneClickable = false;
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserInfoResultBean>(AccountSaveActivity.this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                if (data != null) {
                    updateUserInfoData(data);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userDetailApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, AccountSaveActivity.class);
        context.startActivity(starter);
    }

    @OnClick({R.id.iv_titleLeft, R.id.ll_phone, R.id.ll_mail, R.id.ll_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.ll_phone:
                if(!bindPhoneClickable){
                    T.s(mContext, "正在查询绑定状态，请稍后");
                    break;
                }
                if(TextUtils.isEmpty(phone)){
                    BindPhoneActivity.start(mContext);
                }else{
                    AuthPhoneActivity.start(mContext,phone);
                }
                break;
            case R.id.ll_mail:
                if(!bindPhoneClickable){
                    T.s(mContext, "正在查询绑定状态，请稍后");
                    break;
                }
                if(TextUtils.isEmpty(email)){
                    //绑定邮箱
                    BindEmailActivity.start(mContext);
                }else{
                    //解除绑定
                    AuthEmailActivity.start(mContext,email);
                }
                break;
            case R.id.ll_password:
                UpdatePwdActivity.start(mContext);
                break;
        }
    }
}
