package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.AdImage;
import com.etsdk.app.huov7.model.CouponBeanList;
import com.etsdk.app.huov7.model.CouponListItem;
import com.etsdk.app.huov7.model.GiftCard;
import com.etsdk.app.huov7.model.GiftCardBeanList;
import com.etsdk.app.huov7.model.Goods;
import com.etsdk.app.huov7.model.GoodsBeanList;
import com.etsdk.app.huov7.model.ScoreShopAd;
import com.etsdk.app.huov7.model.ScoreShopOption;
import com.etsdk.app.huov7.model.SplitLine;
import com.etsdk.app.huov7.model.TjColumnHead;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.etsdk.app.huov7.provider.AdImageViewProvider;
import com.etsdk.app.huov7.provider.CouponListItemViewProvider;
import com.etsdk.app.huov7.provider.EntityGoodGrid;
import com.etsdk.app.huov7.provider.EntityGoodGridViewProvider;
import com.etsdk.app.huov7.provider.GiftCardViewProvider;
import com.etsdk.app.huov7.provider.ScoreShopOptionViewProvider;
import com.etsdk.app.huov7.provider.SplitLineViewProvider;
import com.etsdk.app.huov7.provider.TjColumnHeadViewProvider;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class NewScoreShopActivity extends ImmerseActivity implements AdvRefreshListener {


    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    private Items items=new Items();
    private MultiTypeAdapter multiTypeAdapter;
    BaseRefreshLayout baseRefreshLayout;
    private ScoreShopAd.DataBean scoreShopAd;
    private List<CouponListItem> couponListItems=new ArrayList();
    private List<GiftCard> giftCardList=new ArrayList();
    private List<Goods> goodsList=new ArrayList();
    private ScoreShopOption scoreShopOption = new ScoreShopOption();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_score_shop);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        setupUI();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(Boolean isLogin) {
        if (isLogin){
            baseRefreshLayout.refresh();
        }else {
            finish();
        }
    }


    private void setupUI() {
        tvTitleName.setText("积分商城");
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                if(items.get(position) instanceof Goods){
                    if(items.get(position) instanceof GiftCard){
                        return 2;
                    }else {
                        return 1;
                    }
                }
                return 2;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(CouponListItem.class, new CouponListItemViewProvider());
        multiTypeAdapter.register(SplitLine.class, new SplitLineViewProvider());
        multiTypeAdapter.register(AdImage.class, new AdImageViewProvider());
        multiTypeAdapter.register(AdImage.class, new AdImageViewProvider());
        TjColumnHeadViewProvider tjColumnHeadViewProvider = new TjColumnHeadViewProvider();
        tjColumnHeadViewProvider.setRequestBackGroundColor(true);//设置需要背景颜色
        multiTypeAdapter.register(TjColumnHead.class,tjColumnHeadViewProvider );
        multiTypeAdapter.register(GiftCard.class, new GiftCardViewProvider());
//        multiTypeAdapter.register(Goods.class, new EntityGridViewProvider());
        multiTypeAdapter.register(EntityGoodGrid.class, new EntityGoodGridViewProvider());
        multiTypeAdapter.register(ScoreShopOption.class, new ScoreShopOptionViewProvider());
        // 设置适配器
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
//        // 加载数据
        baseRefreshLayout.refresh();
    }

    @Override
    public void getPageData(int i) {
        getCoupon();
        getGiftCard();
        getGoodsListByNet();
        getScoreShopAdData();
        getMyScore();
    }

    private void getMyScore() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserInfoResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                if (data != null ) {
                    scoreShopOption.setMyintegral(data.getMyintegral());
                    multiTypeAdapter.notifyDataSetChanged();
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userIntegralApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);


    }

    private void getCoupon() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.couponListApi);
        httpParams.put("page",1);
        httpParams.put("offset",5);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.couponListApi),new HttpJsonCallBackDialog<CouponBeanList>(){
            @Override
            public void onDataSuccess(CouponBeanList data) {
                couponListItems.clear();
                if(data!=null&&data.getData()!=null&&data.getData().getList()!=null){
                    couponListItems.addAll(data.getData().getList());
                }
                updateListData();
            }
            @Override
            public void onJsonSuccess(int code, String msg,String data) {
                updateListData();
            }
            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                updateListData();
            }
        });
    }

    private void getGiftCard() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.goodsListApi);
        httpParams.put("is_real","1");
        httpParams.put("page",1);
        httpParams.put("offset",5);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.goodsListApi),new HttpJsonCallBackDialog<GiftCardBeanList>(){
            @Override
            public void onDataSuccess(GiftCardBeanList data) {
                giftCardList.clear();
                if(data!=null&&data.getData()!=null&&data.getData().getList()!=null){
                    giftCardList.addAll(data.getData().getList());
                }
                updateListData();
            }
            @Override
            public void onJsonSuccess(int code, String msg,String data) {
                updateListData();
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                updateListData();
            }
        });
    }
    private void getGoodsListByNet() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.goodsListApi);
        httpParams.put("is_real","2");
        httpParams.put("page",1);
        httpParams.put("offset",5);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.goodsListApi),new HttpJsonCallBackDialog<GoodsBeanList>(){
            @Override
            public void onDataSuccess(GoodsBeanList data) {
                goodsList.clear();
                if(data!=null&&data.getData()!=null&&data.getData().getList()!=null){
                    goodsList.addAll(data.getData().getList());
                }
                updateListData();
            }
            @Override
            public void onJsonSuccess(int code, String msg,String data) {
                updateListData();
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                updateListData();
            }
        });
    }
    private void getScoreShopAdData() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.slideListApi);
        httpParams.put("type", "shop");
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).setShowErrorToast(false).get(AppApi.getUrl(AppApi.slideListApi), new HttpJsonCallBackDialog<ScoreShopAd>() {
            @Override
            public void onDataSuccess(ScoreShopAd data) {
                if (data != null&& data.getData()!=null) {
                    scoreShopAd=data.getData();
                    updateListData();
                }
            }
        });
    }
    public List<Goods> getGoodsList(){
        return goodsList;
    }

    private synchronized void updateListData() {
        Items newItems=new Items();
        if(scoreShopAd!=null&&scoreShopAd.getShoptopper()!=null
                &&scoreShopAd.getShoptopper().getList()!=null&&scoreShopAd.getShoptopper().getList().size()>0){
            AdImage adImage = scoreShopAd.getShoptopper().getList().get(0);
            adImage.setRequestPadding(false);
            newItems.add(adImage);
        }
        newItems.add(scoreShopOption);
        if(couponListItems.size()>0){
            newItems.add(new TjColumnHead(TjColumnHead.TYPE_SCORE_COUPONE));
            newItems.addAll(couponListItems.subList(0,couponListItems.size()>5?5:couponListItems.size()));
//            for(CouponListItem couponListItem:couponListItems){
//                newItems.add(couponListItem);
//            }
        }
        if(scoreShopAd!=null&&scoreShopAd.getShopcoupon()!=null
                &&scoreShopAd.getShopcoupon().getList()!=null&&scoreShopAd.getShopcoupon().getList().size()>0){
            AdImage adImage = scoreShopAd.getShopcoupon().getList().get(0);
            adImage.setRequestPadding(true);
            newItems.add(adImage);
        }
        if(giftCardList.size()>0){
            newItems.add(new TjColumnHead(TjColumnHead.TYPE_SCORE_GIFT_CARD));
            for(GiftCard giftCard:giftCardList){
                newItems.add(giftCard);
            }
        }
        if(scoreShopAd!=null&&scoreShopAd.getShopgiftcard()!=null
                &&scoreShopAd.getShopgiftcard().getList()!=null&&scoreShopAd.getShopgiftcard().getList().size()>0){
            AdImage adImage = scoreShopAd.getShopgiftcard().getList().get(0);
            adImage.setRequestPadding(true);
            newItems.add(adImage);
        }
        if(goodsList.size()>0){
            newItems.add(new TjColumnHead(TjColumnHead.TYPE_SCORE_GOODS));
            newItems.add(new EntityGoodGrid(goodsList));
        }
        items.clear();
        baseRefreshLayout.resultLoadData(items,newItems,1);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, NewScoreShopActivity.class);
        context.startActivity(starter);
    }

    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
        }
    }
}
