package com.etsdk.app.huov7.provider;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.GiftCard;
import com.etsdk.app.huov7.ui.EntityDetailActivity;
import com.game.sdk.util.GsonUtil;
import com.liang530.utils.GlideDisplay;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/2/9.
 */
public class GoodsListItemViewProvider
        extends ItemViewProvider<GiftCard, GoodsListItemViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_goods_list_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final GiftCard goodsListItem) {
        GlideDisplay.dispalyWithFitCenterDef(holder.ivGiftCard,goodsListItem.getOriginal_img(),R.mipmap.ic_launcher);
        holder.tvPrice.setText("市场价1："+goodsListItem.getMarket_price()+"元");
        holder.tvPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tvGoodsName.setText(goodsListItem.getGoodsname());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntityDetailActivity.start(v.getContext(), GsonUtil.getGson().toJson(goodsListItem));
            }
        });
        holder.tvExchangeScore.setText(goodsListItem.getIntegral()+"积分");
        holder.btnExchange.setClickable(false);
        if(isViewTypeStart(goodsListItem)){
            holder.vLine.setVisibility(View.GONE);
        }else{
            holder.vLine.setVisibility(View.VISIBLE);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.v_line)
        View vLine;
        @BindView(R.id.iv_gift_card)
        ImageView ivGiftCard;
        @BindView(R.id.tv_goodsName)
        TextView tvGoodsName;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_exchangeScore)
        TextView tvExchangeScore;
        @BindView(R.id.btn_exchange)
        Button btnExchange;
        @BindView(R.id.tv_progress)
        TextView tvProgress;
        @BindView(R.id.pb_progressBar)
        ProgressBar pbProgressBar;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}