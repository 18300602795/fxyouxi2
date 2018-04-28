package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.Goods;
import com.liang530.utils.GlideDisplay;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/14.
 */
public class MineEntityViewProvider
        extends ItemViewProvider<Goods, MineEntityViewProvider.ViewHolder> {


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_mine_entity, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Goods goods) {
        holder.tvEntityName.setText(goods.getGoodsname());
        GlideDisplay.dispalyWithFitCenterDef(holder.ivEntityImg,goods.getOriginal_img(),R.mipmap.ic_launcher);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_entity_img)
        ImageView ivEntityImg;
        @BindView(R.id.tv_entity_name)
        TextView tvEntityName;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}