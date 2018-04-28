package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.CouponBeanList;
import com.etsdk.app.huov7.model.CouponListItem;
import com.etsdk.app.huov7.model.GiftBeanList;
import com.etsdk.app.huov7.model.GiftListItem;
import com.etsdk.app.huov7.provider.CouponListItemViewProvider;
import com.etsdk.app.huov7.provider.GiftListItemViewProvider;
import com.etsdk.app.huov7.ui.CouponListActivity;
import com.etsdk.app.huov7.ui.GiftListActivity;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import butterknife.BindView;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by liu hong liang on 2016/12/7.
 */

public class DetailFuliFragment extends AutoLazyFragment {
    @BindView(R.id.recycler_gift)
    RecyclerView recyclerGift;
    @BindView(R.id.recycler_coupon)
    RecyclerView recyclerCoupon;
    @BindView(R.id.tv_more_gift)
    TextView tvMoreGift;
    @BindView(R.id.tv_more_coupon)
    TextView tvMoreCoupon;
    @BindView(R.id.ll_gift)
    LinearLayout llGift;
    @BindView(R.id.ll_coupon)
    LinearLayout llCoupon;

    private String gameId;
    private Items giftItems = new Items();
    private Items couponItems=new Items();


    public static DetailFuliFragment newInstance(String gameId) {
        DetailFuliFragment detailFuliFragment = new DetailFuliFragment();
        Bundle bundle = new Bundle();
        bundle.putString("gameId", gameId);
        detailFuliFragment.setArguments(bundle);
        return detailFuliFragment;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_detail_fuli);
        setupUI();
    }

    private void setupUI() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            gameId = arguments.getString("gameId");
        }
        recyclerGift.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerGift.setNestedScrollingEnabled(false);
        MultiTypeAdapter giftAdapter = new MultiTypeAdapter(giftItems);
        giftAdapter.register(GiftListItem.class, new GiftListItemViewProvider(giftAdapter));
        recyclerGift.setAdapter(giftAdapter);
        recyclerCoupon.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerCoupon.setNestedScrollingEnabled(false);

        MultiTypeAdapter couponAdapter = new MultiTypeAdapter(couponItems);
        couponAdapter.register(CouponListItem.class, new CouponListItemViewProvider());
        recyclerCoupon.setAdapter(couponAdapter);
        getGiftData();
        getCouponData();
    }

    private void getGiftData() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.giftListApi);
        httpParams.put("gameid", gameId);
        httpParams.put("page", 1);
        httpParams.put("offset", 3);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.giftListApi), new HttpJsonCallBackDialog<GiftBeanList>() {
            @Override
            public void onDataSuccess(GiftBeanList data) {
                if (data != null && data.getData() != null && data.getData().getList() != null) {
                    giftItems.clear();
                    giftItems.addAll(data.getData().getList());

                }
                if (giftItems.size() == 0) {
                    llGift.setVisibility(View.GONE);
                } else {
                    llGift.setVisibility(View.VISIBLE);
                }
                recyclerGift.getAdapter().notifyDataSetChanged();
            }
        });
    }

    public void getCouponData() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.couponListApi);
//        httpParams.put("gameid", gameId);
        httpParams.put("page", 1);
        httpParams.put("offset", 3);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.couponListApi), new HttpJsonCallBackDialog<CouponBeanList>() {
            @Override
            public void onDataSuccess(CouponBeanList data) {
                if (data != null && data.getData() != null && data.getData().getList() != null) {
                    couponItems.clear();
                    couponItems.addAll(data.getData().getList());
                }
                if (couponItems.size() == 0) {
                    llCoupon.setVisibility(View.GONE);
                } else {
                    llCoupon.setVisibility(View.VISIBLE);
                }
                recyclerCoupon.getAdapter().notifyDataSetChanged();
            }
        });
    }


    @OnClick({R.id.tv_more_gift, R.id.tv_more_coupon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_more_gift:
                GiftListActivity.start(mContext,"更多礼包",gameId,0,0,0,0);
                break;
            case R.id.tv_more_coupon:
                CouponListActivity.start(mContext,"积分商城",null,0);
                break;
        }
    }
}
