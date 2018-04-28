package com.etsdk.app.huov7.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameGiftBeanList;
import com.etsdk.app.huov7.model.GameGiftItem;
import com.etsdk.app.huov7.model.SplitLine;
import com.etsdk.app.huov7.provider.SearchGameGiftViewProvider;
import com.etsdk.app.huov7.provider.SplitLineViewProvider;
import com.etsdk.app.huov7.util.RecyclerViewNoAnimator;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by Administrator on 2017/8/8.
 */

public class GameGiftSearchView extends FrameLayout implements AdvRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    private MVCSwipeRefreshHelper baseRefreshLayout;
    private Items items = new Items();
    private String searchKey;

    public GameGiftSearchView(@NonNull Context context) {
        super(context);
        init();
    }

    public GameGiftSearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameGiftSearchView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_game_gift_search, this);
        ButterKnife.bind(this);
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new RecyclerViewNoAnimator());
        MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(SplitLine.class, new SplitLineViewProvider());
        multiTypeAdapter.register(GameGiftItem.class, new SearchGameGiftViewProvider());
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
    }

    public void search(String searchKey) {
        this.searchKey=searchKey;
        baseRefreshLayout.refresh();
    }
    @Override
    public void getPageData(final int requestPageNo) {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.searchIndexApi);
        httpParams.put("searchtype", "game");
        httpParams.put("q", searchKey);
        httpParams.put("page", requestPageNo);
        httpParams.put("offset", 20);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.searchIndexApi), new HttpJsonCallBackDialog<GameGiftBeanList>() {
            @Override
            public void onDataSuccess(GameGiftBeanList data) {
                if (data != null && data.getData() != null && data.getData().getList() != null) {
                    int maxPage = (int) Math.ceil(data.getData().getCount() / 20.);
                    baseRefreshLayout.resultLoadData(items, data.getData().getList(), maxPage);
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
}
