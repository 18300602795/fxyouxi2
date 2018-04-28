package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.AdImage;
import com.etsdk.app.huov7.model.AdImageKeyBean;
import com.etsdk.app.huov7.model.CouponBeanList;
import com.etsdk.app.huov7.model.CouponListItem;
import com.etsdk.app.huov7.model.GiftBeanList;
import com.etsdk.app.huov7.model.GiftListItem;
import com.etsdk.app.huov7.model.MainFuliAd;
import com.etsdk.app.huov7.model.MessageEvent;
import com.etsdk.app.huov7.model.SplitLine;
import com.etsdk.app.huov7.model.TjColumnHead;
import com.etsdk.app.huov7.provider.AdImageViewProvider;
import com.etsdk.app.huov7.provider.CouponListItemViewProvider;
import com.etsdk.app.huov7.provider.FuliOptionColumn;
import com.etsdk.app.huov7.provider.FuliOptionColumnViewProvider;
import com.etsdk.app.huov7.provider.FuliTopAdViewProvider;
import com.etsdk.app.huov7.provider.GiftListItemViewProvider;
import com.etsdk.app.huov7.provider.SplitLineViewProvider;
import com.etsdk.app.huov7.provider.TjColumnHeadViewProvider;
import com.etsdk.app.huov7.ui.DownloadManagerActivity;
import com.etsdk.app.huov7.ui.MessageActivity;
import com.etsdk.app.huov7.ui.SearchActivity;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by liu hong liang on 2016/12/10.
 */

public class MainFuliFragment extends AutoLazyFragment implements AdvRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    BaseRefreshLayout baseRefreshLayout;
    @BindView(R.id.iv_gotoMsg)
    ImageView ivGotoMsg;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;
    private Items items = new Items();
    private MultiTypeAdapter multiTypeAdapter;
    private MainFuliAd.DataBean mainFuliAd;
    private List<GiftListItem> giftListItems = new ArrayList();
    private List<CouponListItem> couponListItems = new ArrayList();

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_main_fuli);
        EventBus.getDefault().register(this);
        setupUI();
    }

    private void setupUI() {
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        multiTypeAdapter = new MultiTypeAdapter(items);
        // 设置适配器
        multiTypeAdapter.register(AdImageKeyBean.class, new FuliTopAdViewProvider());
        multiTypeAdapter.register(AdImage.class, new AdImageViewProvider());
        multiTypeAdapter.register(TjColumnHead.class, new TjColumnHeadViewProvider());
        multiTypeAdapter.register(GiftListItem.class, new GiftListItemViewProvider(multiTypeAdapter));
        multiTypeAdapter.register(CouponListItem.class, new CouponListItemViewProvider());
        multiTypeAdapter.register(SplitLine.class, new SplitLineViewProvider());
        multiTypeAdapter.register(FuliOptionColumn.class, new FuliOptionColumnViewProvider());
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.refresh();
        MessageEvent messageEvent = EventBus.getDefault().getStickyEvent(MessageEvent.class);
        onMessageEvent(messageEvent);
    }

    @Override
    public void getPageData(int requestPageNo) {
        getMainFuliAdData();
        getGift();
        getCoupon();
    }

    private void getCoupon() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.couponListApi);
//        httpParams.put("limittime", "1");
        httpParams.put("isrcmd", "2");//推荐代金券
        httpParams.put("page", 1);
        httpParams.put("offset", 5);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.couponListApi), new HttpJsonCallBackDialog<CouponBeanList>() {
            @Override
            public void onDataSuccess(CouponBeanList data) {
                couponListItems.clear();
                if (data != null && data.getData() != null && data.getData().getList() != null) {
                    couponListItems.addAll(data.getData().getList());
                }
                updateListData();
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                updateListData();
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                updateListData();
            }
        });
    }

    private void getGift() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.giftListApi);
        httpParams.put("luxury", "2");
        httpParams.put("page", 1);
        httpParams.put("offset", 10);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.giftListApi), new HttpJsonCallBackDialog<GiftBeanList>() {
            @Override
            public void onDataSuccess(GiftBeanList data) {
                giftListItems.clear();
                if (data != null && data.getData() != null && data.getData().getList() != null) {
                    giftListItems.addAll(data.getData().getList());
                }
                updateListData();
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                updateListData();
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                updateListData();
            }
        });
    }

    private void getMainFuliAdData() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.slideListApi);
        httpParams.put("type", "weal");
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).setShowErrorToast(false).get(AppApi.getUrl(AppApi.slideListApi), new HttpJsonCallBackDialog<MainFuliAd>() {
            @Override
            public void onDataSuccess(MainFuliAd data) {
                if (data != null && data.getData() != null) {
                    mainFuliAd = data.getData();
                    updateListData();
                }
            }
        });
    }

    private void updateListData() {
        Items newItems = new Items();
        if (mainFuliAd != null) {
            if (mainFuliAd.getWealtopper() != null && mainFuliAd.getWealtopper().getList() != null) {
                newItems.add(mainFuliAd.getWealtopper());
            }
            newItems.add(new FuliOptionColumn());
            if (mainFuliAd.getWealtoday() != null && mainFuliAd.getWealtoday().getList() != null
                    && mainFuliAd.getWealtoday().getList().size() > 0) {
                newItems.add(mainFuliAd.getWealtoday().getList().get(0));
            }
            newItems.add(new SplitLine());
        } else {
            newItems.add(new FuliOptionColumn());
            newItems.add(new SplitLine());
        }
        boolean hasGiftColumn = false;
        if (giftListItems.size() > 0) {
            newItems.add(new TjColumnHead(TjColumnHead.TYPE_LUXURY_GIFT));
            for (GiftListItem giftListItem : giftListItems) {
                newItems.add(giftListItem);
            }
            hasGiftColumn = true;
        }
        if (mainFuliAd != null && mainFuliAd.getWealluxurygift() != null && mainFuliAd.getWealluxurygift().getList() != null
                && mainFuliAd.getWealluxurygift().getList().size() > 0) {
            newItems.add(mainFuliAd.getWealluxurygift().getList().get(0));
            hasGiftColumn = true;
        }
        if (hasGiftColumn) {
            newItems.add(new SplitLine());
        }
        if (couponListItems.size() > 0) {
            newItems.add(new TjColumnHead(TjColumnHead.TYPE_TIME_COUPON));
            for (CouponListItem couponListItem : couponListItems) {
                newItems.add(couponListItem);
            }
        }
        if (mainFuliAd != null && mainFuliAd.getWeallimitcoupon() != null && mainFuliAd.getWeallimitcoupon().getList() != null
                && mainFuliAd.getWeallimitcoupon().getList().size() > 0) {
            newItems.add(mainFuliAd.getWeallimitcoupon().getList().get(0));
        }
        baseRefreshLayout.resultLoadData(items, newItems, 1);
    }


    @OnClick({R.id.main_gameSearch, R.id.iv_tj_downManager, R.id.rl_goto_msg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_gameSearch:
                SearchActivity.start(mContext);
                break;
            case R.id.iv_tj_downManager:
                DownloadManagerActivity.start(mContext);
                break;
            case R.id.rl_goto_msg:
                MessageActivity.start(mContext);
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
