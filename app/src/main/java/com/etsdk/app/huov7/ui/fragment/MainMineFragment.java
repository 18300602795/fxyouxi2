package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.MineGameImgAdapter;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.GameBeanList;
import com.etsdk.app.huov7.model.MessageEvent;
import com.etsdk.app.huov7.model.MineAdText;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.etsdk.app.huov7.ui.AccountManageActivity;
import com.etsdk.app.huov7.ui.DoScoreTaskActivity;
import com.etsdk.app.huov7.ui.FeedBackActivity;
import com.etsdk.app.huov7.ui.GameMoneyListActivity;
import com.etsdk.app.huov7.ui.MessageActivity;
import com.etsdk.app.huov7.ui.MineGiftCouponListActivity;
import com.etsdk.app.huov7.ui.NewGameDetailActivity;
import com.etsdk.app.huov7.ui.RecommandTaskActivity;
import com.etsdk.app.huov7.ui.ScoreRankActivity;
import com.etsdk.app.huov7.ui.ScoreShopActivity;
import com.etsdk.app.huov7.ui.SelectGamePayActivity;
import com.etsdk.app.huov7.ui.SettingActivity;
import com.etsdk.app.huov7.ui.SignInActivity;
import com.etsdk.app.huov7.view.LoadStatusView;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.control.LoginControl;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.liang530.utils.BaseAppUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by liu hong liang on 2016/12/15.
 */

