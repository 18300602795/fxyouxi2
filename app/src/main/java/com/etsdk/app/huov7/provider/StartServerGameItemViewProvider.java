package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.etsdk.app.huov7.model.StartServerGameBean;
import com.etsdk.app.huov7.view.NewListGameItem;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2016/12/21.
 */
public class StartServerGameItemViewProvider
        extends ItemViewProvider<StartServerGameBean, StartServerGameItemViewProvider.ViewHolder> {
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm ");

    public StartServerGameItemViewProvider() {
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(new NewListGameItem(parent.getContext()));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull StartServerGameBean gameBean) {
        holder.listGameItem.setGameBean(gameBean);
//        holder.listGameItem.showTimeLine(true, !isViewTypeEnd(gameBean));
        //设置开服状态信息显示
        String startTime=null;
        try {
            startTime = sdf.format(new Date(Long.parseLong(gameBean.getStarttime())*1000));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if(startTime==null){
            holder.listGameItem.setGameStatusInfo(gameBean.getSername(),null);
        }else{
            holder.listGameItem.setGameStatusInfo(startTime+gameBean.getSername(),null);
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