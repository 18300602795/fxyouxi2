package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.ScoreRankAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.ScoreRankListBean;
import com.etsdk.app.huov7.util.RecommandOptionRcyDivider;
import com.etsdk.app.huov7.util.RecyclerViewNoAnimator;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScoreRankActivity extends ImmerseActivity implements AdvRefreshListener {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.btn_exchange)
    Button btnExchange;
    ScoreRankAdapter scoreRankAdapter;
    BaseRefreshLayout baseRefreshLayout;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    @BindView(R.id.tv_my_score)
    TextView tvMyScore;
    @BindView(R.id.tv_my_rank)
    TextView tvMyRank;
    @BindView(R.id.activity_score_rank)
    LinearLayout activityScoreRank;
    private List<ScoreRankListBean.ScoreRankBean> scoreRankBeanList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_rank);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("积分排行");
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new RecyclerViewNoAnimator());
        recyclerView.addItemDecoration(new RecommandOptionRcyDivider(mContext, LinearLayoutManager.HORIZONTAL));
        scoreRankAdapter = new ScoreRankAdapter(scoreRankBeanList);
        // 设置适配器
        baseRefreshLayout.setAdapter(scoreRankAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.refresh();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ScoreRankActivity.class);
        context.startActivity(starter);
    }

    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight, R.id.btn_exchange})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
            case R.id.btn_exchange:
                ScoreShopActivity.start(mContext);
                break;
        }
    }

    @Override
    public void getPageData(final int requestPageNo) {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.ranklistApi);
        httpParams.put("page", requestPageNo);
        httpParams.put("offset", 20);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.ranklistApi), new HttpJsonCallBackDialog<ScoreRankListBean>() {
            @Override
            public void onDataSuccess(ScoreRankListBean data) {
                if (data != null && data.getData() != null && data.getData().getList() != null) {
                    int maxPage = (int) Math.ceil(data.getData().getCount() / 20.);
                    baseRefreshLayout.resultLoadData(scoreRankBeanList, data.getData().getList(), maxPage);
                } else {
                    baseRefreshLayout.resultLoadData(scoreRankBeanList, new ArrayList(), requestPageNo - 1);
                }
                if(data!=null&&data.getData()!=null){
                    tvMyRank.setText(data.getData().getMyrank());
                    tvMyScore.setText(data.getData().getMyintegral());
                }

            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                baseRefreshLayout.resultLoadData(scoreRankBeanList, null, null);
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                baseRefreshLayout.resultLoadData(scoreRankBeanList, null, null);
            }
        });
    }
}
