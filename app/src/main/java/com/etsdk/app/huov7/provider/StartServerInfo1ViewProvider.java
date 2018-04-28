package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.StartServerInfo1;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/14.
 */
public class StartServerInfo1ViewProvider
        extends ItemViewProvider<StartServerInfo1, StartServerInfo1ViewProvider.ViewHolder> {
    private View.OnClickListener onClickListener;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm ");


    public StartServerInfo1ViewProvider(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_start_server_info1, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull StartServerInfo1 startServerInfo1) {
        holder.itemView.setOnClickListener(onClickListener);
        String startTime=null;
        try {
            startTime = sdf.format(new Date(Long.parseLong(startServerInfo1.getStarttime())*1000));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if(startTime!=null){
            holder.tvInfo.setText(startTime+startServerInfo1.getSername());
        }else{
            holder.tvInfo.setText(startServerInfo1.getSername());
        }
        if("1".equals(startServerInfo1.getStatus())){
            holder.tvInfo.setTextColor(holder.itemView.getResources().getColor(R.color.text_black));
        }else{
            holder.tvInfo.setTextColor(holder.itemView.getResources().getColor(R.color.text_gray));
        }
        if(startServerInfo1.getArrowShowType()==0){
            holder.ivSwitchMore.setVisibility(View.INVISIBLE);
        }else{
            holder.ivSwitchMore.setVisibility(View.VISIBLE);
            if(startServerInfo1.getArrowShowType()==1){
                holder.ivSwitchMore.setImageResource(R.mipmap.smsxia);
            }else{
                holder.ivSwitchMore.setImageResource(R.mipmap.smssan);
            }
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_info)
        TextView tvInfo;
        @BindView(R.id.iv_switch_more)
        ImageView ivSwitchMore;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}