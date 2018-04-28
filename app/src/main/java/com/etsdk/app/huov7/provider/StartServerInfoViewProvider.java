package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.StartServerInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/14.
 */
public class StartServerInfoViewProvider
        extends ItemViewProvider<StartServerInfo, StartServerInfoViewProvider.ViewHolder> {
    private View.OnClickListener onClickListener;
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm ");
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root= inflater.inflate(R.layout.item_start_server_info, parent, false);
        return new ViewHolder(root);
    }

    public StartServerInfoViewProvider(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull StartServerInfo startServerInfo) {
        String startTime = null;
        holder.itemView.setOnClickListener(onClickListener);
        try {
            startTime = sdf.format(new Date(Long.parseLong(startServerInfo.getStarttime())*1000));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (startTime != null) {
            holder.tvInfo.setText(startTime + startServerInfo.getSername());
        }else{
            holder.tvInfo.setText(startServerInfo.getSername());
        }
        if("1".equals(startServerInfo.getStatus())){
            holder.tvInfo.setTextColor(holder.itemView.getResources().getColor(R.color.text_black));
        }else{
            holder.tvInfo.setTextColor(holder.itemView.getResources().getColor(R.color.text_gray));
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_info)
        TextView tvInfo;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}