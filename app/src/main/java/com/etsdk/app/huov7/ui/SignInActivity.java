package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.SignInAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.AdImage;
import com.etsdk.app.huov7.model.SignInAd;
import com.etsdk.app.huov7.model.SignInResultBean;
import com.etsdk.app.huov7.model.UserSignRequestBean;
import com.etsdk.app.huov7.model.UserSignResultBean;
import com.etsdk.app.huov7.ui.dialog.SignInOkDialogUtil;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.log.T;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.liang530.utils.BaseAppUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends ImmerseActivity {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.iv_signTop)
    ImageView ivSignTop;
    @BindView(R.id.tv_sign_count)
    TextView tvSignCount;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.activity_sign_in)
    LinearLayout activitySignIn;
    @BindView(R.id.tv_sign_desc)
    TextView tvSignDesc;
    private List<SignInResultBean.SignData> datas = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        recyclerview.setLayoutManager(new GridLayoutManager(mContext, 7));
        recyclerview.setAdapter(new SignInAdapter(datas));
        setAdImageHeight(ivSignTop);
        getSignData();
//        getSignTopAdImage();
    }
    private void setAdImageHeight(ImageView imageView){
        int height = (int) (BaseAppUtil.getDeviceWidth(mContext) * AppApi.AD_IMAGE_HW_RATA);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        } else {
            layoutParams.height = height;
        }
        imageView.setLayoutParams(layoutParams);
    }
    private void getSignTopAdImage() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.slideListApi);
        httpParams.put("type", "signtopper");
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).setShowErrorToast(false).get(AppApi.getUrl(AppApi.slideListApi), new HttpJsonCallBackDialog<SignInAd>() {
            @Override
            public void onDataSuccess(SignInAd data) {
                if (data != null && data.getData() != null
                        &&data.getData().getSigntopper()!=null
                        &&data.getData().getSigntopper().getList()!=null
                        &&data.getData().getSigntopper().getList().size()>0) {
                    final AdImage adImage = data.getData().getSigntopper().getList().get(0);
//                    GlideDisplay.display(ivSignTop,adImage.getImage(),R.mipmap.gg);
                    Glide.with(SignInActivity.this).load(adImage.getImage()).placeholder(R.mipmap.gg).into(ivSignTop);
                    ivSignTop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if("1".equals(adImage.getType())){
                                WebViewActivity.start(mContext,"",adImage.getUrl());
                            }else if("2".equals(adImage.getType())){
                                GameDetailV2Activity.start(mContext,adImage.getTarget()+"");
                            }else if("3".equals(adImage.getType())){
                                GiftDetailActivity.start(mContext,adImage.getTarget()+"");
                            }else if("4".equals(adImage.getType())){
                                CouponDetailActivity.start(mContext,adImage.getTarget()+"");
                            }
                        }
                    });
                }
            }
        });
    }


    private void getSignData() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<SignInResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(SignInResultBean data) {
                if (data != null) {
                    updateSignData(data);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.userSignApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }
    private void updateSignData(SignInResultBean signInResultBean) {
        if (signInResultBean.getList() != null) {
            datas.clear();
            datas.addAll(signInResultBean.getList());
            recyclerview.getAdapter().notifyDataSetChanged();
        }
        tvSignCount.setText("本月已累计签到" + signInResultBean.getSigndays() + "次");
        tvSignDesc.setText(signInResultBean.getSigndesc());
    }
    public void userSign(final SignInResultBean.SignData signday, final String signScore){
        final UserSignRequestBean userSignRequestBean = new UserSignRequestBean();
        userSignRequestBean.setSignday(signday.getDay());
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(userSignRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserSignResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserSignResultBean data) {
            }

            @Override
            public void onDataSuccess(UserSignResultBean data, String code, String msg) {
                if (data != null) {
                    if("2".equals(data.getStatus())){
                        signday.setSigned("2");
                        recyclerview.getAdapter().notifyDataSetChanged();
                        SignInOkDialogUtil.showDialog(mContext,signScore);
                    }else if("1".equals(data.getStatus())){
                        T.s(mActivity,"签到失败");
                    }
                }else{
                    T.s(mActivity,msg);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.userSignAddApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }
    public static void start(Context context) {
        Intent starter = new Intent(context, SignInActivity.class);
        context.startActivity(starter);
    }
    public static Intent getIntent(Context context) {
        Intent starter = new Intent(context, SignInActivity.class);
        return starter;
    }

    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight, R.id.btn_submit, R.id.iv_signTop})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
            case R.id.btn_submit:
                break;
            case R.id.iv_signTop:
//                GameDetailActivity.start(mContext);
                break;
        }
    }
}
