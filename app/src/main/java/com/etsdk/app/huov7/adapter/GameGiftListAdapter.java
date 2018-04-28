package com.etsdk.app.huov7.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.etsdk.app.huov7.view.ListGameGiftItem;

/**
 * Created by liu hong liang on 2016/12/6.
 * 游戏列表，带礼包领取按钮
 *
 *
 */

public class GameGiftListAdapter extends RecyclerView.Adapter {


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new GameGiftListAdapter.ViewHolder(new ListGameGiftItem(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ListGameGiftItem listGameItem;
        ViewHolder(ListGameGiftItem view) {
            super(view);
            listGameItem=view;
        }
    }
}
