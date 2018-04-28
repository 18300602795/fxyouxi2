package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.ApplyGoodsRequestBean;
import com.etsdk.app.huov7.model.Goods;
import com.etsdk.app.huov7.model.UserAddressInfo;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.etsdk.app.huov7.ui.dialog.EntityExchangeDialogUtil;
import com.etsdk.app.huov7.ui.dialog.HintDialogUtil;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.utils.BaseAppUtil;
import com.liang530.utils.GlideDisplay;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EntityDetailActivity extends ImmerseActivity {


    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.iv_goodsImg)
    ImageView ivGoodsImg;
    @BindView(R.id.tv_goodsName)
    TextView tvGoodsName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_exchangeScore)
    TextView tvExchangeScore;
    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.pb_progressBar)
    ProgressBar pbProgressBar;
    @BindView(R.id.tv_goods_intro)
    TextView tvGoodsIntro;
    @BindView(R.id.tv_my_score)
    TextView tvMyScore;
    @BindView(R.id.btn_exchange)
    Button btnExchange;
    @BindView(R.id.activity_coupon_detail)
    LinearLayout activityCouponDetail;
    @BindView(R.id.webView_content)
    WebView webViewContent;
    @BindView(R.id.tv_fail_hint)
    TextView tvFailHint;
    private String goodsId;
    private String is_real;
    private Goods goods;
    private boolean isAddressOk = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_detail);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        Intent intent = getIntent();
        String goodsDetailJson = intent.getStringExtra("goodsDetailJson");
        goods = GsonUtil.getGson().fromJson(goodsDetailJson, Goods.class);
        updateGoodsData(goods);
        getMyScore();
        getAddressInfo();
    }

    /**
     * 设置完地址后，重置需要填写地址标记
     */
    @Override
    protected void onResume() {
        super.onResume();
        getAddressInfo();
    }

    private void updateGoodsData(Goods goods) {
        if (goods == null) {
            finish();
        }
        tvTitleName.setText(goods.getGoodsname());
        tvPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        if ("1".equals(goods.getIs_real())) {
            ivGoodsImg.setVisibility(View.GONE);
        }else{
            ViewGroup.LayoutParams layoutParams = ivGoodsImg.getLayoutParams();
            layoutParams.height= BaseAppUtil.getDeviceWidth(mContext)/2;
            ivGoodsImg.setLayoutParams(layoutParams);
        }
        GlideDisplay.dispalyWithFitCenterDef(ivGoodsImg, goods.getOriginal_img(), R.mipmap.ic_launcher);
        tvPrice.setText("市场价：" + goods.getMarket_price() + "元");
        tvGoodsName.setText(goods.getGoodsname());
        tvGoodsIntro.setText(goods.getGoods_intro());
        if (!TextUtils.isEmpty(goods.getGoods_content())) {
            AppApi.loadMobileHtmlContent(webViewContent, goods.getGoods_content());
        }
        tvExchangeScore.setText(goods.getIntegral() + "积分");
        tvTitleName.setText(goods.getGoodsname());
        int total = goods.getTotal();
        int remain = goods.getRemain();
        int progress = 100;
        if (total != 0) {
            progress = (int) (remain * 100. / total);
        }
        pbProgressBar.setProgress(progress);
        tvProgress.setText("剩余：" + progress + "%");
        tvMyScore.setText(goods.getMyintegral());
        try {
            if (Long.parseLong(goods.getMyintegral()) < Long.parseLong(goods.getIntegral())) {
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

    private void getAddressInfo() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserAddressInfo>(mActivity, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserAddressInfo data) {
                if (data == null || TextUtils.isEmpty(data.getTopaddress())) {
                    isAddressOk = false;
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.addressDetailApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }


    public static void start(Context context, String goodsDetailJson) {
        Intent starter = new Intent(context, EntityDetailActivity.class);
        starter.putExtra("goodsDetailJson", goodsDetailJson);
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
                if ("2".equals(goods.getIs_real()) && !isAddressOk) {//实物判断地址是否设置了
                    hintRequestAddress();
                    return;
                }
                try {
                    if (Long.parseLong(goods.getMyintegral()) < Long.parseLong(goods.getIntegral())) {
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

    private void hintRequestAddress() {
        new HintDialogUtil().showHintDialog(this, "温馨提示", "兑换实物时，需要填写地址哦，现在去填写?", "去填写", "取消", new HintDialogUtil.HintDialogListener() {
            @Override
            public void ok(String content) {
                UpdateAddressActivity.start(mContext);
            }

            @Override
            public void cancel() {
            }
        });
    }

    public void applyCoupon() {
        final ApplyGoodsRequestBean applyGoodsRequestBean = new ApplyGoodsRequestBean();
        applyGoodsRequestBean.setGoodsid(goods.getGoodsid());
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(applyGoodsRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<Goods>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(Goods data) {
                if (data != null) {
                    if ("1".equals(goods.getIs_real())) {
                        new EntityExchangeDialogUtil().showExchangeDialog(mContext, "兑换成功，请在我的已领取礼品卡中查看卡密！");
                    } else {
                        new EntityExchangeDialogUtil().showExchangeDialog(mContext, "兑换成功，请在我的已领取实物中查看！");
                    }
                    updateGoodsData(goods);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.userGoodsAddApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    private void getMyScore() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserInfoResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                if (data != null) {
                    goods.setMyintegral(data.getMyintegral());
                    updateGoodsData(goods);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userIntegralApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

}
