package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.RecommandGridItem;
import com.etsdk.app.huov7.ui.DoScoreTaskActivity;
import com.etsdk.app.huov7.ui.RecommandTaskActivity;
import com.etsdk.app.huov7.ui.SignInActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/12/6.
 */

public class OptionColumnAdapter extends RecyclerView.Adapter<OptionColumnAdapter.ViewHolder> {
    private  Context context;
    private List<RecommandGridItem> datas = new ArrayList<>();

    public OptionColumnAdapter(Context context) {
        this.context=context;
        //图标删掉了
        datas.add(new RecommandGridItem("积分任务", R.mipmap.coupon, "代金券的最佳通道",new Intent(context, DoScoreTaskActivity.class)));
        datas.add(new RecommandGridItem("每日签到", R.mipmap.coupon, "赚积分是种习惯",new Intent(context, SignInActivity.class)));
        datas.add(new RecommandGridItem("推广员系统", R.mipmap.coupon, "与小伙伴一起躁起来",new Intent(context, RecommandTaskActivity.class)));
        datas.add(new RecommandGridItem("代金券", R.mipmap.coupon, "将优惠进行到底",null));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_option_column_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RecommandGridItem recommandGridItem = datas.get(position);
        holder.ivImage.setImageResource(recommandGridItem.getDrawableId());
        holder.tvTitle.setText(recommandGridItem.getTitleName());
        holder.tvDescription.setText(recommandGridItem.getDescription());
        if(recommandGridItem.getIntent()!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.getContext().startActivity(recommandGridItem.getIntent());
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_description)
        TextView tvDescription;
        @BindView(R.id.iv_image)
        ImageView ivImage;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
