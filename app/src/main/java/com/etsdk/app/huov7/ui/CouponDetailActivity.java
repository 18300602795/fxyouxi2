package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.AdImage;
import com.etsdk.app.huov7.model.ApplyCouponRequestBean;
import com.etsdk.app.huov7.model.CouponDetail;
import com.etsdk.app.huov7.model.CouponListItem;
import com.etsdk.app.huov7.model.CouponTopperAd;
import com.etsdk.app.huov7.ui.dialog.CouponExchangeDialogUtil;
import com.etsdk.app.huov7.view.AdImageView;
import com.etsdk.app.huov7.view.LoadStatusView;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.liang530.utils.BaseAppUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CouponDetailActivity extends ImmerseActivity {
    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.tv_coupone_name)
    TextView tvCouponeName;
    @BindView(R.id.tv_exchangeScore)
    TextView tvExchangeScore;
    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.pb_progressBar)
    ProgressBar pbProgressBar;
    @BindView(R.id.tv_money_text)
    TextView tvMoneyText;
    @BindView(R.id.tv_endTime)
    TextView tvEndTime;
    @BindView(R.id.tv_scope)
    TextView tvScope;
    @BindView(R.id.tv_func)
    TextView tvFunc;
    @BindView(R.id.tv_my_score)
    TextView tvMyScore;
    @BindView(R.id.btn_exchange)
    Button btnExchange;
    @BindView(R.id.activity_coupon_detail)
    LinearLayout activityCouponDetail;
    @BindView(R.id.loadview)
    LoadStatusView loadview;
    @BindView(R.id.tv_fail_hint)
    TextView tvFailHint;
    @BindView(R.id.iv_couponTop)
    AdImageView ivCouponTop;
    private String couponId;
    private SimpleDateFormat simpleDateFormat;
    private CouponListItem couponListItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_detail);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("详情");
        Intent intent = getIntent();
        couponId = intent.getStringExtra("couponId");
        loadview.showLoading();
        loadview.setOnLoadRefreshListener(new LoadStatusView.OnLoadRefreshListener() {
            @Override
            public void onLoadRefresh() {
                getCouponData();
            }
        });
        setAdImageHeight(ivCouponTop);
        getCouponData();
        getCoupontopperAd();
    }

    private void updateCouponData(CouponListItem couponListItem) {
        this.couponListItem = couponListItem;
        tvCouponeName.setText(couponListItem.getCouponname());
        tvExchangeScore.setText(couponListItem.getIntegral() + "积分");
        tvMoneyText.setText(couponListItem.getMoney() + "元");
        if ("0".equals(couponListItem.getEnttime())) {
            tvEndTime.setText("无限期");
        } else {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                tvEndTime.setText(simpleDateFormat.format(new Date(Long.parseLong(couponListItem.getEnttime()) * 1000)));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        tvScope.setText(couponListItem.getScope());
        tvFunc.setText(couponListItem.getFunc());
        int total = couponListItem.getTotal();
        int remain = couponListItem.getRemain();
        int progress = 100;
        if (total != 0) {
            progress = (int) (remain * 100. / total);
        }
        pbProgressBar.setProgress(progress);
        tvProgress.setText("剩余：" + progress + "%");
        tvMyScore.setText(couponListItem.getMyintegral());
        try {
            if (Long.parseLong(couponListItem.getMyintegral()) < Long.parseLong(couponListItem.getIntegral())) {
                btnExchange.setText("去赚积分");
                tvFailHint.setVisibility(View.VISIBLE);
            } else {
                btnExchange.setText("兑换");
                tvFailHint.setVisibility(View.GONE);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            btnExchange.setText("去赚积分");
            tvFailHint.setVisibility(View.VISIBLE);
        }
    }

    private void getCouponData() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.couponDetailApi);
        httpParams.put("couponid", couponId);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.couponDetailApi), new HttpJsonCallBackDialog<CouponDetail>() {
            @Override
            public void onDataSuccess(CouponDetail data) {
                if (data != null && data.getData() != null) {
                    updateCouponData(data.getData());
                    loadview.showSuccess();
                } else {
                    loadview.showFail();
                }
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                super.onJsonSuccess(code, msg, data);
                loadview.showFail();
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                super.onFailure(errorNo, strMsg, completionInfo);
                loadview.showFail();
            }
        });
    }


    public static void start(Context context, String couponId) {
        Intent starter = new Intent(context, CouponDetailActivity.class);
        starter.putExtra("couponId", couponId);
        context.startActivity(starter);
    }

    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight, R.id.btn_exchange})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
            case R.id.btn_exchange:
                try {
                    if (Long.parseLong(couponListItem.getMyintegral()) < Long.parseLong(couponListItem.getIntegral())) {
                        DoScoreTaskActivity.start(mActivity);
                    } else {
                        applyCoupon();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    DoScoreTaskActivity.start(mActivity);
                }
                break;
        }
    }

    public void applyCoupon() {
        final ApplyCouponRequestBean applyCouponRequestBean = new ApplyCouponRequestBean();
        applyCouponRequestBean.setCouponid(couponId);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(applyCouponRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<CouponListItem>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(CouponListItem data) {
                if (data != null) {
                    new CouponExchangeDialogUtil().showExchangeDialog(mContext, "", "");
                    updateCouponData(data);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.userCouponAddApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    private void setAdImageHeight(ImageView imageView) {
        int height = (int) (BaseAppUtil.getDeviceWidth(mContext) * AppApi.AD_IMAGE_HW_RATA);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        } else {
            layoutParams.height = height;
        }
        imageView.setLayoutParams(layoutParams);
    }

    private void getCoupontopperAd() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.slideListApi);
        httpParams.put("type", "coupontopper");
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).setShowErrorToast(false).get(AppApi.getUrl(AppApi.slideListApi), new HttpJsonCallBackDialog<CouponTopperAd>() {
            @Override
            public void onDataSuccess(CouponTopperAd data) {
                if (data != null && data.getData() != null
                        && data.getData().getCoupontopper() != null
                        && data.getData().getCoupontopper().getList() != null
                        && data.getData().getCoupontopper().getList().size() > 0) {
                    AdImage adImage = data.getData().getCoupontopper().getList().get(0);
                    ivCouponTop.setAdImage(adImage);
                }
            }
        });
    }

}
