package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.GameBeanList;
import com.etsdk.app.huov7.model.SplitLine;
import com.etsdk.app.huov7.provider.GameItemViewProvider;
import com.etsdk.app.huov7.provider.SplitLineViewProvider;
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

public class GameListActivity extends ImmerseActivity implements AdvRefreshListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    BaseRefreshLayout baseRefreshLayout;
    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    private Items items=new Items();
    private MultiTypeAdapter multiTypeAdapter;
    private int isnew;//	否	INT	是否新游 2 新游 1 普通 0 所有 20170113新增
    private int remd;//	否	INT	是否新游 2 推荐 1 普通 0 所有 20170113新增
    private int server;//	否	INT	是否新游 2 开服游戏 1 普通 0 所有 20170113新增
    private int test;//	否	INT	是否新游 2 开测游戏 1 普通 0 所有 20170113新增
    private int category;  //INT	是否单机 2 网游 1 单机 0 所有 20170113新增
    private int hot;
    private String type = null;//分类id
    private int welfare;
    private boolean requestTopSplit = false;//是否需要顶部分割线
    private boolean showRank = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        ButterKnife.bind(this);
        setupUI();
    }
    private void setupUI() {
        tvTitleName.setText("游戏列表");
        Intent intent = getIntent();
        if (intent != null) {
            String title =intent.getStringExtra("title");
            if (!TextUtils.isEmpty(title)) {
                tvTitleName.setText(title);
            }
            requestTopSplit = intent.getBooleanExtra("requestTopSplit", false);
            showRank = intent.getBooleanExtra("showRank", false);
            hot =intent.getIntExtra("hot",0);
            isnew =intent.getIntExtra("isnew",0);
            remd =intent.getIntExtra("remd",0);
            server =intent.getIntExtra("server",0);
            test =intent.getIntExtra("test",0);
            welfare =intent.getIntExtra("welfare",0);
            type =intent.getStringExtra("typeId");
            category = intent.getIntExtra("category", 0);
        }

        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        // 设置适配器
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(SplitLine.class, new SplitLineViewProvider());
        multiTypeAdapter.register(GameBean.class, new GameItemViewProvider(showRank));
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.refresh();
    }
    @Override
    public void getPageData(final int requestPageNo) {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.gameListApi);
        httpParams.put("hot", hot);
        httpParams.put("isnew", isnew);
        httpParams.put("remd", remd);
        httpParams.put("server", server);
        httpParams.put("test", test);
        httpParams.put("category", category);
        httpParams.put("welfare",welfare);
        if (type != null) {
            httpParams.put("type", type);
        }
        httpParams.put("page", requestPageNo);
        httpParams.put("offset", 20);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.gameListApi), new HttpJsonCallBackDialog<GameBeanList>() {
            @Override
            public void onDataSuccess(GameBeanList data) {
                if (data != null && data.getData() != null && data.getData().getList() != null) {
                    int maxPage = (int) Math.ceil(data.getData().getCount() / 20.);
                    if (isnew == 1 || hot == 1) {//热门和新游只要20个
                        maxPage = 1;
                    }
                    Items resultItems = new Items();
                    if (requestTopSplit && requestPageNo == 1) {//新游热门第一页第一个顶部有分割线
                        resultItems.add(new SplitLine());
                    }
                    resultItems.addAll(data.getData().getList());
                    baseRefreshLayout.resultLoadData(items, resultItems, maxPage);
                } else {
                    baseRefreshLayout.resultLoadData(items, new ArrayList(), requestPageNo - 1);
                }
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                baseRefreshLayout.resultLoadData(items, null, null);
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                baseRefreshLayout.resultLoadData(items, null, null);
            }
        });
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, GameListActivity.class);
        context.startActivity(starter);
    }

    public static void start(Context context, String title, boolean requestTopSplit, boolean showRank, int hot, int isnew, int remd, int server, int test, int welfare, int category, String typeId) {
        Intent starter = new Intent(context, GameListActivity.class);
        starter.putExtra("title",title);
        starter.putExtra("requestTopSplit", requestTopSplit);
        starter.putExtra("showRank", showRank);
        starter.putExtra("hot",hot);
        starter.putExtra("isnew",isnew);
        starter.putExtra("remd",remd);
        starter.putExtra("server",server);
        starter.putExtra("test",test);
        starter.putExtra("welfare",welfare);
        starter.putExtra("category",category);
        starter.putExtra("typeId",typeId);
        context.startActivity(starter);
    }

    @OnClick({R.id.iv_titleLeft,R.id.tv_titleRight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
        }
    }

}
