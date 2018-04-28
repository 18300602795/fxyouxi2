package com.etsdk.app.huov7.shop.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.shop.model.MyGameBean;
import com.etsdk.app.huov7.shop.model.SelectGameEvent;
import com.etsdk.app.huov7.shop.ui.MyGameListActivity;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2016/12/21.
 */
public class MyGameItemViewProvider
        extends ItemViewProvider<MyGameBean.DataBean.ListBean, MyGameItemViewProvider.ViewHolder> {
    MyGameListActivity myGameListActivity;

    public MyGameItemViewProvider(MyGameListActivity myGameListActivity) {
        this.myGameListActivity = myGameListActivity;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_my_game_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final MyGameBean.DataBean.ListBean gameBean) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myGameListActivity!=null) {
                    myGameListActivity.finish();
                    myGameListActivity = null;
                }
                EventBus.getDefault().post(new SelectGameEvent(gameBean.getId()+"", gameBean.getGamename(), gameBean.getIcon()));
            }
        });
        holder.tvName.setText(gameBean.getGamename());
//        GlideDisplay.display(holder.ivGameIcon, gameBean.getIcon(), R.mipmap.icon_load);
        Glide.with(holder.context).load(gameBean.getIcon()).placeholder(R.mipmap.icon_load).into(holder.ivGameIcon);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_game_icon)
        RoundedImageView ivGameIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        Context context;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            context = view.getContext();
        }
    }
}