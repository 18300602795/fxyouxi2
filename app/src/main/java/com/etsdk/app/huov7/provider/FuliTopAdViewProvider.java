package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.FuliModuleTopImgAdapter;
import com.etsdk.app.huov7.model.AdImageKeyBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/21.
 */
public class FuliTopAdViewProvider
        extends ItemViewProvider<AdImageKeyBean, FuliTopAdViewProvider.ViewHolder> {



    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_fuli_top_ad, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull AdImageKeyBean fuliTopAd) {
        holder.fuliTopRecycler.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        holder.fuliTopRecycler.setNestedScrollingEnabled(false);
        holder.fuliTopRecycler.setAdapter(new FuliModuleTopImgAdapter(fuliTopAd.getList()));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.fuliTopRecycler)
        RecyclerView fuliTopRecycler;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}