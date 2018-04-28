package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.CouponListItem;
import com.etsdk.app.huov7.ui.SelectGamePayActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/13.
 */
public class MineCouponViewProvider
        extends ItemViewProvider<CouponListItem, MineCouponViewProvider.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_mine_coupon, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull CouponListItem mineCoupon) {
        if(mineCoupon.getMoney()!=null&&mineCoupon.getMoney().length()<3){
            holder.tvMoneyText.setText(mineCoupon.getMoney() + "元");
        }else{
            holder.tvMoneyText.setText(mineCoupon.getMoney());
        }
        holder.tvCouponMoney.setText(mineCoupon.getMoney() + "元");
        holder.tvCouponCount.setText(mineCoupon.getMyremain() + "张");
        holder.tvCouponeName.setText(mineCoupon.getCouponname());
        holder.btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectGamePayActivity.start(v.getContext());
            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_money_text)
        TextView tvMoneyText;
        @BindView(R.id.tv_gift_name)
        TextView tvGiftName;
        @BindView(R.id.tv_coupone_name)
        TextView tvCouponeName;
        @BindView(R.id.tv_coupon_money)
        TextView tvCouponMoney;
        @BindView(R.id.btn_option)
        Button btnOption;
        @BindView(R.id.tv_coupon_count)
        TextView tvCouponCount;
        @BindView(R.id.v_line)
        View vLine;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}