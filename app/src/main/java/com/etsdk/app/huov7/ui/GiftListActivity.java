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
import com.etsdk.app.huov7.model.GiftBeanList;
import com.etsdk.app.huov7.model.GiftListItem;
import com.etsdk.app.huov7.provider.GiftListItemViewProvider;
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


public class GiftListActivity extends ImmerseActivity implements AdvRefreshListener {
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
    String gameId;
    int hot;
    int isnew;
    int remd;
    int luxury;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_list);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {

        tvTitleName.setText("礼包列表");
        if (getIntent() != null) {
            Intent intent = getIntent();
            String title = intent.getStringExtra("title");
            gameId = intent.getStringExtra("gameId");
            hot=intent.getIntExtra("hot",0);
            isnew=intent.getIntExtra("isnew",0);
            remd=intent.getIntExtra("remd",0);
            luxury=intent.getIntExtra("luxury",0);
            if(!TextUtils.isEmpty(title)){
                tvTitleName.setText(title);
            }
        }
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        MultiTypeAdapter multiTypeAdapter=new MultiTypeAdapter(items);
        multiTypeAdapter.register(GiftListItem.class,new GiftListItemViewProvider(multiTypeAdapter));
        // 设置适配器
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.refresh();
    }

    private Items getTestData(){
        Items items=new Items();
        for(int i=0;i<20;i++){
            items.add(new GiftListItem());
        }
        return items;
    }
    @Override
    public void getPageData(final int requestPageNo) {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.giftListApi);
        httpParams.put("hot",hot);
        httpParams.put("isnew",isnew);
        httpParams.put("remd",remd);
        httpParams.put("luxury",luxury);
        if(gameId!=null){
            httpParams.put("gameid",gameId);
        }
        httpParams.put("page",requestPageNo);
        httpParams.put("offset",20);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.giftListApi),new HttpJsonCallBackDialog<GiftBeanList>(){
            @Override
            public void onDataSuccess(GiftBeanList data) {
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

    @OnClick({R.id.iv_titleLeft})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
        }
    }
//    gameid	否	INT	对应游戏
//    hot	否	INT	是否热门 2 热门 1 普通 0 所有
//    new	否	INT	是否最新 2 新游 1 普通 0 所有`
//    remd	否	INT	是否推荐 2 推荐 1 普通 0 所有
//    luxury	否	INT	是否豪华 2 豪华礼包 1 普通 0 所有
    public static void start(Context context, String title,String gameId,int hot,int isnew,int remd,int luxury ) {
        Intent starter = new Intent(context, GiftListActivity.class);
        starter.putExtra("title", title);
        starter.putExtra("gameId", gameId);
        starter.putExtra("hot", hot);
        starter.putExtra("isnew", isnew);
        starter.putExtra("remd", remd);
        starter.putExtra("luxury", luxury);
        context.startActivity(starter);
    }
}
