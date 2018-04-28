package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.BuildConfig;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.VpAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.GameDetail;
import com.etsdk.app.huov7.model.MessageEvent;
import com.etsdk.app.huov7.ui.fragment.DetailDescFragment;
import com.etsdk.app.huov7.ui.fragment.GiftListFragment;
import com.etsdk.app.huov7.ui.fragment.NewsListFragment;
import com.etsdk.app.huov7.view.GameDetailDownView;
import com.etsdk.app.huov7.view.GameTagView;
import com.etsdk.app.huov7.view.LoadStatusView;
import com.flyco.tablayout.SlidingTabLayout;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.log.L;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.liang530.views.viewpager.SViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class GameDetailV2Activity extends ImmerseActivity {
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
    @BindView(R.id.ll_headView)
    LinearLayout llHeadView;
    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tablayout)
    SlidingTabLayout tablayout;
    @BindView(R.id.viewpager)
    SViewPager viewpager;
    @BindView(R.id.iv_pay_round)
    ImageView ivPayRound;
    @BindView(R.id.tv_game_size)
    TextView tvGameSize;
    @BindView(R.id.gameTagView)
    GameTagView gameTagView;
    @BindView(R.id.loadview)
    LoadStatusView loadview;
    @BindView(R.id.gameDetailDownView)
    GameDetailDownView gameDetailDownView;
    List<Fragment> fragmentList = new ArrayList<>();
    @BindView(R.id.iv_downManager)
    ImageView ivDownManager;
    @BindView(R.id.iv_gotoMsg)
    ImageView ivGotoMsg;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.tv_rate)
    TextView tvRate;
    @BindView(R.id.tv_send_first)
    TextView tvSendFirst;
    @BindView(R.id.fl_rate)
    ViewGroup flRate;
    private VpAdapter vpAdapter;
    private String[] titleList = new String[]{"简介", "礼包", "攻略", "活动"};
    private String gameId = "0";
    private GameBean gameBean;
    private DetailDescFragment detailDescFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail_v2);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setupUI(null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setupUI(intent);
    }

    private void setupUI(Intent intent) {
        fragmentList.clear();
        if (intent != null) {//onNewintent
            gameId = intent.getStringExtra("gameId");
        } else {//onCreate
            gameId = getIntent().getStringExtra("gameId");
        }
        if (BuildConfig.projectCode == 74) {//显示右边的充值按钮
            ivPayRound.setVisibility(View.VISIBLE);
            ivPay.setVisibility(View.GONE);
        } else {//显示中间的充值按钮
            ivPayRound.setVisibility(View.VISIBLE);
            ivPay.setVisibility(View.GONE);
        }
        detailDescFragment = new DetailDescFragment();
        fragmentList.add(detailDescFragment);
        fragmentList.add(GiftListFragment.newInstance(gameId));
        fragmentList.add(NewsListFragment.newInstance("3", gameId));
        fragmentList.add(NewsListFragment.newInstance("2", gameId));
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
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    tvTitleName.setVisibility(View.GONE);
                    //展开状态

                } else if (state == State.COLLAPSED) {
                    tvTitleName.setVisibility(View.VISIBLE);
                    //折叠状态

                } else {

                    //中间状态

                }
            }
        });
        getGameDetailData();
    }


    @OnClick({R.id.iv_titleLeft, R.id.iv_pay, R.id.iv_downManager, R.id.iv_gotoMsg, R.id.iv_pay_round})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.iv_downManager:
                DownloadManagerActivity.start(mContext);
                break;
            case R.id.iv_gotoMsg:
                MessageActivity.start(mContext);
                break;
            case R.id.iv_pay:
            case R.id.iv_pay_round:
                if (gameBean == null) return;
                GamePayActivity.start(mActivity, gameBean.getGameid(), gameBean.getGamename());
                break;
        }
    }

    private void getGameDetailData() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.gameDetail);
        httpParams.put("gameid", gameId + "");
        L.e("333", "详情url：" + AppApi.getUrl(AppApi.gameDetail));
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
        appBarLayout.setExpanded(true);
//        GlideDisplay.display(ivGameImg, gameBean.getIcon(), R.mipmap.icon_load);
        Glide.with(GameDetailV2Activity.this).load(gameBean.getIcon()).placeholder(R.mipmap.icon_load).into(ivGameImg);
        tvGameName.setText(gameBean.getGamename());
        tvTitleName.setText(gameBean.getGamename());
        tvGameSize.setText(gameBean.getSize());
        tvGameStatus.setText(gameBean.getOneword());
        gameTagView.setGameType(gameBean.getType());
        loadview.showSuccess();
        detailDescFragment.setupGameData(gameBean);
        gameDetailDownView.setGameBean(gameBean);
        //折扣和返利
        flRate.setVisibility(View.GONE);
        if (gameBean.getDiscounttype() != 0) {
            if (gameBean.getDiscounttype() == 1) {
//                tvRate.setText((gameBean.getDiscount()*10)+"折");
                if (gameBean.getFirst_discount() > 0 && gameBean.getFirst_discount() < 1) {
                    tvRate.setText("首充" + Math.round(gameBean.getFirst_discount() * 1000) / 100f + "折，续充" + Math.round(gameBean.getDiscount() * 1000) / 100f + "折");
                    tvRate.setVisibility(View.VISIBLE);
                    flRate.setVisibility(View.VISIBLE);
                } else if (gameBean.getDiscount() > 0 && gameBean.getDiscount() < 1) {
                    tvRate.setText(Math.round(gameBean.getDiscount() * 1000) / 100f + "折");
                    tvRate.setVisibility(View.VISIBLE);
                    flRate.setVisibility(View.VISIBLE);
                }
            } else {
//                tvRate.setText("返利"+(gameBean.getDiscount()*100)+"%");
                if (gameBean.getDiscount() > 0 && gameBean.getDiscount() < 1) {
                    flRate.setVisibility(View.VISIBLE);
                    tvRate.setVisibility(View.VISIBLE);
                    tvRate.setText("赠送" + Math.round(gameBean.getDiscount() * 1000) / 10f + "%");
                }
            }
        }
        if ("2".equals(gameBean.getGive_first())) {
            flRate.setVisibility(View.VISIBLE);
            tvSendFirst.setVisibility(View.VISIBLE);
        } else {
            tvSendFirst.setVisibility(View.GONE);
        }
    }

    public GameBean getGameBean() {
        return gameBean;
    }

    public static void start(Context context, String gameId) {
        Intent starter = new Intent(context, GameDetailV2Activity.class);
        //TODO 此处可以切换为Detail-V3 的样式
//        Intent starter = new Intent(context, NewGameDetailActivity.class);
        starter.putExtra("gameId", gameId);
        context.startActivity(starter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        if (messageEvent != null && "2".equals(messageEvent.getNewMsg())) {
            ivGotoMsg.setImageResource(R.mipmap.syxiaoxi_red);
        } else {
            ivGotoMsg.setImageResource(R.mipmap.syxiaoxi_nomal);
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        JCVideoPlayer.releaseAllVideos();
    }
}
