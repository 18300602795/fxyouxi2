package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.ScoreShopOption;
import com.etsdk.app.huov7.ui.DoScoreTaskActivity;
import com.etsdk.app.huov7.ui.ScoreRankActivity;
import com.etsdk.app.huov7.ui.UpdateAddressActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/22.
 */
public class ScoreShopOptionViewProvider
        extends ItemViewProvider<ScoreShopOption, ScoreShopOptionViewProvider.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_score_shop_option, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ScoreShopOption scoreShopOption) {
        holder.llScoreRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScoreRankActivity.start(v.getContext());
            }
        });
        holder.llGetScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoScoreTaskActivity.start(v.getContext());
            }
        });
        holder.tvGoAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAddressActivity.start(v.getContext());
            }
        });
        holder.tvScoreNum.setText(scoreShopOption.getMyintegral());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_scoreHint)
        TextView tvScoreHint;
        @BindView(R.id.tv_scoreNum)
        TextView tvScoreNum;
        @BindView(R.id.ll_get_score)
        LinearLayout llGetScore;
        @BindView(R.id.ll_score_rank)
        LinearLayout llScoreRank;
        @BindView(R.id.tv_goAddress)
        TextView tvGoAddress;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}