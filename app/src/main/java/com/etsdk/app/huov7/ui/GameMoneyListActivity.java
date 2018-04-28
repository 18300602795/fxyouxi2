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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.GameMoneyListAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.GameBeanList;
import com.etsdk.app.huov7.model.ListRequestBean;
import com.etsdk.app.huov7.util.RecyclerViewNoAnimator;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameMoneyListActivity extends ImmerseActivity implements AdvRefreshListener {

    GameMoneyListAdapter gameMoneyListAdapter;
    BaseRefreshLayout baseRefreshLayout;
    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.activity_game_money_list)
    LinearLayout activityGameMoneyList;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    private List<GameBean> gameBeanList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_money_list);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("游戏币余额");
        if (getIntent() != null) {
            String title = getIntent().getStringExtra("title");
            if (!TextUtils.isEmpty(title)) {
                tvTitleName.setText(title);
            }
        }
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new RecyclerViewNoAnimator());
        gameMoneyListAdapter = new GameMoneyListAdapter(gameBeanList);
        // 设置适配器
        baseRefreshLayout.setAdapter(gameMoneyListAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.refresh();
    }

    @Override
    public void getPageData(final int requestPageNo) {

        final ListRequestBean baseRequestBean = new ListRequestBean();
        baseRequestBean.setPage(requestPageNo+"");
        baseRequestBean.setOffset("20");
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<GameBeanList.DataBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(GameBeanList.DataBean data) {
//                if (data != null&&data.getList()!=null) {
//                    baseRefreshLayout.resultLoadData(gameBeanList,data.getList(),1);
//                }else{
//                    baseRefreshLayout.resultLoadData(gameBeanList,new ArrayList(),1);
//                }
                if(data!=null&&data.getList()!=null){
                    int maxPage = (int)Math.ceil(data.getCount() / 20.);
                    baseRefreshLayout.resultLoadData(gameBeanList,data.getList(),maxPage);
                }else{
                    baseRefreshLayout.resultLoadData(gameBeanList,new ArrayList(),requestPageNo-1);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                baseRefreshLayout.resultLoadData(gameBeanList,null,requestPageNo);
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        RxVolley.post(AppApi.getUrl(AppApi.userGmlistApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, GameMoneyListActivity.class);
        context.startActivity(starter);
    }

    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight})
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
}
