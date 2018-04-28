package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.ReportListAdapter;
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

/**
 * Created by liu hong liang on 2017/2/6.
 * 游戏反馈ui
 */
public class GameFeedbackActivity extends ImmerseActivity {
    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.recycler_report_list)
    RecyclerView recyclerReportList;
    @BindView(R.id.activity_game_feedback)
    LinearLayout activityGameFeedback;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.et_feedbackContent)
    EditText etFeedbackContent;
    private ReportListAdapter reportListAdapter;
    private String gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_feedback);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("游戏反馈");
        gameId = getIntent().getStringExtra("gameId");
        recyclerReportList.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
        reportListAdapter = new ReportListAdapter();
        recyclerReportList.setAdapter(reportListAdapter);
    }

    private void submitFeedBack() {
        final FeedbackRequestBean feedbackRequestBean = new FeedbackRequestBean();
        String content = etFeedbackContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            T.s(mContext, "反馈内容不能为空");
            return;
        }
        feedbackRequestBean.setContent(content);
        feedbackRequestBean.setGameid(gameId);
        feedbackRequestBean.setGamecontent(reportListAdapter.getSelectTypeContent());
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

    @OnClick({R.id.iv_titleLeft, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.btn_submit:
                submitFeedBack();
                break;
        }
    }

    public static void start(Context context,String gameId) {
        Intent starter = new Intent(context, GameFeedbackActivity.class);
        starter.putExtra("gameId",gameId);
        context.startActivity(starter);
    }
}
