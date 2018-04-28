package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.HotRecGameGiftAdapter;
import com.etsdk.app.huov7.model.HotRecGameGift;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/22.
 */
public class GiftHotRecViewProvider
        extends ItemViewProvider<HotRecGameGift, GiftHotRecViewProvider.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_gift_hot_rec, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull HotRecGameGift hotRecGameGift) {
        holder.recyclerview.setLayoutManager(new GridLayoutManager(holder.itemView.getContext(), 3));
        holder.recyclerview.setNestedScrollingEnabled(false);
        holder.recyclerview.setAdapter(new HotRecGameGiftAdapter(hotRecGameGift.getDatas()));
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recyclerview)
        RecyclerView recyclerview;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}