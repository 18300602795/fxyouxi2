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
import com.etsdk.app.huov7.model.CouponBeanList;
import com.etsdk.app.huov7.model.CouponListItem;
import com.etsdk.app.huov7.provider.CouponListItemViewProvider;
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
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class CouponListActivity extends ImmerseActivity implements AdvRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    private Items items=new Items();
    BaseRefreshLayout baseRefreshLayout;
    private String gameId;
    private int limittime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_list);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setupUI();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(Boolean isLogin) {
        if (isLogin){
            baseRefreshLayout.refresh();
        }else {
            finish();
        }
    }




    private void setupUI() {

        tvTitleName.setText("代金券列表");
        if(getIntent()!=null){
            String title = getIntent().getStringExtra("title");
            gameId = getIntent().getStringExtra("gameId");
            limittime = getIntent().getIntExtra("limittime",0);
            if(!TextUtils.isEmpty(title)){
                tvTitleName.setText(title);
            }
        }
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        MultiTypeAdapter multiTypeAdapter=new MultiTypeAdapter(items);
        multiTypeAdapter.register(CouponListItem.class,new CouponListItemViewProvider());
        // 设置适配器
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.refresh();
    }

    private Items getTestData(){
        Items items=new Items();
        for(int i=0;i<20;i++){
            items.add(new CouponListItem());
        }
        return items;
    }
    @Override
    public void getPageData(final int requestPageNo) {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.couponListApi);

        if(gameId!=null){
            httpParams.put("gameid",gameId);
        }
        if(limittime!=0){
            httpParams.put("limittime", limittime);
        }
        httpParams.put("page",requestPageNo);
        httpParams.put("offset",20);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.couponListApi),new HttpJsonCallBackDialog<CouponBeanList>(){
            @Override
            public void onDataSuccess(CouponBeanList data) {
                if(data!=null&&data.getData()!=null&&data.getData().getList()!=null){
                    int maxPage = (int)Math.ceil(data.getData().getCount() / 20.);
                    baseRefreshLayout.resultLoadData(items,data.getData().getList(),maxPage);
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

    public static void start(Context context,String title,String gameId,int limittime) {
        Intent starter = new Intent(context, CouponListActivity.class);
        starter.putExtra("title",title);
        starter.putExtra("gameId",gameId);
        starter.putExtra("limittime",limittime);
        context.startActivity(starter);
    }
}
