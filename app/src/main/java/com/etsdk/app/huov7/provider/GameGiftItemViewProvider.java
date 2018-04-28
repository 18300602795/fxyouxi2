package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.GameGiftItem;
import com.etsdk.app.huov7.view.NewListGameItem;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2016/12/21.
 */
public class GameGiftItemViewProvider
        extends ItemViewProvider<GameGiftItem, GameGiftItemViewProvider.ViewHolder> {
    private boolean isStartServer;
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm ");
    public GameGiftItemViewProvider(boolean isStartServer) {
        this.isStartServer = isStartServer;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(new NewListGameItem(parent.getContext()));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull GameGiftItem gameBean) {
        holder.listGameItem.showLine(!isViewTypeStart(gameBean));
        holder.listGameItem.setGameBean(gameBean);
        holder.listGameItem.setIsHotRank(false,holder.getAdapterPosition());

        String startTime=null;
        try {
            startTime = sdf.format(new Date(Long.parseLong(gameBean.getStarttime())*1000));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if(isStartServer){
            //设置开服状态信息显示
            if(startTime==null){
                holder.listGameItem.setGameStatusInfo(gameBean.getSername(),holder.itemView.getResources().getColor(R.color.text_red));
            }else{
                holder.listGameItem.setGameStatusInfo(startTime+gameBean.getSername(),holder.itemView.getResources().getColor(R.color.text_red));
            }
        }else{       //设置开测状态信息显示
            String testInfo="1".equals(gameBean.getStatus())?"删档内测":"不删档内测";
            if(startTime==null){
                holder.listGameItem.setGameStatusInfo(testInfo,holder.itemView.getResources().getColor(R.color.class_color4));
            }else{
                holder.listGameItem.setGameStatusInfo(startTime+testInfo,holder.itemView.getResources().getColor(R.color.class_color4));
            }
        }
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        NewListGameItem listGameItem;
        ViewHolder(NewListGameItem itemView) {
            super(itemView);
            listGameItem=itemView;
        }
    }
}