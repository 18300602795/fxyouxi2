package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.BuildConfig;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.getcash.ui.AccountListActivity;
import com.etsdk.app.huov7.getcash.ui.CommRecordActivity;
import com.etsdk.app.huov7.getcash.ui.GetCashActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.MessageEvent;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.etsdk.app.huov7.pay.ChargeActivityForWap;
import com.etsdk.app.huov7.ui.AccountManageActivity;
import com.etsdk.app.huov7.ui.AccountSaveActivity;
import com.etsdk.app.huov7.ui.AuthEmailActivity;
import com.etsdk.app.huov7.ui.AuthPhoneActivity;
import com.etsdk.app.huov7.ui.BindEmailActivity;
import com.etsdk.app.huov7.ui.BindPhoneActivity;
import com.etsdk.app.huov7.ui.LoginActivity;
import com.etsdk.app.huov7.ui.MessageActivity;
import com.etsdk.app.huov7.ui.MineGiftCouponListActivityNew;
import com.etsdk.app.huov7.ui.ServiceActivity;
import com.etsdk.app.huov7.ui.SettingActivity;
import com.etsdk.app.huov7.ui.UpdatePwdActivity;
import com.etsdk.app.huov7.ui.UserChargeRecordActivity;
import com.etsdk.app.huov7.ui.UserSpendRecordActivity;
import com.etsdk.app.huov7.view.LoadStatusView;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpNoLoginCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.T;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.control.LoginControl;
import com.liang530.utils.GlideDisplay;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 2017/5/5.
 */

