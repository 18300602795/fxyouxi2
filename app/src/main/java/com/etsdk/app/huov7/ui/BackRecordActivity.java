package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.BackRecordAadapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.BackRecordList;
import com.etsdk.app.huov7.model.ChargeRrcordListRequestBean;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.L;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.views.recyclerview.swipe.RecycleViewDivider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/29.
 */

public class BackRecordActivity extends ImmerseActivity implements AdvRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;

    @BindView(R.id.tv_titleRight)
    TextView ivTitleRight;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    BackRecordAadapter adapter;
    BaseRefreshLayout baseRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_record);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("返利记录");
        ivTitleRight.setVisibility(View.VISIBLE);
        ivTitleRight.setText("申请返利");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, 30, getResources().getColor(R.color.line_lowgray)));

        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        // 设置适配器
        adapter = new BackRecordAadapter();
        baseRefreshLayout.setAdapter(adapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.refresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(Boolean isLogin) {
        if (isLogin){
            baseRefreshLayout.refresh();
        }else {
            finish();
        }
    }
    @OnClick({R.id.iv_titleLeft,R.id.tv_titleRight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                BackActivity.start(this);
                break;
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, BackRecordActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void getPageData(final int requestPageNo) {
        final ChargeRrcordListRequestBean requestBean =new ChargeRrcordListRequestBean();
        requestBean.setPage(requestPageNo);
        requestBean.setOffset(10);
        HttpParamsBuild httpParamsBuild=new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<BackRecordList>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(final BackRecordList data) {
                if(data!=null&&data.getList()!=null){
                    baseRefreshLayout.resultLoadData(adapter.getData(),data.getList(),10);
                }else{
                    baseRefreshLayout.resultLoadData(adapter.getData(),new ArrayList(),requestPageNo-1);
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

        RxVolley.post(AppApi.getUrl(AppApi.backRecord), httpParamsBuild.getHttpParams(),httpCallbackDecode);
    }
}
