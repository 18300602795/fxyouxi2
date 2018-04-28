package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.CardCoupon;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/23.
 */
public class CardCouponViewProvider
        extends ItemViewProvider<CardCoupon, CardCouponViewProvider.ViewHolder> {



    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_card_coupon, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull CardCoupon cardCoupon) {
        holder.tvTitle.setText(cardCoupon.getName());
        holder.tvContent.setText(cardCoupon.getContent());
        if(!isViewTypeStart(cardCoupon)&&!isViewTypeEnd(cardCoupon)){
            holder.vLine.setVisibility(View.VISIBLE);
        }else{
            holder.vLine.setVisibility(View.GONE);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.v_line)
        View vLine;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}