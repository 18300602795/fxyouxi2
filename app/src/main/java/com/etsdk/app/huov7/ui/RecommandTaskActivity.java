package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.AdImage;
import com.etsdk.app.huov7.model.NotifyShareOkRequestBean;
import com.etsdk.app.huov7.model.ShareResultBean;
import com.etsdk.app.huov7.model.TgtopperAd;
import com.etsdk.app.huov7.model.TgyResultBean;
import com.etsdk.app.huov7.sharesdk.ShareDataEvent;
import com.etsdk.app.huov7.sharesdk.ShareUtil;
import com.etsdk.app.huov7.view.AdImageView;
import com.game.sdk.SdkConstant;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.log.L;
import com.liang530.log.T;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.liang530.utils.BaseAppUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created by liu hong liang on 2016/12/23.
 * 推广员界面
 */
public class RecommandTaskActivity extends ImmerseActivity {
    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.activity_recommand_task)
    LinearLayout activityRecommandTask;
    @BindView(R.id.iv_tgytop)
    AdImageView ivTgytop;
    @BindView(R.id.tv_rewardDesc)
    TextView tvRewardDesc;
    @BindView(R.id.tv_rewardScore)
    TextView tvRewardScore;
    @BindView(R.id.tv_inviteCount)
    TextView tvInviteCount;
    @BindView(R.id.btn_invite)
    Button btnInvite;
    @BindView(R.id.btn_award_record)
    Button btnAwardRecord;
    @BindView(R.id.tv_inviteDesc)
    TextView tvInviteDesc;
    private ShareResultBean.DateBean shareResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommand_task);
        ButterKnife.bind(this);
        setupUI();
    }
    private void setupUI() {
        tvTitleName.setText("推广员任务");
        setAdImageHeight(ivTgytop);
        getTgyTopperAd();
        getTGYData();
        getShareData();

    }



    private void updateTGYData(TgyResultBean tgyResultBean){
        tvRewardScore.setText(tgyResultBean.getTotalitg());
        tvInviteCount.setText(tgyResultBean.getUsercnt());
        tvInviteDesc.setText(tgyResultBean.getDisc());
    }
    private void getTGYData() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<TgyResultBean>(mActivity, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(TgyResultBean data) {
                if (data != null) {
                    updateTGYData(data);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.tguserDetailApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight, R.id.btn_award_record,R.id.btn_invite})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
            case R.id.btn_award_record:
                AwardRecordActivity.start(mContext);
                break;
            case R.id.btn_invite:
                if(shareResult==null){
                    getShareData();
                    T.s(mContext,"分享失败，请稍后再试");
                }else{
                    showShare();
                }
                break;
        }
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
    private void getTgyTopperAd() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.slideListApi);
        httpParams.put("type", "tgtopper");
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).setShowErrorToast(false).get(AppApi.getUrl(AppApi.slideListApi), new HttpJsonCallBackDialog<TgtopperAd>() {
            @Override
            public void onDataSuccess(TgtopperAd data) {
                if(data!=null&&data.getData()!=null
                        &&data.getData().getTgtopper()!=null
                        &&data.getData().getTgtopper().getList()!=null
                        &&data.getData().getTgtopper().getList().size()>0){
                    AdImage adImage = data.getData().getTgtopper().getList().get(0);
                    ivTgytop.setAdImage(adImage);
                }
            }
        });
    }
    private void notifyShareOk(String shareid){
        final NotifyShareOkRequestBean notifyShareOkRequestBean = new NotifyShareOkRequestBean();
        notifyShareOkRequestBean.setShareid(shareid);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(notifyShareOkRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<ShareResultBean>(mActivity, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(ShareResultBean data) {
            }
        };
        httpCallbackDecode.setShowTs(false);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.shareNotifyApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);

    }
    private void getShareData() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.shareDetailApi);
        httpParams.put("gameid",SdkConstant.HS_APPID);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.shareDetailApi),new HttpJsonCallBackDialog<ShareResultBean>(){
            @Override
            public void onDataSuccess(ShareResultBean data) {
                if(data!=null&&data.getData()!=null){
                    shareResult=data.getData();
                }
            }
        });
    }
    private void showShare(){
        ShareDataEvent event=new ShareDataEvent();
        event.text=shareResult.getSharetext();
        event.title=shareResult.getTitle();
        event.titleUrl=shareResult.getUrl();
        event.url=shareResult.getUrl();
        event.resouceId=R.mipmap.ic_launcher;
        ShareUtil.setCurrentShareTitle("邀请好友");
        new ShareUtil().oneKeyShare(getApplicationContext(),event, new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                L.e(TAG, "分享成功！：" + hashMap);
                notifyShareOk(shareResult.getShareid());
            }
            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                L.e(TAG, "失败！");
            }
            @Override
            public void onCancel(Platform platform, int i) {
                L.e(TAG, "取消！");
            }
        });
    }

   public static void start(Context context) {
       Intent starter = new Intent(context, RecommandTaskActivity.class);
       context.startActivity(starter);
   }
   public static Intent getIntent(Context context) {
       Intent starter = new Intent(context, RecommandTaskActivity.class);
       return starter;
   }
}
