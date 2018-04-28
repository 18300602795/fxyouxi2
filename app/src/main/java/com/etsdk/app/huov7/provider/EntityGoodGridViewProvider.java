package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.EntityGoodsGvAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/3/10.
 * 积分商城实物商品显示
 */
public class EntityGoodGridViewProvider
        extends ItemViewProvider<EntityGoodGrid, EntityGoodGridViewProvider.ViewHolder> {


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_entity_good_grid, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull EntityGoodGrid entityGoodGrid) {
        holder.gvGoods.setAdapter(new EntityGoodsGvAdapter(entityGoodGrid.getGoodsList()));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.gv_goods)
        GridView gvGoods;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}