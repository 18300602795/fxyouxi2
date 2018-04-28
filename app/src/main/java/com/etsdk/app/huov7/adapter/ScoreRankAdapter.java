package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.ScoreRankListBean;
import com.liang530.utils.BaseAppUtil;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/12/9.
 */

public class ScoreRankAdapter extends RecyclerView.Adapter {

    private final List<ScoreRankListBean.ScoreRankBean> datas;

    public ScoreRankAdapter(List<ScoreRankListBean.ScoreRankBean> scoreRankBeanList) {
        this.datas = scoreRankBeanList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_score_rank, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ViewGroup.LayoutParams layoutParams = viewHolder.tvHotRank.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if (position < 100) {
            viewHolder.tvHotRank.setBackgroundResource(R.drawable.shape_bg_ring);
            layoutParams.width = layoutParams.height = BaseAppUtil.dip2px(holder.itemView.getContext(), 20);
        } else {
            viewHolder.tvHotRank.setBackgroundColor(Color.WHITE);
            layoutParams.width = layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        viewHolder.tvHotRank.setLayoutParams(layoutParams);


        viewHolder.tvHotRank.setText(getRank(position));


        ScoreRankListBean.ScoreRankBean scoreRankBean = datas.get(position);
        viewHolder.tvNickName.setText(scoreRankBean.getNicename());
        viewHolder.tvScoreNum.setText(scoreRankBean.getIntegral());
//        GlideDisplay.display(viewHolder.ivMineHead, scoreRankBean.getPortrait(), R.mipmap.ic_launcher);
        Glide.with(viewHolder.context).load(scoreRankBean.getPortrait()).placeholder(R.mipmap.ic_launcher).into(viewHolder.ivMineHead);
    }

    private String getRank(int position) {
        if (position == 0) {
            datas.get(position).setRank(1);
            return "1";
        }
        if (datas.get(position - 1).getIntegral() != null
                && datas.get(position - 1).getIntegral().equals(datas.get(position).getIntegral())) {//和上一个人的积分相同，排名一致
            datas.get(position).setRank(datas.get(position - 1).getRank());
            return datas.get(position - 1).getRank() + "";
        } else {
            datas.get(position).setRank(position+1);
            return position+1+ "";
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_hot_rank)
        TextView tvHotRank;
        @BindView(R.id.iv_mineHead)
        RoundedImageView ivMineHead;
        @BindView(R.id.tv_nickName)
        TextView tvNickName;
        @BindView(R.id.tv_scoreNum)
        TextView tvScoreNum;
        Context context;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            context = view.getContext();
        }
    }
}
