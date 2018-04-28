package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.NewsListAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.NewsListBean;
import com.etsdk.app.huov7.util.RecyclerViewNoAnimator;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/23.
 */

public class EventActivity extends ImmerseActivity implements AdvRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;

    BaseRefreshLayout baseRefreshLayout;
    private String catalog = null;
    private String gameId = null;
    private NewsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("最新活动");
        catalog = getIntent().getStringExtra("catalog");
        gameId = getIntent().getStringExtra("gameId");
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new RecyclerViewNoAnimator());
        adapter = new NewsListAdapter(null, catalog, EventActivity.this);
        baseRefreshLayout.setAdapter(adapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.refresh();
    }

    @Override
    public void getPageData(final int requestPageNo) {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.newsList);
        httpParams.put("page", requestPageNo);
        httpParams.put("offset", 20);
        if (catalog != null) {
            httpParams.put("catalog", catalog);
        }
        if (gameId != null) {
            httpParams.put("gameid", gameId);
        }
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.newsList), new HttpJsonCallBackDialog<NewsListBean>() {
            @Override
            public void onDataSuccess(NewsListBean data) {
                if (data != null && data.getData() != null && data.getData().getList() != null) {
                    int maxPage = (int) Math.ceil(data.getData().getCount() / 20.);
                    baseRefreshLayout.resultLoadData(adapter.getData(), data.getData().getList(), maxPage);
                } else {
                    baseRefreshLayout.resultLoadData(adapter.getData(), new ArrayList(), requestPageNo - 1);
                }
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                baseRefreshLayout.resultLoadData(adapter.getData(), new ArrayList(), null);
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                baseRefreshLayout.resultLoadData(adapter.getData(), null, null);
            }
        });
    }

    @OnClick({R.id.iv_titleLeft})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
        }
    }

    /**
     * @param catalog 默认为0 0-所有 1-新闻 2-活动 3-攻略 5-评测
     * @param gameId  对应游戏
     * @return
     */
    public static void start(Context context, String catalog, String gameId) {
        Intent starter = new Intent(context, EventActivity.class);
        starter.putExtra("catalog", catalog);
        starter.putExtra("gameId", gameId);
        context.startActivity(starter);
    }
}
