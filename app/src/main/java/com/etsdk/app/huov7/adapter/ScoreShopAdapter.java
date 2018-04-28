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
import com.etsdk.app.huov7.ui.DoScoreTaskActivity;
import com.etsdk.app.huov7.ui.EntityListActivity;
import com.etsdk.app.huov7.ui.GiftCardListActivity;
import com.etsdk.app.huov7.ui.ScoreRankActivity;
import com.liang530.views.convenientbanner.ConvenientBanner;
import com.liang530.views.refresh.mvc.IDataAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/12/5.
 */

public class ScoreShopAdapter extends RecyclerView.Adapter implements IDataAdapter {
    private static final int MODULE_TOP = 0;
    private static final int OPTION_COLUMN = 1;
    private static final int GOLD_COUPON_LIST = 2;
    private static final int GIFT_CARD_LIST = 3;
    private static final int ENTITY_HEAD = 4;
    public static final int ENTITY = 5;



    private int moduleTopSize = 1;
    private int optionColumnSize = 1;
    private int goldCouponSize = 1;

    private int giftCardSize = 1;
    private int entityHeadSize = 1;
    private int entitySize = 6;

    private Context context;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        context = parent.getContext();
        switch (viewType) {
            case MODULE_TOP:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_score_shop_top, parent, false);
                return new ModuleTopViewHolder(view);
            case OPTION_COLUMN:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_score_shop_option, parent, false);
                return new OptionColumnViewHolder(view);
            case GOLD_COUPON_LIST://代金券兑换专区
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_score_shop_comm_list, parent, false);
                return new CouponViewHolder(view);
            case GIFT_CARD_LIST://礼品卡兑换专区
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_score_shop_comm_list, parent, false);
                return new CardViewHolder(view);
            case ENTITY_HEAD://实物兑换头部
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_score_shop_entity_head, parent, false);
                return new EntityHeadViewHolder(view);
            case ENTITY://实物兑换专区
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score_shop_entity, parent, false);
                return new EntityViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e("hongliang", "hold=" + holder);
        if (holder instanceof ModuleTopViewHolder) {
            ModuleTopViewHolder moduleTopViewHolder = (ModuleTopViewHolder) holder;
            moduleTopViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    EntityDetailActivity.start(context);
                }
            });
        } else if (holder instanceof OptionColumnViewHolder) {
            OptionColumnViewHolder optionColumnViewHolder = (OptionColumnViewHolder) holder;
            optionColumnViewHolder.llScoreRank.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ScoreRankActivity.start(v.getContext());
                }
            });
            optionColumnViewHolder.llGetScore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DoScoreTaskActivity.start(v.getContext());
                }
            });

        } else if (holder instanceof CouponViewHolder) {
            final CouponViewHolder couponViewHolder = (CouponViewHolder) holder;
            couponViewHolder.tvName.setText("代金券兑换专区");
            couponViewHolder.recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            couponViewHolder.recyclerview.setNestedScrollingEnabled(false);
            couponViewHolder.recyclerview.setAdapter(new CouponListAdapter());
            couponViewHolder.llCouponShop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CouponListActivity.start(v.getContext(), "代金券商城",null,0);
                }
            });
            couponViewHolder.convenientBanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    EntityDetailActivity.start(v.getContext());
                }
            });
        } else if (holder instanceof CardViewHolder) {
            final CardViewHolder cardViewHolder = (CardViewHolder) holder;
            cardViewHolder.tvName.setText("礼品卡兑换专区");
            cardViewHolder.recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            cardViewHolder.recyclerview.setNestedScrollingEnabled(false);
            cardViewHolder.recyclerview.setAdapter(new GiftCardListAdapter());
            cardViewHolder.llCouponShop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GiftCardListActivity.start(v.getContext(), "礼品卡商城",1);
                }
            });
            cardViewHolder.convenientBanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    EntityDetailActivity.start(v.getContext());
                }
            });
        } else if (holder instanceof EntityHeadViewHolder) {
            final EntityHeadViewHolder entityHeadViewHolder = (EntityHeadViewHolder) holder;
            entityHeadViewHolder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EntityListActivity.start(v.getContext());
                }
            });
        } else if (holder instanceof EntityViewHolder) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    EntityDetailActivity.start(v.getContext());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < moduleTopSize) {
            return MODULE_TOP;
        }
        if (position < moduleTopSize + optionColumnSize) {
            return OPTION_COLUMN;
        }
        if (position < moduleTopSize + optionColumnSize + goldCouponSize) {
            return GOLD_COUPON_LIST;
        }
        if (position < moduleTopSize + optionColumnSize + goldCouponSize + giftCardSize) {
            return GIFT_CARD_LIST;
        }
        if (position < moduleTopSize + optionColumnSize + goldCouponSize + giftCardSize + entityHeadSize) {
            return ENTITY_HEAD;
        }
        return ENTITY;
    }

    @Override
    public int getItemCount() {
        int size = moduleTopSize + optionColumnSize + goldCouponSize
                + giftCardSize + entitySize;
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
        @BindView(R.id.convenientBanner)
        ConvenientBanner convenientBanner;

        ModuleTopViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class OptionColumnViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_scoreHint)
        TextView tvScoreHint;
        @BindView(R.id.tv_scoreNum)
        TextView tvScoreNum;
        @BindView(R.id.ll_get_score)
        LinearLayout llGetScore;
        @BindView(R.id.ll_score_rank)
        LinearLayout llScoreRank;

        OptionColumnViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class CouponViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_more)
        TextView tvMore;
        @BindView(R.id.recyclerview)
        RecyclerView recyclerview;
        @BindView(R.id.convenientBanner)
        ConvenientBanner convenientBanner;
        @BindView(R.id.ll_coupon_shop)
        LinearLayout llCouponShop;

        CouponViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_more)
        TextView tvMore;
        @BindView(R.id.recyclerview)
        RecyclerView recyclerview;
        @BindView(R.id.convenientBanner)
        ConvenientBanner convenientBanner;
        @BindView(R.id.ll_coupon_shop)
        LinearLayout llCouponShop;


        CardViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    static class EntityHeadViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_more)
        TextView tvMore;
        EntityHeadViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class EntityViewHolder extends RecyclerView.ViewHolder {
        EntityViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
