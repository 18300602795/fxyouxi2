package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.ServiceQqAdapter;
import com.etsdk.app.huov7.adapter.ServiceQqGroupAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.HelpInfoBean;
import com.etsdk.app.huov7.view.StrokeTextView;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.jaeger.library.StatusBarUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.log.L;
import com.liang530.system.BasePhone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceActivity extends ImmerseActivity {
    @BindView(R.id.iv_back)
    ImageView btnBack;
    @BindView(R.id.ll_tel)
    LinearLayout llTel;
    @BindView(R.id.lv_service_qq)
    ListView lvServiceQq;
    @BindView(R.id.lv_qq_group)
    ListView lvQqGroup;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    @BindView(R.id.stv_service_time)
    StrokeTextView stvServiceTime;
    private ServiceQqAdapter serviceQqAdapter;
    private ServiceQqGroupAdapter serviceQqGroupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        ButterKnife.bind(this);
        setupUI();
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.bg_black_80), 0);
    }


    public void setupUI() {
        serviceQqGroupAdapter = new ServiceQqGroupAdapter();
        lvQqGroup.setAdapter(serviceQqGroupAdapter);
        serviceQqAdapter = new ServiceQqAdapter();
        lvServiceQq.setAdapter(serviceQqAdapter);
        getAboutUsInfo();
    }

    public void updateServiceInfoData(HelpInfoBean helpInfo) {
        if (helpInfo == null) {
            return;
        }
        tvTel.setText(helpInfo.getTel());
        stvServiceTime.setText(helpInfo.getService_time().replace("| ", "\n").replace('|', '\n'));//以竖线分隔
        serviceQqAdapter.setQqList(helpInfo.getQq());
        serviceQqGroupAdapter.setServiceQqGroupList(helpInfo.getQqgroup(), helpInfo.getQqgroupkey());
    }

    private void getAboutUsInfo() {
        BaseRequestBean requestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<HelpInfoBean>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(HelpInfoBean data) {
                if (data != null) {
                    updateServiceInfoData(data);
                }
            }
            @Override
            public void onFailure(String code, String msg) {
                L.e(TAG, code+" "+msg);
            }
        };
        httpCallbackDecode.setShowTs(false);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);//对话框继续使用install接口，在startup联网结束后，自动结束等待loading
        RxVolley.post(AppApi.getUrl(AppApi.getHelpInfo), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }


    public static void start(Context context) {
        Intent starter = new Intent(context, ServiceActivity.class);
        context.startActivity(starter);
    }

    public static Intent getIntent(Context context) {
        Intent starter = new Intent(context, ServiceActivity.class);
        return starter;
    }

    @OnClick({R.id.iv_back, R.id.ll_tel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_tel:
                BasePhone.callDial(this, tvTel.getText().toString());
                break;
        }
    }
}
