package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.AdImage;
import com.etsdk.app.huov7.model.GameTestNewTab;
import com.etsdk.app.huov7.model.GameTestNewTime;
import com.etsdk.app.huov7.model.MainGameAd;
import com.etsdk.app.huov7.model.SplitLine;
import com.etsdk.app.huov7.model.StartServerGameBean;
import com.etsdk.app.huov7.model.StartServerGameList;
import com.etsdk.app.huov7.model.SwitchFragmentEvent;
import com.etsdk.app.huov7.model.TestGameBean;
import com.etsdk.app.huov7.model.TestGameList;
import com.etsdk.app.huov7.provider.AdImageViewProvider;
import com.etsdk.app.huov7.provider.GameTestNewTabViewProvider;
import com.etsdk.app.huov7.provider.GameTestNewTimeViewProvider;
import com.etsdk.app.huov7.provider.SplitLineViewProvider;
import com.etsdk.app.huov7.provider.StartServerGameItemViewProvider;
import com.etsdk.app.huov7.provider.TestServerGameItemViewProvider;
import com.etsdk.app.huov7.util.RecyclerViewNoAnimator;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by liu hong liang on 2017/1/18.
 *
 * 数据分发：
 *
 *
 *  page=1,拿到数据做数据分类处理，显示,结束刷新
 *  page=2，拿到数据和已有数据合并后做分类数据处理，显示，结束加载更多
 *
 */

