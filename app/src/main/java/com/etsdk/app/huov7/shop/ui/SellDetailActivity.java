package com.etsdk.app.huov7.shop.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.NotifyShareOkRequestBean;
import com.etsdk.app.huov7.model.ShareResultBean;
import com.etsdk.app.huov7.pay.ChargeActivityForWap;
import com.etsdk.app.huov7.sharesdk.ShareDataEvent;
import com.etsdk.app.huov7.sharesdk.ShareUtil;
import com.etsdk.app.huov7.shop.adapter.SellScreenshortAdapter;
import com.etsdk.app.huov7.shop.model.AccountOperationFavorRequestBean;
import com.etsdk.app.huov7.shop.model.ResultBean;
import com.etsdk.app.huov7.shop.model.SellCancelRequestBean;
import com.etsdk.app.huov7.shop.model.SellOptionEvent;
import com.etsdk.app.huov7.shop.model.ShareOptionEvent;
import com.etsdk.app.huov7.shop.model.ShopAccountDetailBean;
import com.etsdk.app.huov7.shop.model.ShopListRefreshEvent;
import com.etsdk.app.huov7.ui.ServiceActivity;
import com.etsdk.app.huov7.view.LoadStatusView;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.L;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.log.T;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.liang530.utils.BaseAppUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 交易角色详情
 */
