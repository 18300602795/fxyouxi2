package com.etsdk.app.huov7.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.ui.GameDetailV2Activity;
import com.liang530.utils.GlideDisplay;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/12/9.
 */

public class SearchHotAdapter extends RecyclerView.Adapter {
    private static final int ITEM_IMAGE_GAME=0;
    private static final int ITEM_TEXT_GAME=1;
    private List<GameBean> datas;
    private int imageGameItemSize=3;
    public SearchHotAdapter(List<GameBean> datas) {
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if(viewType==ITEM_IMAGE_GAME){
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_hot_search_img, parent, false);
//            return new ListViewHolder(view);
//        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_hot_search_text, parent, false);
            return new TextViewHolder(view);
//        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameDetailV2Activity.start(v.getContext(),datas.get(position).getGameid());
            }
        });
//        if(holder instanceof ListViewHolder){
//            ListViewHolder viewHolder= (ListViewHolder) holder;
//            viewHolder.tvGameName.setText(datas.get(position).getGamename());
//            GlideDisplay.display(viewHolder.ivGameImg,datas.get(position).getIcon(),R.mipmap.ic_launcher);
//        }else{
            TextViewHolder viewHolder= (TextViewHolder) holder;
            viewHolder.tvGameName.setText(datas.get(position).getGamename());
//        }
    }


//    @Override
//    public int getItemViewType(int position) {
//        if(position-imageGameItemSize<0){
//            return ITEM_IMAGE_GAME;
//        }else{
//            return ITEM_TEXT_GAME;
//        }
//    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

//    static class ListViewHolder extends RecyclerView.ListViewHolder {
//        @BindView(R.id.iv_game_img)
//        RoundedImageView ivGameImg;
//        @BindView(R.id.tv_game_name)
//        TextView tvGameName;
//        ListViewHolder(View view) {
//            super(view);
//            ButterKnife.bind(this, view);
//        }
//    }
    static class TextViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_game_name)
        TextView tvGameName;
        TextViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
