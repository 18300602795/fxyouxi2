package com.etsdk.app.huov7.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.liang530.photopicker.ShowPicVPActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/10/11.
 */

public class GameImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<String> data=new ArrayList();
    public GameImageAdapter(ArrayList<String> data) {
        this.data = data;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1){
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_game_image, parent, false);
            return new ViewHolder(inflate);
        }else{
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_game_vedio, parent, false);
            return new VideoHolder(inflate);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
//        GlideDisplay.display(holder.ivGameImg,data.get(position),R.mipmap.portrait_load);
        if (holder instanceof ViewHolder){
            Glide.with(((ViewHolder)holder).ivGameImg.getContext()).load(data.get(position)).dontAnimate().placeholder(R.mipmap.gg).into(((ViewHolder)holder).ivGameImg);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowPicVPActivity.start(v.getContext(),data,position,false);
                }
            });
        }else if (holder instanceof VideoHolder){
            Glide.with(((VideoHolder)holder).ivGameImg.getContext()).load(data.get(position)).dontAnimate().placeholder(R.mipmap.gg).into(((VideoHolder)holder).ivGameImg);
            ((VideoHolder)holder).player_iv.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ShowVideoActivity.start(v.getContext());
//                    JCVideoPlayerStandard.startFullscreen(v.getContext(), JCVideoPlayerStandard.class, "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4", "");
                    ShowPicVPActivity.start(v.getContext(),data,position,false);
                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return  2;
        }else {
            return  1;
        }
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_game_img)
        ImageView ivGameImg;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class VideoHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_game_img)
        ImageView ivGameImg;
        @BindView(R.id.player_iv)
        ImageView player_iv;
        public VideoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
