package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameTestNewTime;
import com.etsdk.app.huov7.model.TestGameBean;
import com.etsdk.app.huov7.model.TestGameList;
import com.etsdk.app.huov7.provider.GameTestNewTimeViewProvider;
import com.etsdk.app.huov7.provider.TestServerGameItemViewProvider;
import com.etsdk.app.huov7.util.RecyclerViewNoAnimator;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 开测
 * Created by Administrator on 2017/5/2 0002.
 */

public class GameTestFragment extends AutoLazyFragment implements AdvRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;

    BaseRefreshLayout baseRefreshLayout;
    private Items items=new Items();
    private MultiTypeAdapter multiTypeAdapter;
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
    private Map<String,List<TestGameBean>> testServerMap=new TreeMap();//开测数据map

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_comm_list);
        stetupUI();
    }

    private void stetupUI(){
        swrefresh.setColorSchemeResources(R.color.bg_blue,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new RecyclerViewNoAnimator());
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(TestGameBean.class, new TestServerGameItemViewProvider());
        multiTypeAdapter.register(GameTestNewTime.class,new GameTestNewTimeViewProvider(false));
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        getPageData(1);
    }

    @Override
    public void getPageData(final int requestPageNo) {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.gameListApi);
        httpParams.put("test",2);
        httpParams.put("page",requestPageNo);
        httpParams.put("offset",20);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.gameListApi),new HttpJsonCallBackDialog<TestGameList>(){
            @Override
            public void onDataSuccess(TestGameList data) {
                if(data!=null&&data.getData()!=null){
                    handleTestServerData(requestPageNo,data.getData(),1);
                }
            }
            @Override
            public void onJsonSuccess(int code, String msg,String data) {
                baseRefreshLayout.resultLoadData(items,null,null);
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                baseRefreshLayout.resultLoadData(items,null,null);
            }
        });
    }

    /**
     * 处理开测数据
     * @param requestPageNo
     * @param maxPage
     */
    private void handleTestServerData(int requestPageNo,TestGameList.DataBean data, int maxPage) {
        if(requestPageNo==1) testServerMap.clear();
        if(data.getFormerly()!=null&&data.getFormerly().size()>0){
            testServerMap.put("3",data.getFormerly());
        }
        if(data.getToday()!=null&&data.getToday().size()>0){
            testServerMap.put("1",data.getToday());
        }
        if(data.getWillbe()!=null&&data.getWillbe().size()>0){
            testServerMap.put("2",data.getWillbe());
        }
        Items newItems=new Items();
        for(String dateStr:testServerMap.keySet()){
            newItems.add(new GameTestNewTime(dateStr));
            newItems.addAll(testServerMap.get(dateStr));
        }
        items.clear();
        baseRefreshLayout.onEndRefreshExecute(items,newItems,maxPage);
    }
}
