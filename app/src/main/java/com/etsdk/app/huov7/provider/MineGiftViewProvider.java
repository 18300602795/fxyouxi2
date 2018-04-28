package com.etsdk.app.huov7.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.GiftListItem;
import com.etsdk.app.huov7.ui.GiftDetailActivity;
import com.game.sdk.log.T;
import com.liang530.utils.BaseAppUtil;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/13.
 */
public class MineGiftViewProvider
        extends ItemViewProvider<GiftListItem, MineGiftViewProvider.ViewHolder> {
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_mine_gift, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final GiftListItem mineGift) {
//        GlideDisplay.display(holder.ivGameImg,mineGift.getIcon(),R.mipmap.ic_launcher);
        Glide.with(holder.context).load(mineGift.getIcon()).placeholder(R.mipmap.ic_launcher).into(holder.ivGameImg);
        holder.tvGiftName.setText(mineGift.getGiftname());
        holder.tvGiftCode.setText(mineGift.getGiftcode());
        String startTime=null;
        try {
             startTime = simpleDateFormat.format(Long.parseLong(mineGift.getStarttime())*1000);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String endTime=null;
        try {
            endTime=simpleDateFormat.format(Long.parseLong(mineGift.getEnttime())*1000);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if(startTime!=null&&endTime!=null){
            holder.tvEndTime.setText("有效期："+startTime+"至"+endTime);
        }else if(endTime!=null){
            holder.tvEndTime.setText("有效期：截止至"+endTime);
        }
        holder.btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseAppUtil.copyToSystem(v.getContext(),mineGift.getGiftcode());
                T.s(v.getContext(),"复制成功");
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GiftDetailActivity.start(v.getContext(), mineGift.getGiftid());
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_game_img)
        RoundedImageView ivGameImg;
        @BindView(R.id.tv_gift_name)
        TextView tvGiftName;
        @BindView(R.id.tv_gift_code)
        TextView tvGiftCode;
        @BindView(R.id.tv_endTime)
        TextView tvEndTime;
        @BindView(R.id.btn_option)
        Button btnOption;
        @BindView(R.id.v_line)
        View vLine;
        Context context;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            context = itemView.getContext();
        }
    }
}