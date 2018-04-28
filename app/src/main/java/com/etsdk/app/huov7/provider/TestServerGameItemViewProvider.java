package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.etsdk.app.huov7.model.TestGameBean;
import com.etsdk.app.huov7.view.NewListGameItem;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2016/12/21.
 */
public class TestServerGameItemViewProvider
        extends ItemViewProvider<TestGameBean, TestServerGameItemViewProvider.ViewHolder> {
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm ");
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(new NewListGameItem(parent.getContext()));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull TestGameBean gameBean) {
        holder.listGameItem.setGameBean(gameBean);
//        holder.listGameItem.showTimeLine(true, !isViewTypeEnd(gameBean));
        //设置开服状态信息显示
        String startTime=null;
        try {
            startTime = sdf.format(new Date(Long.parseLong(gameBean.getStarttime())*1000));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String testInfo="1".equals(gameBean.getStatus())?"删档内测":"不删档内测";
        if(startTime==null){
            holder.listGameItem.setGameStatusInfo(testInfo,null);
        }else{
            holder.listGameItem.setGameStatusInfo(startTime+testInfo,null);
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