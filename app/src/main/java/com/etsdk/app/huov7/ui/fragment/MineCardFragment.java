package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.CouponListAdapter;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.Goods;
import com.etsdk.app.huov7.model.GoodsBeanList;
import com.etsdk.app.huov7.model.MineGiftCard;
import com.etsdk.app.huov7.model.MineGoodsRequestBean;
import com.etsdk.app.huov7.provider.MineGiftCardViewProvider;
import com.etsdk.app.huov7.util.RecyclerViewNoAnimator;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by liu hong liang on 2016/12/10.
 * 游戏列表fragment
 */

public class MineCardFragment extends AutoLazyFragment implements AdvRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    CouponListAdapter couponListAdapter;
    BaseRefreshLayout baseRefreshLayout;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    private boolean requestTopSplit = false;//是否需要顶部分割线
    private boolean showRank = false;
    private MultiTypeAdapter multiTypeAdapter;
    private Items items = new Items();

    public static MineCardFragment newInstance(boolean requestTopSplit, boolean showRank) {
        MineCardFragment newFragment = new MineCardFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("requestTopSplit", requestTopSplit);
        bundle.putBoolean("showRank", showRank);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_comm_list);
        setupUI();
    }

    private void setupUI() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            requestTopSplit = arguments.getBoolean("requestTopSplit");
            showRank = arguments.getBoolean("showRank");
        }
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new RecyclerViewNoAnimator());
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(Goods.class, new MineGiftCardViewProvider());
        // 设置适配器
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.refresh();
    }

    private Items getTestData() {
        Items items = new Items();
        for (int i = 0; i < 20; i++) {
            items.add(new MineGiftCard());
        }
        return items;
    }

    @Override
    public void getPageData(int requestPageNo) {
        final MineGoodsRequestBean mineGoodsRequestBean = new MineGoodsRequestBean();
        mineGoodsRequestBean.setIs_real("1");
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(mineGoodsRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<GoodsBeanList.DataBean>(getActivity(), httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(GoodsBeanList.DataBean data) {
                if (data != null&&data.getList()!=null) {
                    baseRefreshLayout.resultLoadData(items,data.getList(),1);
                }else{
                    baseRefreshLayout.resultLoadData(items,new ArrayList(),1);
                }
            }
            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                baseRefreshLayout.resultLoadData(items,null,1);
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userGoodsListApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    @OnClick({})
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }
}
