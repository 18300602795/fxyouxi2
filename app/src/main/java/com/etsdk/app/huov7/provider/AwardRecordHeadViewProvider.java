package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.AwardRecordHead;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2016/12/30.
 */
public class AwardRecordHeadViewProvider
        extends ItemViewProvider<AwardRecordHead, AwardRecordHeadViewProvider.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_award_record_head, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull AwardRecordHead awardRecordHead) {
        holder.tvShowCountHint.setText("您已累计邀请"+awardRecordHead.getCount()+"人");
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_showCountHint)
        TextView tvShowCountHint;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}