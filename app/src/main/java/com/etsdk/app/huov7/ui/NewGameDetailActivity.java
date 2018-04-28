package com.etsdk.app.huov7.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.VpAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.GameDetail;
import com.etsdk.app.huov7.ui.fragment.DetailDescFragment;
import com.etsdk.app.huov7.ui.fragment.DetailFuliFragment;
import com.etsdk.app.huov7.view.DrawCanvas;
import com.etsdk.app.huov7.view.GameDetailDownView;
import com.etsdk.app.huov7.view.GameTagView;
import com.etsdk.app.huov7.view.LoadStatusView;
import com.etsdk.app.huov7.view.PullHeaderLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.log.L;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.liang530.utils.BaseAppUtil;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;
import com.liang530.views.viewpager.SViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewGameDetailActivity extends ImmerseActivity implements PullHeaderLayout.OnScrollListener {

    @BindView(R.id.iv_zoom)
    DrawCanvas ivZoom;
    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.iv_game_img)
    RoundedImageView ivGameImg;
    @BindView(R.id.tv_game_name)
    TextView tvGameName;
    @BindView(R.id.tv_game_size)
    TextView tvGameSize;
    @BindView(R.id.gameTagView)
    GameTagView gameTagView;
    @BindView(R.id.ll_size_type)
    LinearLayout llSizeType;
    @BindView(R.id.tv_game_status)
    TextView tvGameStatus;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    @BindView(R.id.tablayout)
    SlidingTabLayout tablayout;
    @BindView(R.id.ll_headView)
    LinearLayout llHeadView;
    @BindView(R.id.viewpager)
    SViewPager viewpager;
    @BindView(R.id.pullHeaderLayout)
    PullHeaderLayout pullHeaderLayout;
    @BindView(R.id.loadview)
    LoadStatusView loadview;
    @BindView(R.id.activity_new_game)
    RelativeLayout activityNewGame;
    @BindView(R.id.gameDetailDownView)
    GameDetailDownView gameDetailDownView;
    private List<Fragment> fragmentList = new ArrayList<>();
    private String[] titleList = {"简介", "福利"};
    private VpAdapter vpAdapter;
    private boolean isOpen = false;
    private DetailDescFragment detailDescFragment;
    private String gameId;
    private GameBean gameBean;
    private boolean isFinishing=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game_detail);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        Intent intent = getIntent();
        if (intent != null) {
            gameId = intent.getStringExtra("gameId");
        }
        detailDescFragment = new DetailDescFragment();
        fragmentList.add(detailDescFragment);
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

        activityNewGame.setTranslationY(BaseAppUtil.getDeviceHeight(this));
        pullHeaderLayout.setChangeHeightView(activityNewGame, gameDetailDownView);
        pullHeaderLayout.setOnScrollListener(this);
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
        Glide.with(NewGameDetailActivity.this).load(gameBean.getIcon()).placeholder(R.mipmap.icon_load).into(ivGameImg);
        tvGameName.setText(gameBean.getGamename());
        tvTitleName.setText(gameBean.getGamename());
        tvGameSize.setText(gameBean.getSize());
        tvGameStatus.setText(gameBean.getTeststatus());
        gameTagView.setGameType(gameBean.getType());
        loadview.showSuccess();
        detailDescFragment.setupGameData(gameBean);
        gameDetailDownView.setGameBean(gameBean);
    }

    public GameBean getGameBean() {
        return gameBean;
    }


    public static void start(Context context, String gameId) {
        Intent starter = new Intent(context, NewGameDetailActivity.class);
        starter.putExtra("gameId", gameId);
        context.startActivity(starter);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.static_anim, R.anim.static_anim);
        }
    }

    @Override
    public void finish() {
        if(isFinishing){//防止重复点击返回键，造成动画执行多次
            return;
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(activityNewGame, "translationY", BaseAppUtil.getDeviceHeight(this)).setDuration(300);
        objectAnimator.setInterpolator(new AccelerateInterpolator(3));
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isFinishing=true;
                ivZoom.setAlpha(0);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                NewGameDetailActivity.super.finish();
                overridePendingTransition(R.anim.static_anim, R.anim.static_anim);
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        objectAnimator.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isOpen) {
            show();
        }
    }

    private void show() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(activityNewGame, "translationY", 0).setDuration(500);
        objectAnimator.setInterpolator(new AccelerateInterpolator(2));
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Object animatedValue = animation.getAnimatedValue();
                L.e("hongliang", "animatedValue=" + animatedValue);
                if ((float) animatedValue == 0) {
                    ivZoom.setAlpha(150);
                }
            }
        });
        objectAnimator.start();
    }

    @Override
    public void onScroll(int x, int y) {
        L.e(TAG, "准备滑动到--》 " + y);
        if (y < 0) {//
            if (Math.abs(y) > 150) {
                y = -150;
            }
            ivZoom.setAlpha(150 + y);
        }
    }
    @OnClick({R.id.tv_pay,R.id.tv_topClose})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_pay:
                GamePayActivity.start(this,gameBean.getGameid(),gameBean.getGamename());
                break;
            case R.id.tv_topClose:
                finish();
                break;
        }
    }
}
