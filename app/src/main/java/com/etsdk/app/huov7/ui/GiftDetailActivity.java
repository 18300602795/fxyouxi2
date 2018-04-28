package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.ApplyGiftRequestBean;
import com.etsdk.app.huov7.model.GiftDetail;
import com.etsdk.app.huov7.model.GiftListItem;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.etsdk.app.huov7.ui.dialog.DialogGiftUtil;
import com.etsdk.app.huov7.util.StringUtils;
import com.etsdk.app.huov7.view.LoadStatusView;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.T;
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

/**
 * 礼包详情
 * Created by admin on 2016/12/10.
 */
public class GiftDetailActivity extends ImmerseActivity implements View.OnClickListener {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.iv_gift_image)
    ImageView ivGiftImage;
    @BindView(R.id.tv_gift_title)
    TextView tvGiftTitle;
    @BindView(R.id.pb_gift_progressBar)
    ProgressBar pbGiftProgressBar;
    @BindView(R.id.tv_gift_surplus)
    TextView tvGiftSurplus;
    @BindView(R.id.tv_gift_code)
    TextView tvGiftCode;
    @BindView(R.id.tv_gift_get)
    TextView tvGiftGet;
    @BindView(R.id.tv_gift_content)
    TextView tvGiftContent;
    @BindView(R.id.tv_gift_date)
    TextView tvGiftDate;
    @BindView(R.id.loadview)
    LoadStatusView loadview;
    @BindView(R.id.tv_gift_desc)
    TextView tvGiftDesc;
    private String giftId;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gift_detail_activity);
        ButterKnife.bind(this);
        setupUI();

    }

    private void setupUI() {
        tvTitleName.setText("礼包详情");
        Intent intent = getIntent();
        giftId = intent.getStringExtra("giftId");
        loadview.showLoading();
        loadview.setOnLoadRefreshListener(new LoadStatusView.OnLoadRefreshListener() {
            @Override
            public void onLoadRefresh() {
                getGiftData();
            }
        });
        getGiftData();

    }

    private void getGiftData() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.giftDetailApi);
        httpParams.put("giftid", giftId);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.giftDetailApi), new HttpJsonCallBackDialog<GiftDetail>() {
            @Override
            public void onDataSuccess(GiftDetail data) {
                if (data != null && data.getData() != null) {
                    updateGiftData(data.getData());
                }
                loadview.showSuccess();
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

    private void updateGiftData(GiftListItem giftListItem) {
        tvGiftTitle.setText(giftListItem.getGiftname());
        tvGiftContent.setText(giftListItem.getContent());
        String dateTime = null;
        try {
            dateTime = simpleDateFormat.format(new Date(Long.parseLong(giftListItem.getStarttime()) * 1000));
            dateTime += "至" + simpleDateFormat.format(new Date(Long.parseLong(giftListItem.getEnttime()) * 1000));
            tvGiftDate.setText(dateTime);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            tvGiftDate.setText("");
        }

//        GlideDisplay.display(ivGiftImage, giftListItem.getIcon(), R.mipmap.ic_launcher);
        Glide.with(GiftDetailActivity.this).load(giftListItem.getIcon()).placeholder(R.mipmap.ic_launcher).into(ivGiftImage);
        int total = giftListItem.getTotal();
        int remain = giftListItem.getRemain();
        int progress = 100;
        if (total != 0) {
            progress = (int) (remain * 100. / total);
        }
        pbGiftProgressBar.setProgress(progress);
        tvGiftSurplus.setText("剩余：" + progress + "%");
        if (!TextUtils.isEmpty(giftListItem.getGiftcode())) {
            tvGiftCode.setText(giftListItem.getGiftcode());
            tvGiftGet.setText("复制");
            tvGiftCode.setTag(giftListItem.getGiftcode());
        }
        tvGiftDesc.setText(giftListItem.getFunc());
    }

    public static void start(Context context, String giftId) {
        Intent starter = new Intent(context, GiftDetailActivity.class);
        starter.putExtra("giftId", giftId);
        context.startActivity(starter);
    }


    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight, R.id.tv_gift_get})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
            case R.id.tv_gift_get:
                if (tvGiftCode.getTag() == null) {
//                    applyGift();
                    getUserInfoData(GiftDetailActivity.this);
                } else {
                    BaseAppUtil.copyToSystem(v.getContext(), (String) tvGiftCode.getTag());
                    T.s(v.getContext(), "复制成功");
                }
                break;
        }
    }

    private void getUserInfoData(final Context context) {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserInfoResultBean>(context, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                if (data != null) {
                    if (!StringUtils.isEmpty(data.getMobile())){
                        applyGift();
                    }else {
                        T.s(context, "请先去绑定手机");
                        BindPhoneActivity.start(context);
                    }
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userDetailApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }
    public void applyGift() {
        final ApplyGiftRequestBean applyGiftRequestBean = new ApplyGiftRequestBean();
        applyGiftRequestBean.setGiftid(giftId);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(applyGiftRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<GiftListItem>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(GiftListItem data) {
                if (data != null) {
                    new DialogGiftUtil().showConvertDialog(mActivity, data.getGiftcode(),data.getGameid(), null);
                    updateGiftData(data);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.userGiftAddApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

}
