package com.etsdk.app.huov7.getcash.ui;

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
import com.etsdk.app.huov7.getcash.adapter.GetCashRecordRcyAadapter;
import com.etsdk.app.huov7.getcash.model.CommRecordListBean;
import com.etsdk.app.huov7.getcash.model.CommRrcordListRequestBean;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.L;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.views.refresh.mvc.IDataAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 通用的列表型
 * 收入记录列表、商城兑换记录、提现记录
 */
public class CommRecordActivity extends ImmerseActivity implements AdvRefreshListener {
    public static final int TYPE_GET_CASH_RECORD_LIST = 0;
    public static final int TYPE_INCOME_LIST = 1;
    public static final int TYPE_GOODS_LIST = 2;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;

    IDataAdapter<List<CommRecordListBean.DataBean>> adapter;
    BaseRefreshLayout baseRefreshLayout;
    int type = 0;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        type = getIntent().getIntExtra("type", 0);
        switch (type){
            case TYPE_GET_CASH_RECORD_LIST:
                tvTitleName.setText("提现记录");
                adapter = new GetCashRecordRcyAadapter();
                url = AppApi.getUrl(GetCaseApi.URL_ACOUNT_GET_CASH_ROCORD);
                break;
//            case TYPE_INCOME_LIST:
//                tvTitleName.setText("收入记录");
//                adapter = new IncomeRecordRcyAadapter();
//                url = AppApi.getUrl(AppApi.userGoldList);
//                break;
//            case TYPE_GOODS_LIST:
//                tvTitleName.setText("商城兑换记录");
//                adapter = new GoodsRecordRcyAadapter();
//                url = AppApi.getUrl(AppApi.URL_GOODS_RECORD_LIST);
//                break;
        }
        tvTitleRight.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.setAdapter(adapter);
        baseRefreshLayout.refresh();
    }

    @Override
    public void getPageData(final int i) {
        final CommRrcordListRequestBean requestBean =new CommRrcordListRequestBean();
        requestBean.setPage(i);
        requestBean.setOffset(10);
        if(type == TYPE_INCOME_LIST){
            requestBean.setType(1);
        }
        HttpParamsBuild httpParamsBuild=new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<CommRecordListBean>(this, httpParamsBuild.getAuthkey()) {//使用通用bean接收结果
            @Override
            public void onDataSuccess(final CommRecordListBean data) {
                switch (type){
                    case TYPE_INCOME_LIST:
                    case TYPE_GET_CASH_RECORD_LIST:
                        if(data!=null&&data.getList()!=null) {
                            int maxPage = (int)Math.ceil(data.getCount() / 10.);
                            baseRefreshLayout.resultLoadData(adapter.getData(),data.getList(),maxPage);
                            return;
                        }
                    case TYPE_GOODS_LIST:
                        if(data!=null&&data.getProduct_list()!=null) {
                            int maxPage = (int)Math.ceil(data.getCount() / 10.);
                            baseRefreshLayout.resultLoadData(adapter.getData(),data.getProduct_list(),maxPage);
                            return;
                        }
                }
                //上面不执行就是空的
                baseRefreshLayout.resultLoadData(adapter.getData(),new ArrayList(),i-1);
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
        RxVolley.post(url, httpParamsBuild.getHttpParams(),httpCallbackDecode);
    }

    public static void start(Context context, int type) {
        Intent starter = new Intent(context, CommRecordActivity.class);
        starter.putExtra("type", type);
        context.startActivity(starter);
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