public class SellDetailActivity extends ImmerseActivity {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.sellgameitem)
    SellGameItem sellgameitem;
    @BindView(R.id.tv_create_time)
    TextView tvCreateTime;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.rcy_screenshot)
    RecyclerView rcyScreenshot;
    @BindView(R.id.tv_favor)
    TextView tvFavor;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.tv_service)
    TextView tvService;
    @BindView(R.id.ll_option_buy)
    LinearLayout llOptionBuy;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_more_option)
    TextView tvMoreOption;
    @BindView(R.id.ll_option_sell)
    LinearLayout llOptionSell;
    @BindView(R.id.iv_title_down)
    ImageView ivTitleDown;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.loadview)
    LoadStatusView loadview;
    @BindView(R.id.tv_account_id)
    TextView tvAccountId;
    @BindView(R.id.tv_verify_info)
    TextView tvVerifyInfo;
    @BindView(R.id.tv_description)
    TextView tvDesc;
    @BindView(R.id.iv_more_option)
    ImageView ivMoreOption;

    private ArrayList<String> imageList = new ArrayList<>();
    private SellScreenshortAdapter sellScreenshortAdapter;
    ShopAccountDetailBean.DataBean data;
    private boolean isFavor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setupUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void setupUI() {
        tvTitleName.setText("角色详情");
//        ivTitleDown.setVisibility(View.VISIBLE);
//        ivTitleDown.setImageResource(R.mipmap.huosdk_share);
        scrollView.post(new Runnable() {//滑到顶部，不然会滑到截图recyclerview那里
            @Override
            public void run() {
                if (scrollView != null) {
                    scrollView.scrollTo(0, 0);
                }
            }
        });
        rcyScreenshot.setLayoutManager(new GridLayoutManager(mContext, 4));
        sellScreenshortAdapter = new SellScreenshortAdapter(imageList);
        rcyScreenshot.setAdapter(sellScreenshortAdapter);
        getDetailInfo();
        loadview.setOnLoadRefreshListener(new LoadStatusView.OnLoadRefreshListener() {
            @Override
            public void onLoadRefresh() {
                getDetailInfo();
            }
        });
    }

    public void getDetailInfo() {
        loadview.showLoading();
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.dealAccountRead);
        httpParams.put("id",getIntent().getIntExtra("id", 0));
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.dealAccountRead),new HttpJsonCallBackDialog<ShopAccountDetailBean>(){
            @Override
            public void onDataSuccess(ShopAccountDetailBean data) {
                updateInfo(data.getData());
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                updateInfo(null);
            }
        });
    }

    public void setFavor(final boolean isFavor){
        AccountOperationFavorRequestBean requestBean = new AccountOperationFavorRequestBean();
        requestBean.setId(getIntent().getIntExtra("id", 0)+"");
        requestBean.setOperation(isFavor?"2":"1");//1收藏，2取消
//        requestBean.setOperation("1");//1收藏，2取消
        final int imaginId = isFavor? R.mipmap.huosdk_shoucang: R.mipmap.huosdk_shoucang2;//成功后的显示，与当前相反
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<ResultBean>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(final ResultBean data) {
            }

            @Override
            public void onDataSuccess(ResultBean data, String code, String msg) {
                if("200".equals(code)){
                    Drawable drawable = mContext.getResources().getDrawable(imaginId);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
                    tvFavor.setCompoundDrawables(null, drawable, null, null);
                    SellDetailActivity.this.isFavor = !SellDetailActivity.this.isFavor;
                    EventBus.getDefault().post(new ShopListRefreshEvent());
                }else{
                    T.s(mContext, "操作失败 "+msg);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                L.e(TAG, code + " " + msg);
                T.s(mContext, "操作失败 "+msg);
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.dealAccountOperationCollect), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    private void cancelSell(final Context context, int id){
        SellCancelRequestBean requestBean = new SellCancelRequestBean();
        requestBean.setId(id+"");
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<ResultBean>(context, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(final ResultBean data) {
            }

            @Override
            public void onDataSuccess(ResultBean data, String code, String msg) {
                if("200".equals(code)){
                    T.s(context, "已取消出售");
                    EventBus.getDefault().post(new ShopListRefreshEvent());
                    finish();
                }else{
                    T.s(context, "操作失败 "+msg);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                T.s(context, "操作失败 "+msg);
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.dealAccountCancel), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    public void updateInfo(ShopAccountDetailBean.DataBean data) {
        if (data == null) {
            loadview.showFail();
            return;
        }
        this.data = data;
        loadview.showSuccess();
        tvAccountId.setText("小号：" + data.getNickname());
        if (data.getStatus() == SellOptionEvent.STATUS_ON) {
            tvVerifyInfo.setText("信息通过官方审核");
            tvVerifyInfo.setVisibility(View.VISIBLE);
            String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date(data.getCreate_time() * 1000L));
            tvCreateTime.setText("上架时间：" + time);
        } else {
            tvCreateTime.setText("");
        }
        tvMoney.setText("￥" + data.getTotal_price());
        tvBuy.setText("立即购买 "+data.getTotal_price()+"元");
        tvDesc.setText(data.getDescription());
        if(data.getImage()!=null) {
            imageList.addAll(data.getImage());
        }
        sellScreenshortAdapter.notifyDataSetChanged();
        if(data.getIs_self()==1){//不是自己发布
            llOptionBuy.setVisibility(View.VISIBLE);
            llOptionSell.setVisibility(View.GONE);
        }else if(data.getIs_self()==2){//是自己发布
            llOptionBuy.setVisibility(View.GONE);
            llOptionSell.setVisibility(View.VISIBLE);
        }
        if(data.getOrder()!=null && (data.getOrder().getIs_buy()==2 || data.getOrder().getIs_sell()==2)){
            llOptionBuy.setVisibility(View.VISIBLE);
            llOptionSell.setVisibility(View.GONE);
            tvBuy.setText("已交易");
            tvBuy.setBackgroundResource(R.drawable.shape_circle_rect_gray_dark_5);
            tvBuy.setClickable(false);
        }
        if (data.getStatus() == SellOptionEvent.STATUS_VARIFY) {
            tvState.setText("审核中");
        } else if (data.getStatus() == SellOptionEvent.STATUS_OFF) {
            tvState.setText("已下架");
        } else if (data.getStatus() == SellOptionEvent.STATUS_SOLD) {
            tvState.setText("已出售");
        } else if (data.getStatus() == SellOptionEvent.STATUS_VARIFY_FAIL) {
            tvState.setText("审核失败");
        } else if (data.getStatus() == SellOptionEvent.STATUS_ON){
            tvState.setText("审核通过");
        }
        int imaginId = data.getIs_collect()==2? R.mipmap.huosdk_shoucang2: R.mipmap.huosdk_shoucang;
        isFavor = data.getIs_collect()==2?true:false;
        Drawable drawable = mContext.getResources().getDrawable(imaginId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
        tvFavor.setCompoundDrawables(null, drawable, null, null);
        tvDesc.setText(data.getDescription());
        GameBean gameBean = new GameBean();
        gameBean.setGameid(data.getGameid()+"");
        gameBean.setGamename(data.getGamename());
        gameBean.setOneword(data.getTitle());
        gameBean.setIcon(data.getIcon());
        sellgameitem.setGameBean(gameBean, data.getServername());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSellOptionevent(SellOptionEvent event) {
        switch (event.type) {
            case SellOptionEvent.OPTION_CANCEL_SELL:
                new AlertDialog.Builder(mContext).setTitle("取消出售").setMessage("取消出售后，小号将转回到您的账号。如登录未见小号，请点悬浮球切换。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelSell(mContext, data.getId());
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                break;
            case SellOptionEvent.OPTION_DELETE_SELL:
                T.s(this, "删除卖号");
                break;
            case SellOptionEvent.OPTION_EDIT_SELL:
                SellEditActivity.start(mContext, data.getId());
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShareOptionevent(ShareOptionEvent event) {
        switch (event.type) {
            case ShareOptionEvent.WEIBO:
//                T.s(this, "微博");
                showShare(SinaWeibo.NAME);
                break;
            case ShareOptionEvent.WEIXIN:
//                T.s(this, "微信");
                showShare(Wechat.NAME);
                break;
            case ShareOptionEvent.PENGYOUQUAN:
//                T.s(this, "朋友圈");
                showShare(WechatMoments.NAME);
                break;
            case ShareOptionEvent.QQ:
//                T.s(this, "QQ");
                showShare(QQ.NAME);
                break;
            case ShareOptionEvent.QQKONGJIAN:
//                T.s(this, "QQ空间");
                showShare(QZone.NAME);
                break;
            case ShareOptionEvent.COPY:
                BaseAppUtil.copyToSystem(mContext, AppApi.getUrl(AppApi.shareRead));
                T.s(mContext, "链接已复制到剪贴板");
                break;
        }
    }

    private void showShare(String platform) {
        if(data == null){
            T.s(mContext, "暂未获取到分享信息，请稍后再试");
            return;
        }
        ShareDataEvent event = new ShareDataEvent();
        event.text = data.getDescription();
        event.title = data.getGamename();
        event.titleUrl = AppApi.getUrl(AppApi.shareRead);
        event.url = AppApi.getUrl(AppApi.shareRead);
        event.siteUrl = AppApi.getUrl(AppApi.shareRead);
        event.platform = platform;
        if(TextUtils.isEmpty(data.getIcon())){
            event.resouceId = R.mipmap.ic_launcher;
        }else{
            event.imageURL = data.getIcon();
        }
        ShareUtil.setCurrentShareTitle("分享");
        new ShareUtil().oneKeyShare(getApplicationContext(), event, new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                L.e(TAG, "分享成功！：" + hashMap);
//                notifyShareOk(shareResult.getShareid());
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                L.e(TAG, "失败！");
                T.s(mContext, "分享失败 "+i);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                L.e(TAG, "取消！");
                T.s(mContext, "取消分享");
            }
        });
    }

    private void notifyShareOk(String shareid) {
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

    public static void start(Context context, int id) {
        Intent starter = new Intent(context, SellDetailActivity.class);
        starter.putExtra("id", id);
        context.startActivity(starter);
    }

    @OnClick({R.id.iv_titleLeft, R.id.iv_title_down, R.id.tv_buy, R.id.tv_more_option, R.id.iv_more_option,
            R.id.tv_service, R.id.tv_favor})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.iv_title_down:
                new ShareOptionDialogUtil().show(this);
                break;
            case R.id.tv_buy:
                new BuyHintDialogUtil().show(mContext, new BuyHintDialogUtil.Listener() {
                    @Override
                    public void ok() {
                        ChargeActivityForWap.start(mContext, AppApi.getUrl(AppApi.accountBuy), "确认订单", getIntent().getIntExtra("id", 0));
                    }

                    @Override
                    public void cancel() {

                    }
                });
                break;
            case R.id.tv_more_option:
            case R.id.iv_more_option:
                new SellOptionDialogUtil().show(this, data.getStatus());
                break;
            case R.id.tv_service:
                ServiceActivity.start(mContext);
                break;
            case R.id.tv_favor:
                setFavor(isFavor);
                break;
        }
    }
}
