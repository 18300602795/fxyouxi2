package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.GamelikeItemAdapter;
import com.etsdk.app.huov7.model.GamelikeBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * 猜你喜欢
 * Created by Administrator on 2017/4/27 0027.
 */

public class GamelikeListProvider
        extends ItemViewProvider<GamelikeBean, GamelikeListProvider.ViewHolder> {



    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_game_like_list, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull GamelikeBean gamelikeBean) {
        holder.setData(gamelikeBean);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            GridLayoutManager layoutManager = new GridLayoutManager(itemView.getContext(), 5);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
//            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
        }

        void setData(GamelikeBean gamelikeBean){
            recyclerView.setAdapter(new GamelikeItemAdapter(gamelikeBean));
        }
    }

}
