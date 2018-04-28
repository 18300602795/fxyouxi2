package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.ui.GameDetailV2Activity;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/12/6.
 * 我的游戏列表
 */

public class GameMoneyListAdapter extends RecyclerView.Adapter {

    List<GameBean> gameBeanList;
    public GameMoneyListAdapter(List<GameBean> gameBeanList) {
        this.gameBeanList = gameBeanList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_game_money, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameDetailV2Activity.start(v.getContext(),gameBeanList.get(holder.getAdapterPosition()).getGameid());
            }
        });
        ViewHolder viewHolder = (ViewHolder) holder;
        GameBean gameBean = gameBeanList.get(position);
//        GlideDisplay.display(viewHolder.ivGameImg, gameBean.getIcon(),R.mipmap.ic_launcher);
        Glide.with(viewHolder.context).load(gameBean.getIcon()).placeholder(R.mipmap.ic_launcher).into(viewHolder.ivGameImg);
        viewHolder.tvGameName.setText(gameBean.getGamename());
        viewHolder.tvOneword.setText(gameBean.getOneword());
        viewHolder.tvGameMoney.setText(gameBean.getGmcnt()+"游戏币");
    }

    @Override
    public int getItemCount() {
        return gameBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_game_img)
        RoundedImageView ivGameImg;
        @BindView(R.id.tv_game_name)
        TextView tvGameName;
        @BindView(R.id.tv_oneword)
        TextView tvOneword;
        @BindView(R.id.tv_game_money)
        TextView tvGameMoney;
        Context context;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            context = view.getContext();
        }
    }
}
