package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.etsdk.app.huov7.ui.dialog.CouponExchangeDialogUtil;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/15.
 */

public class EarnActivity extends ImmerseActivity {
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earn);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        tvTitleName.setText("我要赚钱");
        getUserInfoData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(Boolean isLogin) {
        if (isLogin){
        }else {
            finish();
        }
    }

    @OnClick({R.id.iv_titleLeft, R.id.sign_ll, R.id.attention_ll, R.id.invite_ll, R.id.active_ll, R.id.demo_ll, R.id.play_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.sign_ll:
                SignInActivity.start(EarnActivity.this);
                break;
            case R.id.attention_ll:
                WeAccountsActivity.start(EarnActivity.this);
                break;
            case R.id.invite_ll:
                InviteActivity.start(EarnActivity.this);
                break;
            case R.id.active_ll:
                EventActivity.start(EarnActivity.this, "2", null);
                break;
            case R.id.demo_ll:
                new CouponExchangeDialogUtil().showExchangeDialog(EarnActivity.this, "友情提示", "该功能正在开发中，敬请期待");
                break;
            case R.id.play_ll:
                new CouponExchangeDialogUtil().showExchangeDialog(EarnActivity.this, "友情提示", "该功能正在开发中，敬请期待");
                break;
        }
    }

    public void getUserInfoData() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserInfoResultBean>(EarnActivity.this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
            }

            @Override
            public void onFailure(String code, String msg) {
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userDetailApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, EarnActivity.class);
        context.startActivity(starter);
    }
}
