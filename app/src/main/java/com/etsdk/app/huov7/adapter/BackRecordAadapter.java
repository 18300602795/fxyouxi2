package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.BackRecordList;
import com.etsdk.app.huov7.ui.BackEditActivity;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;
import com.liang530.views.refresh.mvc.IDataAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/9/26.
 */

public class BackRecordAadapter extends RecyclerView.Adapter implements IDataAdapter<List<BackRecordList.BackRecord>> {
    List<BackRecordList.BackRecord> listBeen = new ArrayList<>();
    Context context;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new GameListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_back_record, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        ((GameListViewHolder) holder).tvGameName.setText(listBeen.get(position).getGame_name());
        switch (listBeen.get(position).getStatus()) {
            case "1":
                ((GameListViewHolder) holder).tvPayType.setText("审核通过");
                ((GameListViewHolder) holder).tvPayType.setBackgroundResource(R.drawable.back_state_succed);
                break;
            case "2":
                ((GameListViewHolder) holder).tvPayType.setText("审核中");
                ((GameListViewHolder) holder).tvPayType.setBackgroundResource(R.drawable.back_state_loading);
                break;
            case "3":
                ((GameListViewHolder) holder).tvPayType.setText("审核失败");
                ((GameListViewHolder) holder).tvPayType.setBackgroundResource(R.drawable.back_state_fause);
                break;
        }
//        GlideDisplay.display(((GameListViewHolder) holder).iv_game_img, listBeen.get(position).getGame_icon(), R.mipmap.ic_launcher);
        Glide.with(context).load(listBeen.get(position).getGame_icon()).placeholder(R.mipmap.ic_launcher).into(((GameListViewHolder) holder).iv_game_img);
        ((GameListViewHolder) holder).tvTime.setText("充值时间：" + format.format(new Date((Long.valueOf(listBeen.get(position).getTime()) * 1000))));
        ((GameListViewHolder) holder).game_list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("backBean", listBeen.get(position));
                intent.setClass(context, BackEditActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBeen.size();
    }

    @Override
    public void notifyDataChanged(List<BackRecordList.BackRecord> listBeen, boolean isRefresh) {
        if (isRefresh) {
            this.listBeen.clear();
        }
        this.listBeen.addAll(listBeen);
        notifyDataSetChanged();
    }

    @Override
    public List<BackRecordList.BackRecord> getData() {
        return listBeen;
    }

    @Override
    public boolean isEmpty() {
        return listBeen.isEmpty();
    }

    static class GameListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_game_name)
        TextView tvGameName;
        @BindView(R.id.back_state)
        TextView tvPayType;
        @BindView(R.id.tv_oneword)
        TextView tvTime;
        @BindView(R.id.iv_game_img)
        RoundedImageView iv_game_img;
        @BindView(R.id.game_list_item)
        RelativeLayout game_list_item;

        GameListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
