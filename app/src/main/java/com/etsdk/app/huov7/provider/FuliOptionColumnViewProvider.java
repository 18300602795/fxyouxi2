package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.ui.CardCouponIntroduceActivity;
import com.etsdk.app.huov7.ui.CouponListActivity;
import com.etsdk.app.huov7.ui.FuliGiftActivity;
import com.etsdk.app.huov7.ui.RecommandTaskActivity;
import com.etsdk.app.huov7.ui.ScoreShopActivity;
import com.etsdk.app.huov7.ui.SignInActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/21.
 */
public class FuliOptionColumnViewProvider
        extends ItemViewProvider<FuliOptionColumn, FuliOptionColumnViewProvider.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_fuli_option_column, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull FuliOptionColumn fuliOptionColumn) {
        holder.llGotoSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInActivity.start(v.getContext());
            }
        });
        holder.llCardCouponDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardCouponIntroduceActivity.start(v.getContext());
            }
        });
        holder.llCouponShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CouponListActivity.start(v.getContext(), "代金券列表", null, 0);
            }
        });
        holder.tvScoreShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScoreShopActivity.start(v.getContext());
            }
        });
        holder.tvApplyGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FuliGiftActivity.start(v.getContext());
            }
        });
        holder.tvInviteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecommandTaskActivity.start(v.getContext());
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_applyGift)
        TextView tvApplyGift;
        @BindView(R.id.tv_inviteFriend)
        TextView tvInviteFriend;
        @BindView(R.id.tv_scoreShop)
        TextView tvScoreShop;
        @BindView(R.id.ll_goto_signIn)
        LinearLayout llGotoSignIn;
        @BindView(R.id.ll_coupon_shop)
        LinearLayout llCouponShop;
        @BindView(R.id.ll_cardCouponDesc)
        LinearLayout llCardCouponDesc;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}