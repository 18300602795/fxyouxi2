package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameItemPay;
import com.etsdk.app.huov7.model.GamePayBeanList;
import com.etsdk.app.huov7.provider.GameItemPayViewProvider;
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
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class SelectGamePayActivity extends ImmerseActivity implements AdvRefreshListener {
    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.et_game_name)
    EditText etGameName;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    @BindView(R.id.activity_select_game_pay)
    LinearLayout activitySelectGamePay;
    @BindView(R.id.recyclerView_recGame)
    RecyclerView recGameRecyclerView;
    @BindView(R.id.swrefresh_recGame)
    SwipeRefreshLayout swrefreshRecGame;
    private Items recGameItems = new Items();
    private Items searchGameItems = new Items();
    private MultiTypeAdapter multiTypeAdapter;
    private BaseRefreshLayout baseRefreshLayout;
    private MultiTypeAdapter recGameAdapter;
    private BaseRefreshLayout recGameRefreshLayout;
    private String searchKey="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_game_pay);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("选择游戏");
        //搜索
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        multiTypeAdapter = new MultiTypeAdapter(searchGameItems);
        multiTypeAdapter.register(GameItemPay.class, new GameItemPayViewProvider());
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
//        baseRefreshLayout.refresh();

        //推荐
        recGameRefreshLayout = new MVCSwipeRefreshHelper(swrefreshRecGame);
        recGameRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recGameAdapter = new MultiTypeAdapter(recGameItems);
        recGameAdapter.register(GameItemPay.class, new GameItemPayViewProvider());
        recGameRefreshLayout.setAdapter(recGameAdapter);
        recGameRefreshLayout.setAdvRefreshListener(recGameRefreshListener);
        recGameRefreshLayout.refresh();
        etGameName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    swrefreshRecGame.setVisibility(View.VISIBLE);//显示推荐
                    swrefresh.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight, R.id.btn_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
            case R.id.btn_search:
                searchKey = etGameName.getText().toString().toString();
                if(!TextUtils.isEmpty(searchKey)){
                    baseRefreshLayout.refresh();
                    swrefresh.setVisibility(View.VISIBLE);
                    swrefreshRecGame.setVisibility(View.GONE);
                }
                break;
        }
    }
    //推荐加载监听
    AdvRefreshListener recGameRefreshListener=new AdvRefreshListener() {
        @Override
        public void getPageData(final int requestPageNo) {
            HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.gameListApi);
            httpParams.put("page", requestPageNo);
            httpParams.put("offset", 20);
            //成功，失败，null数据
            NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.gameListApi), new HttpJsonCallBackDialog<GamePayBeanList>() {
                @Override
                public void onDataSuccess(GamePayBeanList data) {
                    if (data != null && data.getData() != null && data.getData().getList() != null) {
                        int maxPage = (int) Math.ceil(data.getData().getCount() / 20.);
                        recGameRefreshLayout.resultLoadData(recGameItems, data.getData().getList(), maxPage);
                    } else {
                        recGameRefreshLayout.resultLoadData(recGameItems, new ArrayList(), requestPageNo - 1);
                    }
                }

                @Override
                public void onJsonSuccess(int code, String msg, String data) {
                    recGameRefreshLayout.resultLoadData(recGameItems, null, null);
                }

                @Override
                public void onFailure(int errorNo, String strMsg, String completionInfo) {
                    recGameRefreshLayout.resultLoadData(recGameItems, null, null);
                }
            });
        }
    };
    public static void start(Context context) {
        Intent starter = new Intent(context, SelectGamePayActivity.class);
        context.startActivity(starter);
    }
    public static Intent getIntent(Context context) {
        Intent starter = new Intent(context, SelectGamePayActivity.class);
        return starter;
    }

    @Override
    public void getPageData(final int requestPageNo) {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.searchIndexApi);
        httpParams.put("searchtype", "game");
        httpParams.put("q", searchKey);
        httpParams.put("page", requestPageNo);
        httpParams.put("offset", 20);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.searchIndexApi), new HttpJsonCallBackDialog<GamePayBeanList>() {
            @Override
            public void onDataSuccess(GamePayBeanList data) {
                if (data != null && data.getData() != null && data.getData().getList() != null) {
                    int maxPage = (int) Math.ceil(data.getData().getCount() / 20.);
                    baseRefreshLayout.resultLoadData(searchGameItems, data.getData().getList(), maxPage);
                } else {
                    baseRefreshLayout.resultLoadData(searchGameItems, new ArrayList(), requestPageNo - 1);
                }
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                baseRefreshLayout.resultLoadData(searchGameItems, null, null);
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                baseRefreshLayout.resultLoadData(searchGameItems, null, null);
            }
        });
    }
}
