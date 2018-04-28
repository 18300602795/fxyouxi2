package com.etsdk.app.huov7.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.Goods;
import com.etsdk.app.huov7.ui.dialog.HintDialogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/13.
 */
public class MineGiftCardViewProvider
        extends ItemViewProvider<Goods, MineGiftCardViewProvider.ViewHolder> {



    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_mine_gift_card, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Goods mineGoods) {
        holder.tvGoodsIntro.setText(mineGoods.getGoodsname());
        holder.tvMoney.setText(mineGoods.getMarket_price()+"元");
//        GlideDisplay.display(holder.ivEntityImg,mineGoods.getOriginal_img(),R.mipmap.ic_launcher);
        Glide.with(holder.context).load(mineGoods.getOriginal_img()).placeholder(R.mipmap.ic_launcher).into(holder.ivEntityImg);
        if(isViewTypeStart(mineGoods)){
            holder.vLine.setVisibility(View.GONE);
        }else{
            holder.vLine.setVisibility(View.VISIBLE);
        }
        if(TextUtils.isEmpty(mineGoods.getNote())){//虚拟物品
            holder.tvOption.setText("尚未发货");
            holder.tvOption.setClickable(false);
            holder.tvOption.setEnabled(false);
        }else{
            holder.tvOption.setText("查看卡密");
            holder.tvOption.setClickable(true);
            holder.tvOption.setEnabled(true);
        }
        holder.tvOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HintDialogUtil().showHintDialog(v.getContext(),"查看卡密",mineGoods.getNote(),"确定",null,null);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_entity_img)
        ImageView ivEntityImg;
        @BindView(R.id.tv_money_text)
        TextView tvMoneyText;
        @BindView(R.id.tv_money)
        TextView tvMoney;
        @BindView(R.id.tv_goods_intro)
        TextView tvGoodsIntro;
        @BindView(R.id.tv_option)
        TextView tvOption;
        @BindView(R.id.v_line)
        View vLine;
        Context context;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            context = itemView.getContext();
        }
    }
}