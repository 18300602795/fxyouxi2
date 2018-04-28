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
import com.etsdk.app.huov7.model.GiftCard;
import com.etsdk.app.huov7.model.GiftCardBeanList;
import com.etsdk.app.huov7.provider.GiftCardViewProvider;
import com.etsdk.app.huov7.provider.GoodsListItemViewProvider;
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

public class GiftCardListActivity extends ImmerseActivity implements AdvRefreshListener {
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
    private int is_real;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_card_list);
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
        tvTitleName.setText("礼品卡列表");
        if(getIntent()!=null){
            String title = getIntent().getStringExtra("title");
            is_real = getIntent().getIntExtra("is_real",1);
            if(!TextUtils.isEmpty(title)){
                tvTitleName.setText(title);
            }
        }
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        MultiTypeAdapter multiTypeAdapter=new MultiTypeAdapter(items);
        if(is_real==1){
            multiTypeAdapter.register(GiftCard.class,new GiftCardViewProvider());
        }else{
            multiTypeAdapter.register(GiftCard.class,new GoodsListItemViewProvider());
        }
        // 设置适配器
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.refresh();
    }


    @Override
    public void getPageData(final int requestPageNo) {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.goodsListApi);
        httpParams.put("is_real",is_real);
        httpParams.put("page",requestPageNo);
        httpParams.put("offset",20);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.goodsListApi),new HttpJsonCallBackDialog<GiftCardBeanList>(){
            @Override
            public void onDataSuccess(GiftCardBeanList data) {
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

    public static void start(Context context) {
        Intent starter = new Intent(context, GiftCardListActivity.class);
        context.startActivity(starter);
    }
    public static void start(Context context,String title,int is_real ) {
        Intent starter = new Intent(context, GiftCardListActivity.class);
        starter.putExtra("title",title);
        starter.putExtra("is_real",is_real);
        context.startActivity(starter);
    }
}