public class MainMineFragment extends AutoLazyFragment {
    @BindView(R.id.iv_mineHead)
    ImageView ivMineHead;
    @BindView(R.id.rl_account_manage)
    RelativeLayout rlAccountManage;
    @BindView(R.id.ll_mine_coupon)
    LinearLayout llMineCoupon;
    @BindView(R.id.line_money)
    View lineMoney;
    @BindView(R.id.ll_mine_gift)
    LinearLayout llMineGift;
    @BindView(R.id.rl_mine_game_money)
    RelativeLayout rlMineGameMoney;
    @BindView(R.id.ll_score_task)
    LinearLayout llScoreTask;
    @BindView(R.id.ll_score_shop)
    LinearLayout llScoreShop;
    @BindView(R.id.ll_score_rank)
    LinearLayout llScoreRank;
    @BindView(R.id.ll_setting)
    LinearLayout llSetting;
    @BindView(R.id.recy_mine_game)
    RecyclerView recyMineGame;
    @BindView(R.id.ll_goto_doTask)
    LinearLayout llGotoDoTask;
    @BindView(R.id.ll_gotoRecTask)
    LinearLayout llGotoRecTask;
    @BindView(R.id.ll_platform_feedback)
    LinearLayout llPlatformFeedback;
    @BindView(R.id.iv_gotoMsg)
    ImageView ivGotoMsg;
    @BindView(R.id.tv_nickName)
    TextView tvNickName;
    @BindView(R.id.btn_pay)
    Button btnPay;
    @BindView(R.id.tv_my_score)
    TextView tvMyScore;
    @BindView(R.id.tv_coupon_count)
    TextView tvCouponCount;
    @BindView(R.id.tv_gift_count)
    TextView tvGiftCount;
    @BindView(R.id.rl_mine_sign)
    RelativeLayout rlMineSign;
    @BindView(R.id.tv_gameAd)
    TextView tvGameAd;
    @BindView(R.id.tv_signAd)
    TextView tvSignAd;
    @BindView(R.id.tv_taskAd)
    TextView tvTaskAd;
    @BindView(R.id.tv_earnAd)
    TextView tvEarnAd;
    @BindView(R.id.tv_shopAd)
    TextView tvShopAd;
    @BindView(R.id.tv_rankAd)
    TextView tvRankAd;
    @BindView(R.id.loadview)
    LoadStatusView loadview;
    @BindView(R.id.ll_mineSignOption)
    LinearLayout llMineSignOption;
    @BindView(R.id.ll_tgyTask)
    LinearLayout llTgyTask;
    @BindView(R.id.ll_mine_game_list)
    LinearLayout llMineGameList;
    private List<GameBean> gameBeanList = new ArrayList<>();

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_main_mine);
        EventBus.getDefault().register(this);
        setupUI();
    }

    private void setupUI() {
        recyMineGame.setLayoutManager(new GridLayoutManager(mContext, 4));
        recyMineGame.setAdapter(new MineGameImgAdapter(gameBeanList));
        setMoneyGameListItemHeight();
        getMyGameList();
        getUserAdText();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadview.showSuccess();
            }
        }, 3000);
        loadview.showLoading();
        MessageEvent messageEvent = EventBus.getDefault().getStickyEvent(MessageEvent.class);
        onMessageEvent(messageEvent);
    }

    @Override
    protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();
        updateData();
    }

    private void updateUserInfoData(UserInfoResultBean userInfoResultBean) {
        tvNickName.setText(userInfoResultBean.getNickname());
        tvMyScore.setText(userInfoResultBean.getMyintegral());
        tvCouponCount.setText(userInfoResultBean.getCouponcnt() + "张");
        tvGiftCount.setText(userInfoResultBean.getGiftcnt() + "个");
//        GlideDisplay.display(ivMineHead, userInfoResultBean.getPortrait(), R.mipmap.ic_launcher);
        Glide.with(getActivity()).load(userInfoResultBean.getPortrait()).placeholder(R.mipmap.ic_launcher).into(ivMineHead);
        loadview.showSuccess();
        //存入用户信息
        LoginControl.saveKey(GsonUtil.getGson().toJson(userInfoResultBean));
        //处理未读消息
        EventBus.getDefault().postSticky(new MessageEvent(userInfoResultBean.getNewmsg()));
    }

    /**
     * 更新数据
     */
    public void updateData() {
        getUserInfoData();
        getMyGameList();
    }

    public void getUserInfoData() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserInfoResultBean>(getActivity(), httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                if (data != null) {
                    updateUserInfoData(data);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userDetailApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    private void getMyGameList() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<GameBeanList.DataBean>(getActivity(), httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(GameBeanList.DataBean data) {
                gameBeanList.clear();
                if (data != null && data.getList() != null) {
                    gameBeanList.addAll(data.getList());
                }
                updateMoneyGameListShow();
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userGmlistApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    /**
     * 设置4个游戏币余额的高度显示
     */
    private void setMoneyGameListItemHeight(){
        int childCount = llMineGameList.getChildCount();
        int deviceWidth = BaseAppUtil.getDeviceWidth(mContext);
        int height=(deviceWidth-BaseAppUtil.dip2px(mContext,30)*2-BaseAppUtil.dip2px(mContext,20)*3)/4;
        for(int i=0;i<childCount;i++){
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llMineGameList.getChildAt(i).getLayoutParams();
            layoutParams.height=height;
            layoutParams.weight=height;
            llMineGameList.getChildAt(i).setLayoutParams(layoutParams);
        }
    }

    private void updateMoneyGameListShow(){
        if(gameBeanList==null||gameBeanList.size()==0){
            llMineGameList.setVisibility(View.GONE);
        }else{
            llMineGameList.setVisibility(View.VISIBLE);
            int size = gameBeanList.size();
            for(int i=0;i<4;i++){
                ImageView imageView = (ImageView) llMineGameList.getChildAt(i);
                if(i<size){
                    final GameBean gameBean = gameBeanList.get(i);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setClickable(true);
                    imageView.setEnabled(true);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NewGameDetailActivity.start(v.getContext(),gameBean.getGameid());
                        }
                    });
//                    GlideDisplay.display(imageView, gameBean.getIcon(), R.mipmap.ic_launcher);
                    Glide.with(getActivity()).load(gameBean.getIcon()).placeholder(R.mipmap.ic_launcher).into(imageView);
                }else{
                    imageView.setVisibility(View.INVISIBLE);
                    imageView.setClickable(false);
                    imageView.setEnabled(false);
                }
            }
        }
    }

    private void getUserAdText() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.slideListApi);
        httpParams.put("type", "user");
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).setShowErrorToast(false).get(AppApi.getUrl(AppApi.slideListApi), new HttpJsonCallBackDialog<MineAdText>() {
            @Override
            public void onDataSuccess(MineAdText data) {
                if (data != null && data.getData() != null) {
                    updateMineAdText(data.getData());
                }
            }
        });
    }

    private void updateMineAdText(MineAdText.DataBean dataBean) {
        if (dataBean.getUsermygame() != null && dataBean.getUsermygame().getList() != null && dataBean.getUsermygame().getList().size() > 0) {
            tvGameAd.setText(dataBean.getUsermygame().getList().get(0).getName());
        }
        if (dataBean.getUsermysign() != null && dataBean.getUsermysign().getList() != null && dataBean.getUsermysign().getList().size() > 0) {
            tvSignAd.setText(dataBean.getUsermysign().getList().get(0).getName());
        }

        if (dataBean.getUseract() != null && dataBean.getUseract().getList() != null && dataBean.getUseract().getList().size() > 0) {
            tvTaskAd.setText(dataBean.getUseract().getList().get(0).getName());
        }
        if (dataBean.getUserearn() != null && dataBean.getUserearn().getList() != null && dataBean.getUserearn().getList().size() > 0) {
            tvEarnAd.setText(dataBean.getUserearn().getList().get(0).getName());
        }
        if (dataBean.getUsershop() != null && dataBean.getUsershop().getList() != null && dataBean.getUsershop().getList().size() > 0) {
            tvShopAd.setText(dataBean.getUsershop().getList().get(0).getName());
        }
        if (dataBean.getUserrank() != null && dataBean.getUserrank().getList() != null && dataBean.getUserrank().getList().size() > 0) {
            tvRankAd.setText(dataBean.getUserrank().getList().get(0).getName());
        }
    }

    @OnClick({R.id.rl_account_manage, R.id.ll_mine_coupon, R.id.ll_mine_gift,
            R.id.rl_mine_game_money, R.id.iv_gotoMsg, R.id.ll_goto_doTask,
            R.id.ll_score_task, R.id.ll_score_shop, R.id.ll_score_rank,
            R.id.ll_setting, R.id.ll_platform_feedback, R.id.ll_gotoRecTask,
            R.id.rl_mine_sign, R.id.btn_pay, R.id.ll_mineSignOption, R.id.ll_tgyTask
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_account_manage:
                AccountManageActivity.start(mContext);
                break;
            case R.id.ll_mine_coupon:
                MineGiftCouponListActivity.start(mContext, 1);
                break;
            case R.id.ll_mine_gift:
                MineGiftCouponListActivity.start(mContext, 0);
                break;
            case R.id.rl_mine_game_money:
                GameMoneyListActivity.start(mContext);
                break;
            case R.id.ll_tgyTask:
                RecommandTaskActivity.start(mContext);
                break;
            case R.id.iv_gotoMsg:
                MessageActivity.start(mContext);
                break;
            case R.id.ll_goto_doTask:
                DoScoreTaskActivity.start(mContext);
                break;
            case R.id.ll_score_task:
                DoScoreTaskActivity.start(mContext);
                break;
            case R.id.ll_score_shop:
                ScoreShopActivity.start(mContext);
                break;
            case R.id.ll_score_rank:
                ScoreRankActivity.start(mContext);
                break;
            case R.id.ll_setting:
                SettingActivity.start(mContext);
                break;
            case R.id.ll_platform_feedback:
                FeedBackActivity.start(mContext);
                break;
            case R.id.ll_gotoRecTask:
                DoScoreTaskActivity.start(mContext);
                break;
            case R.id.rl_mine_sign:
                SignInActivity.start(mContext);
                break;
            case R.id.ll_mineSignOption:
                SignInActivity.start(mContext);
                break;
            case R.id.btn_pay:
                SelectGamePayActivity.start(mContext);
                break;
        }
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
    protected void onDestroyViewLazy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroyViewLazy();
    }
}
