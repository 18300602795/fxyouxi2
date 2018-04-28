package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.GameBeanList;
import com.etsdk.app.huov7.model.SplitLine;
import com.etsdk.app.huov7.model.SwitchFragmentEvent;
import com.etsdk.app.huov7.provider.GameItemViewProvider;
import com.etsdk.app.huov7.provider.SplitLineViewProvider;
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

import java.util.ArrayList;

import butterknife.BindView;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by liu hong liang on 2016/12/10.
 * 游戏列表fragment
 */

public class GameListFragment extends AutoLazyFragment implements AdvRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    BaseRefreshLayout baseRefreshLayout;
    private boolean requestTopSplit = false;//是否需要顶部分割线
    private boolean showRank = false;
    private Items items=new Items();
    private int isnew;//	否	INT	是否新游 2 新游 1 普通 0 所有 20170113新增
    private int remd;//	否	INT	是否新游 2 推荐 1 普通 0 所有 20170113新增
    private int server;//	否	INT	是否新游 2 开服游戏 1 普通 0 所有 20170113新增
    private int test;//	否	INT	是否新游 2 开测游戏 1 普通 0 所有 20170113新增
    private int hot;
    private int category;   //INT	是否单机 2 网游 1 单机 0 所有 20170113新增
    private String type;//分类id

    /**
     * 通用的游戏列表fragment
     * @param requestTopSplit
     * @param showRank
     * @return
     */
    public static GameListFragment newInstance(boolean requestTopSplit, boolean showRank,int hot,int isnew,int remd,int server,int test,int category,String typeId) {
        GameListFragment newFragment = new GameListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("requestTopSplit", requestTopSplit);
        bundle.putBoolean("showRank", showRank);
        bundle.putInt("hot", hot);
        bundle.putInt("isnew", isnew);
        bundle.putInt("remd", remd);
        bundle.putInt("server", server);
        bundle.putInt("test", test);
        bundle.putInt("category", category);
        bundle.putString("typeId", typeId);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_game_list);
        EventBus.getDefault().register(this);
        setupUI();
    }

    private void setupUI() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            requestTopSplit = arguments.getBoolean("requestTopSplit");
            showRank = arguments.getBoolean("showRank");
            hot =arguments.getInt("hot");
            isnew=arguments.getInt("isnew");
            remd=arguments.getInt("remd");
            server=arguments.getInt("server");
            test=arguments.getInt("test");
            category=arguments.getInt("category");
            type =arguments.getString("typeId",null);
        }
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        recyclerView.addItemDecoration(new RecommandOptionRcyDivider(getContext(), LinearLayoutManager.HORIZONTAL, 10, getResources().getColor(R.color.aile_line_gray)));
        recyclerView.setItemAnimator(new RecyclerViewNoAnimator());
        MultiTypeAdapter multiTypeAdapter=new MultiTypeAdapter(items);
        multiTypeAdapter.register(SplitLine.class, new SplitLineViewProvider());
        multiTypeAdapter.register(GameBean.class, new GameItemViewProvider(showRank));
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.refresh();
    }
    /**
     * 在fragment之前收到切换通知
     *
     * @param switchFragmentEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1)
    public void onSwitchFragmentEvent(SwitchFragmentEvent switchFragmentEvent) {
//        if(this.getActivity().getClass().getName().equals(switchFragmentEvent.activityClassName)){
//            if(switchFragmentEvent.positions.length>1){
//                switchFragment(switchFragmentEvent.positions[1]);
//                if(switchFragmentEvent.positions.length==2){
//                    EventBus.getDefault().cancelEventDelivery(switchFragmentEvent) ;
//                    EventBus.getDefault().removeStickyEvent(switchFragmentEvent);
//                }
//            }
//        }
    }


    @Override
    public void getPageData(final int requestPageNo) {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.gameListApi);
        httpParams.put("hot",hot);
        httpParams.put("isnew",isnew);
        httpParams.put("remd",remd);
        httpParams.put("server",server);
        httpParams.put("test",test);
        httpParams.put("category",category);
        if(type!=null){
            httpParams.put("type",type);
        }
        httpParams.put("page",requestPageNo);
        httpParams.put("offset",20);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.gameListApi),new HttpJsonCallBackDialog<GameBeanList>(){
            @Override
            public void onDataSuccess(GameBeanList data) {
                if(data!=null&&data.getData()!=null&&data.getData().getList()!=null){
                    int maxPage = (int)Math.ceil(data.getData().getCount() / 20.);
                    if(isnew==1||hot==1){//热门和新游只要20个
                        maxPage=1;
                    }
                    Items resultItems=new Items();
                    if(requestTopSplit&&requestPageNo==1){//新游热门第一页第一个顶部有分割线
                        resultItems.add(new SplitLine());
                    }
                    resultItems.addAll(data.getData().getList());
                    baseRefreshLayout.resultLoadData(items,resultItems,maxPage);
                }else{
                    baseRefreshLayout.resultLoadData(items,new ArrayList(),requestPageNo-1);
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


    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        EventBus.getDefault().unregister(this);
    }
}
