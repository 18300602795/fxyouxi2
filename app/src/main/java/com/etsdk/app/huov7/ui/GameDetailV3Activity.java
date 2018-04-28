package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.VpAdapter;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.GameDetail;
import com.etsdk.app.huov7.ui.fragment.DetailDescFragment;
import com.etsdk.app.huov7.ui.fragment.DetailFuliFragment;
import com.etsdk.app.huov7.ui.fragment.TestGameListFragment;
import com.etsdk.app.huov7.view.GameDetailDownView;
import com.etsdk.app.huov7.view.GameTagView;
import com.etsdk.app.huov7.view.LoadStatusView;
import com.etsdk.app.huov7.view.widget.XSViewPager;
import com.flyco.tablayout.SlidingTabLayout;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.application.BaseActivity;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameDetailV3Activity extends BaseActivity {
    @BindView(R.id.view_bg_detail)
    View detailBGView;
    @BindView(R.id.iv_game_img)
    ImageView ivGameImg;
    @BindView(R.id.tv_game_name)
    TextView tvGameName;
    @BindView(R.id.ll_size_type)
    LinearLayout llSizeType;
    @BindView(R.id.tv_game_status)
    TextView tvGameStatus;
    @BindView(R.id.iv_pay)
    ImageView ivPay;
    @BindView(R.id.tablayout)
    SlidingTabLayout tablayout;
    @BindView(R.id.viewpager)
    XSViewPager viewpager;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;
    List<Fragment> fragmentList = new ArrayList<>();
    @BindView(R.id.tv_game_size)
    TextView tvGameSize;
    @BindView(R.id.gameTagView)
    GameTagView gameTagView;
    @BindView(R.id.loadview)
    LoadStatusView loadview;
    @BindView(R.id.gameDetailDownView)
    GameDetailDownView gameDetailDownView;
    private VpAdapter vpAdapter;
    private String[] titleList = {"简介", "福利"};
    private String gameId = "0";
    private GameBean gameBean;
    private DetailDescFragment detailDescFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail_v3);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        Intent intent = getIntent();
        if (intent != null) {
            gameId = intent.getStringExtra("gameId");
        }

        slidingUpPanelLayout.setMinFlingVelocity(500);
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED){
                    //关闭Activity
                    GameDetailV3Activity.this.finish();
                }

            }
        });
        detailDescFragment = new DetailDescFragment();
        fragmentList.add(TestGameListFragment.newInstance(true, true));
        fragmentList.add(DetailFuliFragment.newInstance(gameId));
        vpAdapter = new VpAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewpager.setOffscreenPageLimit(3);
        viewpager.setCanScroll(true);
        viewpager.setAdapter(vpAdapter);
        tablayout.setViewPager(viewpager);
        loadview.showLoading();
        loadview.setOnLoadRefreshListener(new LoadStatusView.OnLoadRefreshListener() {
            @Override
            public void onLoadRefresh() {
                getGameDetailData();
            }
        });
        getGameDetailData();
    }

    @OnClick({R.id.iv_pay,R.id.view_bg_detail})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_pay:
                if(gameBean==null) return;
                GamePayActivity.start(mActivity,gameBean.getGameid(),gameBean.getGamename());
                break;
            case R.id.view_bg_detail:
                GameDetailV3Activity.this.finish();
                break;
        }
    }

    private void getGameDetailData() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.gameDetail);
        httpParams.put("gameid", gameId + "");
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.gameDetail), new HttpJsonCallBackDialog<GameDetail>() {
            @Override
            public void onDataSuccess(GameDetail data) {
                if (data != null && data.getData() != null) {
                    setupData(data.getData());
                } else {
                    loadview.showFail();
                }
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                loadview.showFail();
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                loadview.showFail();
            }
        });
    }

    private void setupData(GameBean gameBean) {
        this.gameBean = gameBean;
//        GlideDisplay.display(ivGameImg, gameBean.getIcon(), R.mipmap.ic_launcher);
        Glide.with(GameDetailV3Activity.this).load(gameBean.getIcon()).placeholder(R.mipmap.icon_load).into(ivGameImg);
        tvGameName.setText(gameBean.getGamename());
        tvGameSize.setText(gameBean.getSize());
        tvGameStatus.setText(gameBean.getOneword());
        gameTagView.setGameType(gameBean.getType());
        loadview.showSuccess();
        detailDescFragment.setupGameData(gameBean);
        gameDetailDownView.setGameBean(gameBean);
    }

    public GameBean getGameBean() {
        return gameBean;
    }

    public static void start(Context context, String gameId) {
        Intent starter = new Intent(context, GameDetailV3Activity.class);
        starter.putExtra("gameId", gameId);
        context.startActivity(starter);
    }

}
