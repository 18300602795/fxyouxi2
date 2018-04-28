package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.CouponPay;
import com.etsdk.app.huov7.ui.GamePayActivity;
import com.etsdk.app.huov7.view.NumUpDownView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/13.
 */
public class CouponPayViewProvider
        extends ItemViewProvider<CouponPay, CouponPayViewProvider.ViewHolder> {


    private GamePayActivity gamePayActivity;

    public CouponPayViewProvider(GamePayActivity gamePayActivity) {
        this.gamePayActivity = gamePayActivity;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_new_coupon_pay, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull CouponPay couponPay) {
        holder.tvRemainCount.setText(couponPay.getMyremain() + "张");
        if (couponPay.getMoney() != null && couponPay.getMoney().length() == 3) {
            holder.tvImageMoneyText.setText(couponPay.getMoney());
        } else {
            holder.tvImageMoneyText.setText(couponPay.getMoney() + "元");
        }
        holder.tvCouponMoney.setText(couponPay.getMoney() + "元");
        if (isViewTypeEnd(couponPay)) {
            holder.vLine.setVisibility(View.GONE);
        } else {
            holder.vLine.setVisibility(View.VISIBLE);
        }
        holder.vNumUpDown.setGamePayActivity(gamePayActivity, couponPay);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_image_money_text)
        TextView tvImageMoneyText;
        @BindView(R.id.tv_coupone_name)
        TextView tvCouponeName;
        @BindView(R.id.tv_coupon_money)
        TextView tvCouponMoney;
        @BindView(R.id.tv_remainCount)
        TextView tvRemainCount;
        @BindView(R.id.v_numUpDown)
        NumUpDownView vNumUpDown;
        @BindView(R.id.v_line)
        View vLine;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            View viewById = itemView.findViewById(R.id.v_numSwitch);
        }
    }
}