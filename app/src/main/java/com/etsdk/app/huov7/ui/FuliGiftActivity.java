package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.FuliGiftAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.AdImage;
import com.etsdk.app.huov7.model.FuliGiftAd;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.GameBeanList;
import com.etsdk.app.huov7.model.GiftBeanList;
import com.etsdk.app.huov7.model.GiftListItem;
import com.etsdk.app.huov7.model.HotRecGameGift;
import com.etsdk.app.huov7.model.SplitLine;
import com.etsdk.app.huov7.model.TjColumnHead;
import com.etsdk.app.huov7.provider.AdImageViewProvider;
import com.etsdk.app.huov7.provider.GiftHotRecViewProvider;
import com.etsdk.app.huov7.provider.GiftListItemViewProvider;
import com.etsdk.app.huov7.provider.SplitLineViewProvider;
import com.etsdk.app.huov7.provider.TjColumnHeadViewProvider;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by liu hong liang on 2016/12/10.
 * 福利礼包界面，from 首页-推荐-礼包
 */
public class FuliGiftActivity extends ImmerseActivity implements AdvRefreshListener {

    FuliGiftAdapter fuliGiftAdapter;
    BaseRefreshLayout baseRefreshLayout;
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
    @BindView(R.id.activity_fuli_gift)
    LinearLayout activityFuliGift;
    private Items items = new Items();
    private MultiTypeAdapter multiTypeAdapter;
    private FuliGiftAd.DataBean FuliGiftAd;
    private List<GameBean> giftHotRecItems = new ArrayList();
    private List<GiftListItem> giftHotItems = new ArrayList();
    private List<GiftListItem> giftNewItems = new ArrayList();
    private List<GiftListItem> giftRecomendItems = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuli_gift);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("福利礼包");
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(AdImage.class, new AdImageViewProvider());
        multiTypeAdapter.register(TjColumnHead.class, new TjColumnHeadViewProvider());
        multiTypeAdapter.register(HotRecGameGift.class, new GiftHotRecViewProvider());
        multiTypeAdapter.register(SplitLine.class, new SplitLineViewProvider());
        multiTypeAdapter.register(GiftListItem.class, new GiftListItemViewProvider(multiTypeAdapter));
        fuliGiftAdapter = new FuliGiftAdapter();
        // 设置适配器
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
//        // 加载数据
        baseRefreshLayout.refresh();
    }

    @Override
    public void getPageData(int requestPageNo) {
//        getFuliGiftAdData();
//        getHotRecGameGiftData();
        getGift(2,0,0,0,2);
        getGift(0,2,0,0,2);
        getGift(0,0,2,0,2);
    }

    /**
     * banner广告
     */
    private void getFuliGiftAdData() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.slideListApi);
        httpParams.put("type", "gift");
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).setShowErrorToast(false).get(AppApi.getUrl(AppApi.slideListApi), new HttpJsonCallBackDialog<FuliGiftAd>() {
            @Override
            public void onDataSuccess(FuliGiftAd data) {
                if (data != null && data.getData() != null) {
                    FuliGiftAd = data.getData();
                    updateListData();
                }
            }
        });
    }

    /**
     * 拿了几个热门游戏当广告
     */
    private void getHotRecGameGiftData(){
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.gameListApi);
        httpParams.put("remd","2");
        httpParams.put("hasgift","2");
        httpParams.put("page",1);
        httpParams.put("offset",6);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.gameListApi),new HttpJsonCallBackDialog<GameBeanList>(){
            @Override
            public void onDataSuccess(GameBeanList data) {
                giftHotRecItems.clear();
                if(data!=null&&data.getData()!=null&&data.getData().getList()!=null){
                    giftHotRecItems.addAll(data.getData().getList());
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



    private void getGift(final int hot, final int isnew, final int remd, int luxury,int offset ) {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.giftListApi);
        httpParams.put("hot", hot);
        httpParams.put("isnew", isnew);
        httpParams.put("remd", remd);
        httpParams.put("luxury", luxury);
        httpParams.put("page", 1);
        httpParams.put("offset", offset);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.giftListApi), new HttpJsonCallBackDialog<GiftBeanList>() {
            @Override
            public void onDataSuccess(GiftBeanList data) {
                List<GiftListItem> giftList=new ArrayList<GiftListItem>();
                if(data!=null&&data.getData()!=null&&data.getData().getList() != null){
                    giftList=data.getData().getList();
                }
                if(hot!=0){
                    giftHotItems.clear();
                    giftHotItems.addAll(giftList);
                }
                if(isnew!=0){
                    giftNewItems.clear();
                    giftNewItems.addAll(giftList);
                }
                if(remd!=0){
                    giftRecomendItems.clear();
                    giftRecomendItems.addAll(giftList);
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


    private void updateListData() {
        Items newItems = new Items();
        if (FuliGiftAd != null && FuliGiftAd.getGifttopper() != null && FuliGiftAd.getGifttopper().getList() != null
                && FuliGiftAd.getGifttopper().getList().size() > 0) {
            AdImage adImage = FuliGiftAd.getGifttopper().getList().get(0);
            adImage.setRequestPadding(false);
            newItems.add(adImage);
        }
        //热门推荐游戏
        boolean hasSplitColumn = true;
        if (giftHotRecItems.size() > 0) {
            newItems.add(new TjColumnHead(TjColumnHead.TYPE_HOT_REC_GIFT));
            HotRecGameGift hotRecGameGift =new HotRecGameGift(giftHotRecItems);
            newItems.add(hotRecGameGift);
            hasSplitColumn = true;
        }
        if (FuliGiftAd != null && FuliGiftAd.getGiftrmd() != null && FuliGiftAd.getGiftrmd().getList() != null
                && FuliGiftAd.getGiftrmd().getList().size() > 0) {
            newItems.add(FuliGiftAd.getGiftrmd().getList().get(0));
            hasSplitColumn = true;
        }
//        if (hasSplitColumn) {
//            newItems.add(new SplitLine());
//        }
        if(giftRecomendItems.size()+giftHotItems.size()>0) {//有数据才加入分割线
            newItems.add(new SplitLine());
        }
        //推荐礼包
        if (giftRecomendItems.size() > 0) {
            newItems.add(new TjColumnHead(TjColumnHead.TYPE_HOT_REC_GIFT));
            for (GiftListItem giftListItem : giftRecomendItems) {
                newItems.add(giftListItem);
            }
            newItems.add(new SplitLine());
        }
        //热门礼包
        hasSplitColumn = true;
        if (giftHotItems.size() > 0) {
            newItems.add(new TjColumnHead(TjColumnHead.TYPE_HOT_GIFT));
            for (GiftListItem giftListItem : giftHotItems) {
                newItems.add(giftListItem);
            }
            hasSplitColumn = true;
            newItems.add(new SplitLine());
        }
        if (FuliGiftAd != null && FuliGiftAd.getGifthot() != null && FuliGiftAd.getGifthot().getList() != null
                && FuliGiftAd.getGifthot().getList().size() > 0) {
            newItems.add(FuliGiftAd.getGifthot().getList().get(0));
            hasSplitColumn = true;
        }
//        if (hasSplitColumn) {
//            newItems.add(new SplitLine());
//        }
        //最新礼包
        if (giftNewItems.size() > 0) {
            newItems.add(new TjColumnHead(TjColumnHead.TYPE_NEW_GIFT));
            for (GiftListItem giftListItem : giftNewItems) {
                newItems.add(giftListItem);
            }
            newItems.add(new SplitLine());
        }
        items.clear();
        baseRefreshLayout.resultLoadData(items, newItems, 1);
    }

    public static void start(Context context) {
        //福利礼包改为显示全部礼包

        GiftListSearchActivity.start(context,"福利礼包",null,0,0,0,0);

//        Intent starter = new Intent(context, FuliGiftActivity.class);
//        context.startActivity(starter);
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
