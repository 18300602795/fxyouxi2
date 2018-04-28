package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.ScoreShopAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.util.RecyclerViewNoAnimator;
import com.liang530.views.refresh.mvc.AdvRefreshListener;
import com.liang530.views.refresh.mvc.BaseRefreshLayout;
import com.liang530.views.refresh.mvc.UltraRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

public class ScoreShopActivity extends ImmerseActivity implements AdvRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptrRefresh)
    PtrClassicFrameLayout ptrRefresh;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;
    ScoreShopAdapter scoreShopAdapter;
    BaseRefreshLayout baseRefreshLayout;
    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_shop);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
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
        tvTitleName.setText("积分商城");
        baseRefreshLayout = new UltraRefreshLayout(ptrRefresh);
        scoreShopAdapter = new ScoreShopAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = scoreShopAdapter.getItemViewType(position);
                if(itemViewType==ScoreShopAdapter.ENTITY){
                    return 1;
                }
                return 2;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new RecyclerViewNoAnimator());

        // 设置适配器
        baseRefreshLayout.setAdapter(scoreShopAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
//        // 加载数据
//        baseRefreshLayout.refresh();
    }

    @Override
    public void getPageData(int i) {

    }

    public static void start(Context context) {
        Intent starter = new Intent(context, NewScoreShopActivity.class);
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
