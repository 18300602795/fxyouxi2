package com.etsdk.app.huov7.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.GameItemPay;
import com.etsdk.app.huov7.ui.GamePayActivity;
import com.etsdk.app.huov7.view.GameTagView;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/12.
 */
public class GameItemPayViewProvider
        extends ItemViewProvider<GameItemPay, GameItemPayViewProvider.ViewHolder> {



    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_game_item_pay, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final GameItemPay gameItemPay) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GamePayActivity.start(v.getContext(), gameItemPay.getGameid(), gameItemPay.getGamename());
            }
        });
        holder.tvGameName.setText(gameItemPay.getGamename());
        holder.tvOneword.setText(gameItemPay.getOneword());
//        GlideDisplay.display(holder.ivGameImg, gameItemPay.getIcon(), R.mipmap.ic_launcher);
        Glide.with(holder.context).load(gameItemPay.getIcon()).placeholder(R.mipmap.ic_launcher).into(holder.ivGameImg);
        holder.gameTagView.setGameType(gameItemPay.getType());
        if(isViewTypeStart(gameItemPay)){
            holder.vLine.setVisibility(View.GONE);
        }else{
            holder.vLine.setVisibility(View.VISIBLE);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.v_line)
        View vLine;
        @BindView(R.id.tv_hot_rank)
        TextView tvHotRank;
        @BindView(R.id.iv_game_img)
        RoundedImageView ivGameImg;
        @BindView(R.id.tv_game_name)
        TextView tvGameName;
        @BindView(R.id.gameTagView)
        GameTagView gameTagView;
        @BindView(R.id.tv_oneword)
        TextView tvOneword;
        @BindView(R.id.tv_pay)
        TextView tvPay;
        @BindView(R.id.game_list_item)
        LinearLayout gameListItem;
        Context context;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            context = itemView.getContext();
        }
    }
}