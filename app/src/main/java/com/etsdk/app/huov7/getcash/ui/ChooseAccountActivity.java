package com.etsdk.app.huov7.getcash.ui;

import android.app.Activity;
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
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.getcash.GetCaseApi;
import com.etsdk.app.huov7.getcash.adapter.ChooseAccountRcyAadapter;
import com.etsdk.app.huov7.getcash.model.AccountListBean;
import com.etsdk.app.huov7.getcash.model.AccountListRequestBean;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.control.LoginControl;
import com.liang530.log.L;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择提现账户
 */
public class ChooseAccountActivity extends ImmerseActivity implements AdvRefreshListener {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;

    BaseRefreshLayout baseRefreshLayout;
    ChooseAccountRcyAadapter adapter;
    int accountId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        accountId = getIntent().getIntExtra("accountId", 0);
        tvTitleName.setText("选择提现账户");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        // 设置适配器
        adapter = new ChooseAccountRcyAadapter(this);
        adapter.setSelectAccountId(accountId);
        baseRefreshLayout.setAdapter(adapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.setCanloadMore(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        baseRefreshLayout.refresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.release();
    }

    @Override
    public void getPageData(final int i) {
        AccountListRequestBean requestBean =new AccountListRequestBean();
        requestBean.setUsername(LoginControl.getAccount());
        requestBean.setPage(i);
        requestBean.setOffset(20);
        HttpParamsBuild httpParamsBuild=new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<AccountListBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(final AccountListBean data) {
                if(data!=null&&data.getCash_list()!=null){
                    baseRefreshLayout.resultLoadData(adapter.getData(),data.getCash_list(),20);
                }else{
                    baseRefreshLayout.resultLoadData(adapter.getData(),new ArrayList(),i-1);
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
        RxVolley.post(AppApi.getUrl(GetCaseApi.URL_ACOUNT_LIST), httpParamsBuild.getHttpParams(),httpCallbackDecode);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ChooseAccountActivity.class);
        context.startActivity(starter);
    }

    public static void startForResualt(Activity activity, int accountId) {
        Intent starter = new Intent(activity, ChooseAccountActivity.class);
        starter.putExtra("accountId", accountId);
        activity.startActivityForResult(starter, 0);
    }

    @OnClick({R.id.iv_titleLeft})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
        }
    }

}
