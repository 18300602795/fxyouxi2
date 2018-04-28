package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.FeedbackRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.T;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedBackActivity extends ImmerseActivity {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.activity_game_feedback)
    LinearLayout activityGameFeedback;
    @BindView(R.id.et_feedbackContent)
    EditText etFeedbackContent;
    @BindView(R.id.et_feedbackPhone)
    EditText etFeedbackPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("意见反馈");
    }

    private void submitFeedBack() {
        final FeedbackRequestBean feedbackRequestBean = new FeedbackRequestBean();
        String content = etFeedbackContent.getText().toString().trim();
        String phone = etFeedbackPhone.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            T.s(mContext, "反馈内容不能为空");
            return;
        }
        if(!TextUtils.isEmpty(phone)){
            feedbackRequestBean.setLinkman(phone);
        }
        feedbackRequestBean.setContent(content);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(feedbackRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<String>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(String data) {
                T.s(mContext, "反馈成功");
                finish();
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.guestbookWriteApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
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
                submitFeedBack();
                break;
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, FeedBackActivity.class);
        context.startActivity(starter);
    }
}
