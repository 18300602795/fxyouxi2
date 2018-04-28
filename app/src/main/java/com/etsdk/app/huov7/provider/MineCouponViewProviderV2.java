package com.etsdk.app.huov7.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.CouponListItemV2;
import com.etsdk.app.huov7.ui.GameDetailV2Activity;
import com.etsdk.app.huov7.ui.GamePayActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/13.
 */
public class MineCouponViewProviderV2
        extends ItemViewProvider<CouponListItemV2, MineCouponViewProviderV2.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_mine_coupon_v2, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final CouponListItemV2 mineCoupon) {
        holder.tvCouponMoney.setText(mineCoupon.getGmcnt()+"");
        holder.tvName.setText(mineCoupon.getGamename());
        holder.tvOneword.setText(mineCoupon.getOneword());
        holder.btnCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GamePayActivity.start(v.getContext(), mineCoupon.getGameid(), mineCoupon.getGamename());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameDetailV2Activity.start(v.getContext(), mineCoupon.getGameid());
            }
        });
//        GlideDisplay.display(holder.ivIcon, mineCoupon.getIcon(), R.mipmap.ic_bg_coupon);
        Glide.with(holder.context).load(mineCoupon.getIcon()).placeholder(R.mipmap.ic_launcher).into(holder.ivIcon);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_coupon_money)
        TextView tvCouponMoney;
        @BindView(R.id.tv_oneword)
        TextView tvOneword;
        @BindView(R.id.btn_charge)
        Button btnCharge;
        @BindView(R.id.v_line)
        View vLine;
        Context context;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            context = view.getContext();
        }
    }
}