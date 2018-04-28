package com.etsdk.app.huov7.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.DoTaskTop;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.etsdk.app.huov7.ui.ScoreRankActivity;
import com.game.sdk.util.GsonUtil;
import com.google.gson.JsonSyntaxException;
import com.liang530.control.LoginControl;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/2/8.
 */
public class DoTaskTopViewProvider
        extends ItemViewProvider<DoTaskTop, DoTaskTopViewProvider.ViewHolder> {


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_do_task_top, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull DoTaskTop doTaskTop) {
        holder.tvScoreRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScoreRankActivity.start(v.getContext());
            }
        });
        holder.tvMyScore.setText(doTaskTop.getMyintegral());
        String userInfo = LoginControl.getKey();
        if(!TextUtils.isEmpty(userInfo)){
            try {
                UserInfoResultBean userInfoResultBean = GsonUtil.getGson().fromJson(userInfo, UserInfoResultBean.class);
                if(userInfoResultBean!=null){
                    holder.tvUserName.setText(userInfoResultBean.getNickname());
//                    GlideDisplay.display(holder.ivMineHead, userInfoResultBean.getPortrait(), R.mipmap.ic_launcher);
                    Glide.with(holder.context).load(userInfoResultBean.getPortrait()).placeholder(R.mipmap.ic_launcher).into(holder.ivMineHead);
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_mineHead)
        RoundedImageView ivMineHead;
        @BindView(R.id.tv_userName)
        TextView tvUserName;
        @BindView(R.id.tv_my_score)
        TextView tvMyScore;
        @BindView(R.id.tv_scoreRank)
        TextView tvScoreRank;
        Context context;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }
    }
}