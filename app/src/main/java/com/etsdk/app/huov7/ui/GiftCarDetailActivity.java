package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 礼包详情
 * Created by admin on 2016/12/10.
 */
public class GiftCarDetailActivity extends ImmerseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gift_detail_activity);
        ButterKnife.bind(this);
        setupUI();

    }

    private void setupUI() {

    }

//    private void getGiftData() {
//        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.giftDetailApi);
//        httpParams.put("giftid", giftId);
//        //成功，失败，null数据
//        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.giftDetailApi), new HttpJsonCallBackDialog<GiftDetail>() {
//            @Override
//            public void onDataSuccess(GiftDetail data) {
//                if (data != null && data.getData() != null) {
//                    updateGiftData(data.getData());
//                }
//                loadview.showSuccess();
//            }
//
//            @Override
//            public void onJsonSuccess(int code, String msg, String data) {
//                super.onJsonSuccess(code, msg, data);
//                loadview.showFail();
//            }
//
//            @Override
//            public void onFailure(int errorNo, String strMsg, String completionInfo) {
//                super.onFailure(errorNo, strMsg, completionInfo);
//                loadview.showFail();
//            }
//        });
//    }

//    private void updateGiftData(GiftListItem giftListItem) {
//        tvGiftTitle.setText(giftListItem.getGiftname());
//        tvGiftContent.setText(giftListItem.getContent());
//        String dateTime = null;
//        try {
//            dateTime = simpleDateFormat.format(new Date(Long.parseLong(giftListItem.getStarttime()) * 1000));
//            dateTime += "至" + simpleDateFormat.format(new Date(Long.parseLong(giftListItem.getEnttime()) * 1000));
//            tvGiftDate.setText(dateTime);
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//            tvGiftDate.setText("");
//        }
//
//        GlideDisplay.dispalyWithFitCenter(ivGiftImage, giftListItem.getIcon());
//        int total = giftListItem.getTotal();
//        int remain = giftListItem.getRemain();
//        int progress = 100;
//        if (total != 0) {
//            progress = (int) (remain * 100. / total);
//        }
//        pbGiftProgressBar.setProgress(progress);
//        tvGiftSurplus.setText("剩余：" + progress + "%");
//        if (!TextUtils.isEmpty(giftListItem.getGiftcode())) {
//            tvGiftCode.setText(giftListItem.getGiftcode());
//            tvGiftGet.setText("复制");
//            tvGiftCode.setTag(giftListItem.getGiftcode());
//        }
//
//    }

    public static void start(Context context, String goodsId) {
        Intent starter = new Intent(context, GiftCarDetailActivity.class);
        starter.putExtra("goodsId", goodsId);
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

                break;
        }
    }

//    public void applyGift() {
//        final ApplyGiftRequestBean applyGiftRequestBean = new ApplyGiftRequestBean();
//        applyGiftRequestBean.setGiftid(giftId);
//        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(applyGiftRequestBean));
//        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<GiftListItem>(this, httpParamsBuild.getAuthkey()) {
//            @Override
//            public void onDataSuccess(GiftListItem data) {
//                if (data != null) {
//                    new DialogGiftUtil().showConvertDialog(mActivity, data.getGiftcode(), null);
//                    updateGiftData(data);
//                }
//            }
//        };
//        httpCallbackDecode.setShowTs(true);
//        httpCallbackDecode.setLoadingCancel(false);
//        httpCallbackDecode.setShowLoading(true);
//        RxVolley.post(AppApi.getUrl(AppApi.userGiftAddApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
//    }

}