public class MainMineFragmentNew extends AutoLazyFragment {
    @BindView(R.id.iv_gotoMsg)
    ImageView ivGotoMsg;
    @BindView(R.id.iv_mineHead)
    RoundedImageView ivMineHead;
    @BindView(R.id.tv_nickName)
    TextView tvNickName;
    @BindView(R.id.tv_coin_balance)
    TextView tvCoinBalance;
    @BindView(R.id.rl_account_manage)
    RelativeLayout rlAccountManage;
    @BindView(R.id.ll_mine_gift)
    LinearLayout llMineGift;
    @BindView(R.id.ll_mine_service)
    LinearLayout llMineService;
    @BindView(R.id.ll_mine_save)
    LinearLayout llMineSave;
    @BindView(R.id.ll_mine_charge)
    LinearLayout llMineCharge;
    @BindView(R.id.ll_charge_record)
    LinearLayout llChargeRecord;
    @BindView(R.id.ll_spend_record)
    LinearLayout llSpendRecord;
    @BindView(R.id.ll_setting)
    LinearLayout llSetting;
    @BindView(R.id.loadview)
    LoadStatusView loadview;
    @BindView(R.id.tv_coupon_balance)
    TextView tvCouponBalance;
    @BindView(R.id.ll_coupon_balance)
    LinearLayout llCouponBalance;
    @BindView(R.id.v_mine_charge_line)
    View vMineChargeLine;
    @BindView(R.id.ll_show_platform_money)
    LinearLayout llShowPlatformMoney;
    @BindView(R.id.v_mine_save_line)
    View vMineSaveLine;
    @BindView(R.id.ll_mine_phone)
    LinearLayout llMinePhone;
    @BindView(R.id.ll_mine_mail)
    LinearLayout llMineMail;
    @BindView(R.id.ll_mine_modify_password)
    LinearLayout llMineModifyPassword;
    @BindView(R.id.ll_account_save_option)
    LinearLayout llAccountSaveOption;
    @BindView(R.id.v_mine_phone_line)
    View vMinePhoneLine;
    private List<GameBean> gameBeanList = new ArrayList<>();
    private boolean bindPhoneClickable = false;
    private String phone = null;
    private String email = null;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_main_mine_new);
        EventBus.getDefault().register(this);
        setupUI();
    }

    private void setupUI() {
        if ("0".equals(BuildConfig.USE_PLATFORM_MONEY)) {//不使用平台，隐藏
            vMineChargeLine.setVisibility(View.GONE);
            llMineCharge.setVisibility(View.GONE);
            llShowPlatformMoney.setVisibility(View.GONE);
        } else {
            vMineChargeLine.setVisibility(View.VISIBLE);
            llMineCharge.setVisibility(View.VISIBLE);
            llShowPlatformMoney.setVisibility(View.VISIBLE);
        }
        if (BuildConfig.projectCode == 177) {//177七二网络
            ivMineHead.setImageResource(R.mipmap.touxiang);
            llAccountSaveOption.setVisibility(View.VISIBLE);
            llMineSave.setVisibility(View.GONE);
            vMineSaveLine.setVisibility(View.GONE);
            llMinePhone.setVisibility(View.VISIBLE);
            vMinePhoneLine.setVisibility(View.VISIBLE);
        } else {
            llAccountSaveOption.setVisibility(View.GONE);
            llMineSave.setVisibility(View.GONE);
            vMineSaveLine.setVisibility(View.VISIBLE);
            llMinePhone.setVisibility(View.GONE);
            vMinePhoneLine.setVisibility(View.GONE);
        }

//        recyMineGame.setLayoutManager(new GridLayoutManager(mContext, 4));
//        recyMineGame.setAdapter(new MineGameImgAdapter(gameBeanList));
//        setMoneyGameListItemHeight();
//        getMyGameList();
//        getUserAdText();
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                loadview.showSuccess();
//            }
//        }, 3000);
//        loadview.showLoading();
//        MessageEvent messageEvent = EventBus.getDefault().getStickyEvent(MessageEvent.class);
//        onMessageEvent(messageEvent);
    }

    @Override
    protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();
        updateData();
    }

    private void updateUserInfoData(UserInfoResultBean userInfoResultBean) {
        int errorImage = R.mipmap.ic_launcher;
        if (BuildConfig.projectCode == 177) {//177七二网络
            errorImage = R.mipmap.touxiang;
        }
        if (userInfoResultBean != null) {
            bindPhoneClickable = true;
            phone = userInfoResultBean.getMobile();
            email = userInfoResultBean.getEmail();
            tvNickName.setText(userInfoResultBean.getNickname());
            tvCoinBalance.setText(userInfoResultBean.getPtbcnt() + "");
            tvCouponBalance.setText(userInfoResultBean.getGmgamecnt());
//            GlideDisplay.display(ivMineHead, userInfoResultBean.getPortrait(), errorImage);
            Glide.with(getActivity()).load(userInfoResultBean.getPortrait()).placeholder(errorImage).into(ivMineHead);
            loadview.showSuccess();
            //存入用户信息
            LoginControl.saveKey(GsonUtil.getGson().toJson(userInfoResultBean));
            //处理未读消息
            EventBus.getDefault().postSticky(new MessageEvent(userInfoResultBean.getNewmsg()));
        } else {
            tvNickName.setText(getString(R.string.default_login_hint));
            tvCoinBalance.setText("0");
            tvCouponBalance.setText("0");
            GlideDisplay.display(ivMineHead, null, errorImage);
        }
    }

    /**
     * 更新数据
     */
    public void updateData() {
        getUserInfoData();
    }

    public void getUserInfoData() {
        bindPhoneClickable = false;
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpNoLoginCallbackDecode httpCallbackDecode = new HttpNoLoginCallbackDecode<UserInfoResultBean>(getActivity(), httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                if (data != null) {
                    updateUserInfoData(data);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                if (CODE_SESSION_ERROR.equals(code)) {
                    updateUserInfoData(null);
                } else {
                    super.onFailure(code, msg);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userDetailApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
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

    @OnClick({R.id.ll_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_setting:
                SettingActivity.start(mContext);
                break;
        }
    }

    @OnClick({R.id.iv_gotoMsg, R.id.rl_account_manage, R.id.ll_mine_gift, R.id.ll_mine_service,
            R.id.ll_mine_save, R.id.ll_mine_charge, R.id.ll_charge_record, R.id.ll_spend_record,
            R.id.ll_coupon_balance, R.id.ll_mine_phone, R.id.ll_mine_mail, R.id.ll_mine_modify_password,
            R.id.tv_get_cash, R.id.ll_money_account, R.id.ll_get_cash_record, R.id.ll_mine_back})
    public void onClickRequestLogin(View view) {
        if (getString(R.string.default_login_hint).equals(tvNickName.getText().toString())) {
            LoginActivity.start(getContext());
            return;
        }
        switch (view.getId()) {
            case R.id.iv_gotoMsg:
                MessageActivity.start(mContext);
                break;
            case R.id.rl_account_manage:
                AccountManageActivity.start(mContext);
                break;
            case R.id.ll_mine_gift:
                //礼包
                MineGiftCouponListActivityNew.start(mContext, MineGiftCouponListActivityNew.TYPE_GIFT, "礼包");
                break;
            case R.id.ll_mine_service:
                ServiceActivity.start(mContext);
                break;
            case R.id.ll_mine_save:
                AccountSaveActivity.start(mContext);
                break;
            case R.id.ll_mine_charge:
//                SelectGamePayActivity.start(mContext);
                ChargeActivityForWap.start(mContext, AppApi.getUrl(AppApi.chargePingtaibi), "平台币充值", null);
                break;
            case R.id.ll_charge_record:
                UserChargeRecordActivity.start(mContext);
                break;
            case R.id.ll_spend_record:
                UserSpendRecordActivity.start(mContext);
                break;
            case R.id.ll_coupon_balance:
                //代金券
                MineGiftCouponListActivityNew.start(mContext, MineGiftCouponListActivityNew.TYPE_COUPON, "代金券");
                break;
            //以下为177七二网络独有2017-6-26
            case R.id.ll_mine_phone:
                if (!bindPhoneClickable) {
                    T.s(mContext, "正在查询绑定状态，请稍后");
                    break;
                }
                if (TextUtils.isEmpty(phone)) {
                    BindPhoneActivity.start(mContext);
                } else {
                    AuthPhoneActivity.start(mContext, phone);
                }
                break;
            case R.id.ll_mine_mail:
                if (!bindPhoneClickable) {
                    T.s(mContext, "正在查询绑定状态，请稍后");
                    break;
                }
                if (TextUtils.isEmpty(email)) {
                    //绑定邮箱
                    BindEmailActivity.start(mContext);
                } else {
                    //解除绑定
                    AuthEmailActivity.start(mContext, email);
                }
                break;
            case R.id.ll_mine_modify_password:
                UpdatePwdActivity.start(mContext);
                break;
            case R.id.tv_get_cash:
                GetCashActivity.start(mContext);
                break;
            case R.id.ll_money_account:
                AccountListActivity.start(mContext);
                break;
            case R.id.ll_get_cash_record:
                CommRecordActivity.start(mContext, CommRecordActivity.TYPE_GET_CASH_RECORD_LIST);
                break;
            case R.id.ll_mine_back:
//                new CouponExchangeDialogUtil().showExchangeDialog(getActivity(), "友情提示", "该功能正在开发中，敬请期待");
//                BackActivity.start(mContext);
                AccountSaveActivity.start(mContext);
                break;
        }
    }
}
