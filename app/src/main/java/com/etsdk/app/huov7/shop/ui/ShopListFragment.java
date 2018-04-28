package com.etsdk.app.huov7.shop.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.shop.adapter.ShopListAdapter;
import com.etsdk.app.huov7.shop.model.CommPageRequstBean;
import com.etsdk.app.huov7.shop.model.ShopListBean;
import com.etsdk.app.huov7.shop.model.ShopListRefreshEvent;
import com.etsdk.app.huov7.util.RecyclerViewNoAnimator;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.L;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 2017/6/09
 * 交易商城列表fragment
 */

public class ShopListFragment extends AutoLazyFragment implements AdvRefreshListener {
    public static final int TYPE_ALL = 0;//全部
    public static final int TYPE_MY_SELL = 1;//我出售的
    public static final int TYPE_MY_FAVOR = 2;//我的收藏
    public static final int TYPE_MY_BOUGHT = 3;//我购买的

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;

    BaseRefreshLayout baseRefreshLayout;
    private int type = 0;
    private ShopListAdapter adapter;

    public static ShopListFragment newInstance(int type) {
        ShopListFragment newFragment = new ShopListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_comm_list);
        setupUI();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        if(adapter!=null){
            adapter.release();
        }
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateList(ShopListRefreshEvent event){
        if(baseRefreshLayout!=null) {
            baseRefreshLayout.refresh();
        }
    }

    private void setupUI() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            type =arguments.getInt("type",0);
        }
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new RecyclerViewNoAnimator());
        adapter = new ShopListAdapter(baseRefreshLayout, null, type);
        baseRefreshLayout.setAdapter(adapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.refresh();

//        //测试条目
//        ArrayList<ShopListBean.DataBean.ListBean> list = new ArrayList();
//        list.add(new ShopListBean.DataBean.ListBean());
//        list.add(new ShopListBean.DataBean.ListBean());
//        list.add(new ShopListBean.DataBean.ListBean());
//        baseRefreshLayout.resultLoadData(adapter.getData(),list,1);
    }

    @Override
    public void getPageData(final int requestPageNo) {
        String url = "";
        if(type == TYPE_ALL){//全部，普通get请求
            url = AppApi.dealAccountList;
            HttpParams httpParams = AppApi.getCommonHttpParams(url);
            httpParams.put("page",requestPageNo);
            httpParams.put("offset",10);
            NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(url),new HttpJsonCallBackDialog<ShopListBean>(){
                @Override
                public void onDataSuccess(ShopListBean data) {
                    if(data!=null&&data.getData()!=null&&data.getData().getList()!=null){
                        int maxPage = (int)Math.ceil(data.getData().getCount() / 10.);
                        baseRefreshLayout.resultLoadData(adapter.getData(),data.getData().getList(),maxPage);
                    }else{
                        baseRefreshLayout.resultLoadData(adapter.getData(), new ArrayList(), requestPageNo - 1);
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
        }else{//我的，内部方法方式
            if(type == TYPE_MY_SELL){//卖号
                url = AppApi.dealAccountSellList;
            }else if (type == TYPE_MY_BOUGHT){//买号
                url = AppApi.dealAccountBuyList;
            }else{//收藏
                url = AppApi.dealAccountCollectList;
            }
            final CommPageRequstBean requestBean =new CommPageRequstBean();
            requestBean.setPage(requestPageNo);
            requestBean.setOffset(10);
            HttpParamsBuild httpParamsBuild=new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
            HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<ShopListBean.DataBean>(getContext(), httpParamsBuild.getAuthkey()) {
                @Override
                public void onDataSuccess(final ShopListBean.DataBean data) {
                }

                @Override
                public void onDataSuccess(ShopListBean.DataBean data, String code, String msg) {
                    super.onDataSuccess(data, code, msg);
                    if(data!=null&&data.getList().size()>0){
                        int maxPage = (int)Math.ceil(data.getCount() / 10.);
                        baseRefreshLayout.resultLoadData(adapter.getData(),data.getList(),maxPage);
                    }else{
                        baseRefreshLayout.resultLoadData(adapter.getData(), new ArrayList(), requestPageNo - 1);
                    }
                }

                @Override
                public void onFailure(String code, String msg) {
                    L.e(TAG, code+" "+msg);
                    baseRefreshLayout.resultLoadData(adapter.getData(),null,null);
                }
            };
            httpCallbackDecode.setShowTs(true);
            httpCallbackDecode.setLoadingCancel(false);
            httpCallbackDecode.setShowLoading(false);
            RxVolley.post(AppApi.getUrl(url), httpParamsBuild.getHttpParams(),httpCallbackDecode);
        }
    }
}
