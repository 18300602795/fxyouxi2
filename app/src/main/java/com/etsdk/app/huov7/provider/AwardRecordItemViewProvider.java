package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.AwardRecordItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2016/12/30.
 */
public class AwardRecordItemViewProvider
        extends ItemViewProvider<AwardRecordItem, AwardRecordItemViewProvider.ViewHolder> {



    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_award_record_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull AwardRecordItem awardRecordItem) {
        if (isViewTypeEnd(awardRecordItem)) {
            holder.viewAwardLine.setVisibility(View.GONE);
        } else {
            holder.viewAwardLine.setVisibility(View.VISIBLE);
        }
        holder.tvAwardName.setText(awardRecordItem.getSubusername());
        holder.tvShowScore.setText(awardRecordItem.getIntegral()+"积分");
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_award_name)
        TextView tvAwardName;
        @BindView(R.id.tv_showScore)
        TextView tvShowScore;
        @BindView(R.id.view_award_line)
        View viewAwardLine;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}