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
import com.etsdk.app.huov7.ui.GiftListActivity;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/12/9.
 */

public class HotRecGameGiftAdapter extends RecyclerView.Adapter {

    private List<GameBean> datas;

    public HotRecGameGiftAdapter(List<GameBean> datas) {
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_hot_rec_game_gift, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder= (ViewHolder) holder;
        final GameBean gameBean = datas.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GiftListActivity.start(v.getContext(),gameBean.getGamename(),gameBean.getGameid(),0,0,0,0);
            }
        });
        viewHolder.tvGameName.setText(gameBean.getGamename());
        viewHolder.tvGiftCount.setText("礼包总数:"+gameBean.getGiftcnt());
//        GlideDisplay.display(viewHolder.ivGameImg,gameBean.getIcon(),R.mipmap.ic_launcher);
        Glide.with(viewHolder.context).load(gameBean.getIcon()).placeholder(R.mipmap.ic_launcher).into(viewHolder.ivGameImg);
    }

    @Override
    public int getItemCount() {
        if(datas==null) return 0;
        return datas.size()>6?6:datas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_game_img)
        RoundedImageView ivGameImg;
        @BindView(R.id.tv_game_name)
        TextView tvGameName;
        @BindView(R.id.tv_gift_count)
        TextView tvGiftCount;
        Context context;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            context = view.getContext();
        }
    }
}
