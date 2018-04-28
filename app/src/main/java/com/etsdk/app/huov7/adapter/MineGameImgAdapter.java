package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.ui.NewGameDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/10/11.
 */

public class MineGameImgAdapter extends RecyclerView.Adapter<MineGameImgAdapter.ViewHolder> {
    private List<GameBean> gameBeanList=new ArrayList<>();
    public MineGameImgAdapter(List<GameBean> gameBeanList) {
        this.gameBeanList=gameBeanList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mine_game_img, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final GameBean gameBean = gameBeanList.get(position);
        if(gameBean!=null){
//            GlideDisplay.display(holder.ivGameImg,gameBean.getIcon(),R.mipmap.ic_launcher);
            Glide.with(holder.context).load(gameBean.getIcon()).placeholder(R.mipmap.ic_launcher).into(holder.ivGameImg);
        }else{//是空白占位
            holder.ivGameImg.setImageResource(R.color.tranparent);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameBean!=null){
                    NewGameDetailActivity.start(v.getContext(),gameBean.getGameid());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(gameBeanList==null||gameBeanList.size()==0) return 0;
        while (gameBeanList.size()<4){
            gameBeanList.add(null);
        }
        return gameBeanList.size()>4?4:gameBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_game_img)
        ImageView ivGameImg;
        Context context;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            context = view.getContext();
        }
    }
}
