package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.GameFirstClassifyAdapter;
import com.etsdk.app.huov7.adapter.GameFirstClassifyAdapterV1;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.AdImage;
import com.etsdk.app.huov7.model.GameClassifyListModel;
import com.etsdk.app.huov7.model.GameClassifyListModelV1;
import com.etsdk.app.huov7.model.MainGameAd;
import com.etsdk.app.huov7.util.RecyclerViewNoAnimator;
import com.etsdk.app.huov7.view.LoadStatusView;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.log.T;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by liu hong liang on 2016/12/9.
 */

public class GameFirstClassifyFragmentV1 extends AutoLazyFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    GameFirstClassifyAdapterV1 firstClassifyAdapter;
    @BindView(R.id.loadview)
    LoadStatusView loadview;
    private List<GameClassifyListModelV1.GameClassify> datas = new ArrayList();

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_game_classify);
        setupUI();
    }

    private void setupUI() {
        //设置颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.bg_blue,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new RecyclerViewNoAnimator());
        firstClassifyAdapter = new GameFirstClassifyAdapterV1(datas);
        // 设置适配器
        recyclerView.setAdapter(firstClassifyAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNetData();
            }
        });
        loadview.showLoading();
        loadview.setOnLoadRefreshListener(new LoadStatusView.OnLoadRefreshListener() {
            @Override
            public void onLoadRefresh() {
                getNetData();
                getGametypetopperAd();
            }
        });
        getNetData();
        getGametypetopperAd();
    }

    private void getNetData() {
        //获取分类列表
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.gametypeApi);
        httpParams.put("page", 1);
        httpParams.put("offset", Integer.MAX_VALUE);//不分页
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.gametypeApi), new HttpJsonCallBackDialog<GameClassifyListModelV1>() {
            @Override
            public void onDataSuccess(GameClassifyListModelV1 data) {
                if (data != null && data.getData() != null && data.getData().getList() != null) {
                    firstClassifyAdapter.setDatas(data.getData().getList());
                    swipeRefreshLayout.setRefreshing(false);
                    loadview.showSuccess();
                }else{
                    firstClassifyAdapter.setDatas(new ArrayList<GameClassifyListModelV1.GameClassify>());
                    swipeRefreshLayout.setRefreshing(false);
                    loadview.showSuccess();
                }
            }
            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                Log.e(TAG, "数据解析失败");
                if(swipeRefreshLayout!=null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                if(datas.isEmpty()){
                    loadview.showFail();
                }else{
                    T.s(mContext, "服务器忙，请稍后再试");
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                Log.e(TAG, "加载失败:" + completionInfo);
                if(swipeRefreshLayout!=null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                if(datas.isEmpty()){
                    loadview.showFail();
                }else{
                    T.s(mContext, "连接失败，请稍后再试");
                }
            }
        });
    }
    private void getGametypetopperAd() {
        if(true) {
            return;//不要广告页
        }
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.slideListApi);
        httpParams.put("type", "gametypetopper");
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).setShowErrorToast(false).get(AppApi.getUrl(AppApi.slideListApi), new HttpJsonCallBackDialog<MainGameAd>() {
            @Override
            public void onDataSuccess(MainGameAd data) {
                if(data!=null&&data.getData()!=null
                        &&data.getData().getGametypetopper()!=null
                        &&data.getData().getGametypetopper().getList()!=null
                        &&data.getData().getGametypetopper().getList().size()>0){
                    AdImage adImage = data.getData().getGametypetopper().getList().get(0);
                    firstClassifyAdapter.setAdImage(adImage);
                }
            }
        });
    }



}