public class GameTestNewFragment extends AutoLazyFragment implements AdvRefreshListener, GameTestNewTabViewProvider.OnTestNewTabSelectListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    BaseRefreshLayout baseRefreshLayout;
    private Items items=new Items();
    boolean isStartServerUI=true;
    private MultiTypeAdapter multiTypeAdapter;
    private Map<String,List<StartServerGameBean>> startServerMap=new TreeMap();//开服数据map
    private Map<String,List<TestGameBean>> testServerMap=new TreeMap();//开测数据map
    private AdImage adImage;
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_comm_list);
        EventBus.getDefault().register(this);
        setupUI();
    }

    private void setupUI() {
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new RecyclerViewNoAnimator());
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(SplitLine.class, new SplitLineViewProvider());
        multiTypeAdapter.register(AdImage.class, new AdImageViewProvider());
        multiTypeAdapter.register(TestGameBean.class, new TestServerGameItemViewProvider());
        multiTypeAdapter.register(StartServerGameBean.class, new StartServerGameItemViewProvider());
        multiTypeAdapter.register(GameTestNewTime.class,new GameTestNewTimeViewProvider(true));
        multiTypeAdapter.register(GameTestNewTab.class,new GameTestNewTabViewProvider(this));
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        SwitchFragmentEvent stickyEvent = EventBus.getDefault().getStickyEvent(SwitchFragmentEvent.class);
        if(stickyEvent!=null){
            onSwitchFragmentEvent(stickyEvent);
        }
        onSelectTab(true);//默认显示开服表
    }
    /**
     * 在fragment之前收到切换通知
     * @param switchFragmentEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSwitchFragmentEvent(SwitchFragmentEvent switchFragmentEvent){
        if(this.getActivity().getClass().getName().equals(switchFragmentEvent.activityClassName)){
            if(switchFragmentEvent.positions.length>2){
                onSelectTab(switchFragmentEvent.positions[2]==1);
                EventBus.getDefault().removeStickyEvent(switchFragmentEvent);
            }
        }
    }


    @Override
    public void getPageData(final int requestPageNo) {
        if(isStartServerUI){
            getStartServerData(requestPageNo);
        }else{
            getTestGameData(requestPageNo);
        }
        getGametestserverAd();
    }

    public void getStartServerData(final int requestPageNo){
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.gameListApi);
        httpParams.put("server",2);
        httpParams.put("page",requestPageNo);
        httpParams.put("offset",20);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.gameListApi),new HttpJsonCallBackDialog<StartServerGameList>(){
            @Override
            public void onDataSuccess(StartServerGameList data) {
                if(data!=null&&data.getData()!=null&&data.getData().getList()!=null){
                    int maxPage = (int)Math.ceil(data.getData().getCount() / 20.);
                    handleStartServerData(requestPageNo,data.getData().getList(),maxPage);
                }else{
                    handleStartServerData(requestPageNo,new ArrayList(),requestPageNo-1);
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
     * 处理开服数据
     * @param requestPageNo
     * @param list
     * @param maxPage
     */
    private void handleStartServerData(int requestPageNo, List<StartServerGameBean> list, int maxPage) {
        if(requestPageNo==1) startServerMap.clear();
        for(StartServerGameBean startServerGameBean:list){
            String startTime = startServerGameBean.getStarttime();
            Date date=new Date(Long.parseLong(startTime)*1000);
            String formatData = sdf.format(date);
            List<StartServerGameBean> dateList = startServerMap.get(formatData);
            if(dateList==null){
                dateList=new ArrayList();
                startServerMap.put(formatData,dateList);
            }
            dateList.add(startServerGameBean);
        }
        Items newItems=new Items();
        if(adImage!=null){
            newItems.add(new SplitLine());
            newItems.add(adImage);
        }
        newItems.add(new GameTestNewTab(true));
        for(String dateStr:startServerMap.keySet()){
            newItems.add(new GameTestNewTime(dateStr));
            newItems.addAll(startServerMap.get(dateStr));
        }
        items.clear();
        if(requestPageNo==1){
            baseRefreshLayout.onEndRefreshExecute(items,newItems,maxPage);
        }else {
            baseRefreshLayout.onEndLoadMoreExecute(items,newItems,maxPage);
        }
    }
    public void getTestGameData(final int requestPageNo){
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.gameListApi);
        httpParams.put("test",2);
        httpParams.put("page",requestPageNo);
        httpParams.put("offset",20);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.gameListApi),new HttpJsonCallBackDialog<TestGameList>(){
            @Override
            public void onDataSuccess(TestGameList data) {
                if(data!=null&&data.getData()!=null&&data.getData().getList()!=null){
                    int maxPage = (int)Math.ceil(data.getData().getCount() / 20.);
                    handleTestServerData(requestPageNo,data.getData().getList(),maxPage);
                }else{
                    handleTestServerData(requestPageNo,new ArrayList(),requestPageNo-1);
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
     * @param list
     * @param maxPage
     */
    private void handleTestServerData(int requestPageNo, List<TestGameBean> list, int maxPage) {
        if(requestPageNo==1) testServerMap.clear();
        for(TestGameBean testGameBean:list){
            String startTime = testGameBean.getStarttime();
            Date date=new Date(Long.parseLong(startTime)*1000);
            String formatData = sdf.format(date);
            List<TestGameBean> dateList = testServerMap.get(formatData);
            if(dateList==null){
                dateList=new ArrayList();
                testServerMap.put(formatData,dateList);
            }
            dateList.add(testGameBean);
        }
        Items newItems=new Items();
        if(adImage!=null){
            newItems.add(new SplitLine());
            newItems.add(adImage);
        }
        newItems.add(new GameTestNewTab(false));
        for(String dateStr:testServerMap.keySet()){
            newItems.add(new GameTestNewTime(dateStr));
            newItems.addAll(testServerMap.get(dateStr));
        }
        items.clear();
        if(requestPageNo==1){
            baseRefreshLayout.onEndRefreshExecute(items,newItems,maxPage);
        }else {
            baseRefreshLayout.onEndLoadMoreExecute(items,newItems,maxPage);
        }
    }

    /**
     * 切换选择tab
     * @param isStartServer
     */
    @Override
    public void onSelectTab(boolean isStartServer) {
        this.isStartServerUI=isStartServer;
        items.clear();
        Items newItems=new Items();
        if(adImage!=null){
            newItems.add(new SplitLine());
            newItems.add(adImage);
        }
        if(isStartServerUI){
            newItems.add(new GameTestNewTab(true));
            for(String dateStr:startServerMap.keySet()){
                newItems.add(new GameTestNewTime(dateStr));
                newItems.addAll(startServerMap.get(dateStr));
            }
        }else{
            newItems.add(new GameTestNewTab(false));
            for(String dateStr:testServerMap.keySet()){
                newItems.add(new GameTestNewTime(dateStr));
                newItems.addAll(testServerMap.get(dateStr));
            }
        }
        items.addAll(newItems);
        baseRefreshLayout.setPage(1);//重置页码为1
        multiTypeAdapter.notifyDataSetChanged();
        getPageData(1);
    }

    /**
     * 获取顶部图片
     */
    private void getGametestserverAd() {
        if(true){//不要广告
            return;
        }
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.slideListApi);
        httpParams.put("type", "gametestserver");
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).setShowErrorToast(false).get(AppApi.getUrl(AppApi.slideListApi), new HttpJsonCallBackDialog<MainGameAd>() {
            @Override
            public void onDataSuccess(MainGameAd data) {
                if(data!=null&&data.getData()!=null
                        &&data.getData().getGametestserver()!=null
                        &&data.getData().getGametestserver().getList()!=null
                        &&data.getData().getGametestserver().getList().size()>0){
                    adImage = data.getData().getGametestserver().getList().get(0);
                    adImage.setRequestPadding(false);
                    adImage.setRequestBottomMargin(false);
                    boolean isFind=false;
                    for(int i=0;i<items.size()&&i<3;i++){//找到顶部图替换掉
                        if(items.get(i) instanceof AdImage){
                            isFind=true;
                            items.remove(i);
                            items.add(i,adImage);
                            multiTypeAdapter.notifyDataSetChanged();
                        }
                    }
                    if(!isFind){//没有找到，添加一个顶部图
                        items.add(0,new SplitLine());
                        items.add(1,adImage);
                    }
                }
            }
        });
    }



}
