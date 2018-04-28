package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameTestNewTime;
import com.etsdk.app.huov7.model.StartServerGameBean;
import com.etsdk.app.huov7.model.StartServerGameList;
import com.etsdk.app.huov7.provider.GameTestNewTimeViewProvider;
import com.etsdk.app.huov7.provider.StartServerGameItemViewProvider;
import com.etsdk.app.huov7.util.RecyclerViewNoAnimator;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 开服
 * Created by Administrator on 2017/5/2 0002.
 */

public class GameStartFragment extends AutoLazyFragment implements AdvRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;

    BaseRefreshLayout baseRefreshLayout;
    private Items items = new Items();
    private MultiTypeAdapter multiTypeAdapter;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
    private Map<String, List<StartServerGameBean>> startServerMap = new TreeMap();//开服数据map

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_comm_list);
        stetupUI();
    }

    private void stetupUI() {
        swrefresh.setColorSchemeResources(R.color.bg_blue,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new RecyclerViewNoAnimator());
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(StartServerGameBean.class, new StartServerGameItemViewProvider());
        multiTypeAdapter.register(GameTestNewTime.class, new GameTestNewTimeViewProvider(true));
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        getPageData(1);
    }

    @Override
    public void getPageData(final int requestPageNo) {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.gameListApi);
        httpParams.put("server", 2);
        httpParams.put("page", requestPageNo);
        httpParams.put("offset", 20);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.gameListApi), new HttpJsonCallBackDialog<StartServerGameList>() {
            @Override
            public void onDataSuccess(StartServerGameList data) {
                if(data!=null&&data.getData()!=null){
                    handleStartServerData(requestPageNo,data.getData(),1);
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

    /**
     * 处理开服数据
     *
     * @param requestPageNo
     * @param maxPage
     */
    private void handleStartServerData(int requestPageNo, StartServerGameList.DataBean data, int maxPage) {
        if (requestPageNo == 1) startServerMap.clear();
        if(data.getFormerly()!=null&&data.getFormerly().size()>0){
            startServerMap.put("3",data.getFormerly());
        }
        if(data.getToday()!=null&&data.getToday().size()>0){
            startServerMap.put("1",data.getToday());
        }
        if(data.getWillbe()!=null&&data.getWillbe().size()>0){
            startServerMap.put("2",data.getWillbe());
        }
        Items newItems = new Items();
        for (String dateStr : startServerMap.keySet()) {
            newItems.add(new GameTestNewTime(dateStr));
            newItems.addAll(startServerMap.get(dateStr));
        }
        items.clear();
        baseRefreshLayout.onEndRefreshExecute(items, newItems, maxPage);
    }

    public String getDateShowStr(Date date) {
        String dateStr = sdf.format(date);
        Calendar mingtianCalendar = Calendar.getInstance();
        mingtianCalendar.add(Calendar.DATE, 1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (sdf.format(new Date()).equals(dateStr)) {//等于今天
            dateStr = "1";

        } else if (sdf.format(mingtianCalendar.getTime()).equals(dateStr)) {//明天
            dateStr = "2";
        } else {
            dateStr = "3";
        }
        return dateStr;
    }

}
