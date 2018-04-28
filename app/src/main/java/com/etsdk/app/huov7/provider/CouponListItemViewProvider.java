package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.CouponListItem;
import com.etsdk.app.huov7.ui.CouponDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/5.
 */
public class CouponListItemViewProvider
        extends ItemViewProvider<CouponListItem, CouponListItemViewProvider.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_coupon_list_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final CouponListItem couponListItem) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CouponDetailActivity.start(v.getContext(),couponListItem.getCouponid());
            }
        });
        if(couponListItem.getMoney()!=null&&couponListItem.getMoney().length()==3){
            holder.tvImageMoneyText.setText(couponListItem.getMoney());
        }else{
            holder.tvImageMoneyText.setText(couponListItem.getMoney()+"元");
        }
        holder.tvCouponMoney.setText(couponListItem.getMoney()+"元");
        holder.tvExchangeScore.setText(couponListItem.getIntegral()+"积分");
        int total = couponListItem.getTotal();
        int remain = couponListItem.getRemain();
        int progress=100;
        if(total!=0){
            progress=(int)((total-remain)*100./total);
        }
        holder.tvProgress.setText("剩余"+(100-progress)+"%");
        holder.pbCouponProgress.setProgress((100-progress));
        if(isViewTypeStart(couponListItem)){
            holder.vLine.setVisibility(View.GONE);
        }else{
            holder.vLine.setVisibility(View.VISIBLE);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_image_money_text)
        TextView tvImageMoneyText;
        @BindView(R.id.tv_coupone_name)
        TextView tvCouponeName;
        @BindView(R.id.tv_coupon_money)
        TextView tvCouponMoney;
        @BindView(R.id.tv_exchangeScore)
        TextView tvExchangeScore;
        @BindView(R.id.btn_exchange)
        Button btnExchange;
        @BindView(R.id.tv_progress)
        TextView tvProgress;
        @BindView(R.id.pb_couponProgress)
        ProgressBar pbCouponProgress;
        @BindView(R.id.v_line)
        View vLine;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}