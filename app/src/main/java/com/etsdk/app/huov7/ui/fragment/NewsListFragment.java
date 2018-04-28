package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.NewsListAdapter;
import com.etsdk.app.huov7.base.AutoLazyFragment;
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

/**
 * 2017/5/11
 * 资讯列表fragment
 */

public class NewsListFragment extends AutoLazyFragment implements AdvRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;

    BaseRefreshLayout baseRefreshLayout;
    private String catalog = null;
    private String gameId = null;
    private NewsListAdapter adapter;

    /**
     *
     * @param catalog   默认为0 0-所有 1-新闻 2-活动 3-攻略 5-评测
     * @param gameId    对应游戏
     * @return
     */
    public static NewsListFragment newInstance(String catalog, String gameId) {
        NewsListFragment newFragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("catalog", catalog);
        bundle.putString("gameId", gameId);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_comm_list);
//        EventBus.getDefault().register(this);
        setupUI();
    }

    private void setupUI() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            catalog =arguments.getString("catalog",null);
            gameId =arguments.getString("gameId",null);
        }
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new RecyclerViewNoAnimator());
        adapter = new NewsListAdapter(null, catalog, getActivity());
        baseRefreshLayout.setAdapter(adapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.refresh();
    }

    @Override
    public void getPageData(final int requestPageNo) {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.newsList);
        httpParams.put("page",requestPageNo);
        httpParams.put("offset",20);
        if(catalog != null){
            httpParams.put("catalog",catalog);
        }
        if(gameId != null){
            httpParams.put("gameid",gameId);
        }
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.newsList),new HttpJsonCallBackDialog<NewsListBean>(){
            @Override
            public void onDataSuccess(NewsListBean data) {
                if(data!=null&&data.getData()!=null&&data.getData().getList()!=null){
                    int maxPage = (int)Math.ceil(data.getData().getCount() / 20.);
                    baseRefreshLayout.resultLoadData(adapter.getData(),data.getData().getList(),maxPage);
                }else{
                    baseRefreshLayout.resultLoadData(adapter.getData(),new ArrayList(),requestPageNo-1);
                }
            }
            @Override
            public void onJsonSuccess(int code, String msg,String data) {
                baseRefreshLayout.resultLoadData(adapter.getData(),new ArrayList(),null);
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                baseRefreshLayout.resultLoadData(adapter.getData(),null,null);
            }
        });
    }
}
