package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.SignInResultBean;
import com.etsdk.app.huov7.ui.SignInActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/12/9.
 */

public class SignInAdapter extends RecyclerView.Adapter {

    private List<SignInResultBean.SignData> datas;
    private Integer firstSignDay = Integer.MAX_VALUE;//第一个可以被签到的天

    public SignInAdapter(List<SignInResultBean.SignData> datas) {
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_sign_in, parent, false);
        view.measure(0, 0);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.ivSignInBorder.getLayoutParams().height = view.getMeasuredHeight();
        viewHolder.ivSignInBorder.requestLayout();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvSignDay.setText(datas.get(position).getDay() + "");
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();
                if (context instanceof SignInActivity) {
                    ((SignInActivity) context).userSign(datas.get(holder.getAdapterPosition()),
                            datas.get(holder.getAdapterPosition()).getIntegral());
                }
            }
        });
        if ("2".equals(datas.get(position).getSigned())) {
//            viewHolder.ivSignIn.setImageResource(R.mipmap.ic_sign_in_chicken);
            viewHolder.tvShowScore.setText("+"+datas.get(position).getIntegral()+"积分");
            viewHolder.tvShowScore.setVisibility(View.VISIBLE);
            viewHolder.ivSignIn.setVisibility(View.INVISIBLE);
            viewHolder.ivSignInBorder.setVisibility(View.VISIBLE);
            viewHolder.itemView.setEnabled(false);
        } else {
            viewHolder.tvShowScore.setVisibility(View.INVISIBLE);
            viewHolder.ivSignIn.setImageResource(R.mipmap.jidan);
            viewHolder.ivSignInBorder.setVisibility(View.GONE);
            viewHolder.ivSignIn.setVisibility(View.VISIBLE);
            if (firstSignDay == datas.get(position).getDay()) {//第一个没签过的天可以签到
                viewHolder.itemView.setEnabled(true);
            } else {
                viewHolder.itemView.setEnabled(false);
            }
        }
    }

    /**
     * 查找第一个没签到的天
     *
     * @return
     */
    private int findFirstCanSignDay() {
        for (int i = 0; i < datas.size(); i++) {
            if (!"2".equals(datas.get(i).getSigned())) {
                return datas.get(i).getDay();
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        firstSignDay = findFirstCanSignDay();
        return datas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_signIn)
        ImageView ivSignIn;
        @BindView(R.id.tv_sign_day)
        TextView tvSignDay;
        @BindView(R.id.iv_signInBorder)
        ImageView ivSignInBorder;
        @BindView(R.id.tv_show_score)
        TextView tvShowScore;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
