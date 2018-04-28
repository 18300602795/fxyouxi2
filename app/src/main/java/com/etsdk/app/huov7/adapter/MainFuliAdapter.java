package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.ui.CouponListActivity;
import com.etsdk.app.huov7.ui.FuliGiftActivity;
import com.etsdk.app.huov7.ui.GiftListActivity;
import com.etsdk.app.huov7.ui.ScoreShopActivity;
import com.etsdk.app.huov7.ui.SignInActivity;
import com.etsdk.app.huov7.ui.WebViewActivity;
import com.liang530.views.convenientbanner.ConvenientBanner;
import com.liang530.views.refresh.mvc.IDataAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/12/10.
 */

public class MainFuliAdapter extends RecyclerView.Adapter implements IDataAdapter {
    public static final int MODULE_TOP = 0;
    public static final int DAY_FEATURE = 1;
    public static final int GIFT_LIST = 2;
    public static final int COUPON_LIST = 3;

    private ViewGroup referLayout;

    private int moduleTopSize = 1;
    private int dayFeatureSize = 1;
    private int giftListSize = 1;
    private int couponListSize = 1;
    private Context context;

    public MainFuliAdapter(ViewGroup view){

        referLayout = view;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        context = parent.getContext();
        switch (viewType) {
            case MODULE_TOP:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_fuli_module_top, parent, false);
                return new ModuleTopViewHolder(view);
            case DAY_FEATURE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_fuli_day_feature, parent, false);
                return new DayFeatureViewHolder(view);
            case GIFT_LIST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_fuli_comm_list, parent, false);
                return new GiftListViewHolder(view);
            case COUPON_LIST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_fuli_comm_list, parent, false);
                return new CouponListViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e("hongliang", "hold=" + holder);
        if (holder instanceof ModuleTopViewHolder) {
            final ModuleTopViewHolder moduleTopViewHolder = (ModuleTopViewHolder) holder;
            moduleTopViewHolder.fuliTopRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
//            moduleTopViewHolder.fuliTopRecycler.setAdapter(new FuliModuleTopImgAdapter());
            moduleTopViewHolder.fuliTopRecycler.setNestedScrollingEnabled(false);
            //TODO 解决recycleView与下拉刷新控件滑动冲突问题

//            moduleTopViewHolder.fuliTopRecycler.requestDisallowInterceptTouchEvent(true);
//            moduleTopViewHolder.fuliTopRecycler.getParent().requestDisallowInterceptTouchEvent(true);
//            referLayout.setEnabled(false);
            /**
            moduleTopViewHolder.fuliTopRecycler.setOnTouchListener(new View.OnTouchListener() {
                int lastX,lastY;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    referLayout.requestDisallowInterceptTouchEvent(false);
                    int x = (int) event.getRawX();
                    int y = (int) event.getRawY();

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            lastX = x;
                            lastY = y;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            int deltaY = y - lastY;
                            int deltaX = x - lastX;
                            if (Math.abs(deltaX) < Math.abs(deltaY)) {
                                referLayout.requestDisallowInterceptTouchEvent(false);
                            } else {
                                referLayout.requestDisallowInterceptTouchEvent(true);
                            }
                        default:
                            break;
                    }
                    return false;
                }
            }); */

            moduleTopViewHolder.tvScoreShop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ScoreShopActivity.start(v.getContext());
                }
            });
            moduleTopViewHolder.tvApplyGift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FuliGiftActivity.start(v.getContext());
                }
            });
        } else if (holder instanceof DayFeatureViewHolder) {
            DayFeatureViewHolder dayFeatureViewHolder = (DayFeatureViewHolder) holder;
            dayFeatureViewHolder.llGotoSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SignInActivity.start(v.getContext());
                }
            });
            dayFeatureViewHolder.llCardCouponDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebViewActivity.start(v.getContext(),"卡券说明","http://baidu.com");
                }
            });
            dayFeatureViewHolder.llCouponShop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CouponListActivity.start(v.getContext(),"积分商城",null,0);
                }
            });

        } else if (holder instanceof GiftListViewHolder) {
            GiftListViewHolder giftListViewHolder = (GiftListViewHolder) holder;
            giftListViewHolder.recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            giftListViewHolder.recyclerview.setNestedScrollingEnabled(false);
            giftListViewHolder.recyclerview.setAdapter(new GiftListAdapter());
            giftListViewHolder.tvName.setText("豪华礼包");
            giftListViewHolder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GiftListActivity.start(v.getContext(),"豪华礼包",null,0,0,0,2);
                }
            });
        } else if (holder instanceof CouponListViewHolder) {
            CouponListViewHolder couponListViewHolder = (CouponListViewHolder) holder;
            couponListViewHolder.recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            couponListViewHolder.recyclerview.setNestedScrollingEnabled(false);
            couponListViewHolder.recyclerview.setAdapter(new CouponListAdapter());
            couponListViewHolder.tvName.setText("限时代金券");
            couponListViewHolder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CouponListActivity.start(v.getContext(),"限时代金券",null,1);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < moduleTopSize) {
            return MODULE_TOP;
        }
        if (position < moduleTopSize + dayFeatureSize) {
            return DAY_FEATURE;
        }
        if (position < moduleTopSize + dayFeatureSize + giftListSize) {
            return GIFT_LIST;
        }
        return COUPON_LIST;
    }

    @Override
    public int getItemCount() {
        int size = moduleTopSize + dayFeatureSize + giftListSize
                + couponListSize;
        return size;
    }

    @Override
    public void notifyDataChanged(Object o, boolean b) {

    }

    @Override
    public Object getData() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    static class ModuleTopViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.fuliTopRecycler)
        RecyclerView fuliTopRecycler;
        @BindView(R.id.tv_applyGift)
        TextView tvApplyGift;
        @BindView(R.id.tv_inviteFriend)
        TextView tvInviteFriend;
        @BindView(R.id.tv_scoreShop)
        TextView tvScoreShop;

        ModuleTopViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class DayFeatureViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_goto_signIn)
        LinearLayout llGotoSignIn;
        @BindView(R.id.ll_coupon_shop)
        LinearLayout llCouponShop;
        @BindView(R.id.ll_cardCouponDesc)
        LinearLayout llCardCouponDesc;
        @BindView(R.id.convenientBanner)
        ConvenientBanner convenientBanner;

        DayFeatureViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class GiftListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_more)
        TextView tvMore;
        @BindView(R.id.recyclerview)
        RecyclerView recyclerview;
        @BindView(R.id.convenientBanner)
        ConvenientBanner convenientBanner;

        GiftListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class CouponListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_more)
        TextView tvMore;
        @BindView(R.id.recyclerview)
        RecyclerView recyclerview;
        @BindView(R.id.convenientBanner)
        ConvenientBanner convenientBanner;

        CouponListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
