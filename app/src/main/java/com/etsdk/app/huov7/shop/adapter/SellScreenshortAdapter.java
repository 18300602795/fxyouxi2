package com.etsdk.app.huov7.shop.adapter;

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
 * 角色交易截图
 * Created by liu hong liang on 2016/10/11.
 */

public class SellScreenshortAdapter extends RecyclerView.Adapter<SellScreenshortAdapter.ViewHolder> {
    private ArrayList<String> data=new ArrayList();
    public SellScreenshortAdapter(ArrayList<String> data) {
        this.data = data;
    }
    @Override
    public SellScreenshortAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_sell_screenshort, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(SellScreenshortAdapter.ViewHolder holder, final int position) {
        Glide.with(holder.itemView.getContext()).load(data.get(position)).error(R.mipmap.ic_launcher).into(holder.ivGameImg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPicVPActivity.start(v.getContext(),data,position,false);
            }
        });
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
}
