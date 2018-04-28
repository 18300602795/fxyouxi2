package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.AwardRecordTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2016/12/30.
 */
public class AwardRecordTimeViewProvider
        extends ItemViewProvider<AwardRecordTime, AwardRecordTimeViewProvider.ViewHolder> {



    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_award_record_time, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull AwardRecordTime awardRecordTime) {
        holder.tvShowDate.setText(awardRecordTime.getGetTime());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_showDate)
        TextView tvShowDate;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}