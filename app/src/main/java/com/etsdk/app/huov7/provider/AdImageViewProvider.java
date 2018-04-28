package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.AdImage;
import com.etsdk.app.huov7.view.AdImageView;
import com.liang530.utils.BaseAppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2016/12/21.
 */
public class AdImageViewProvider
        extends ItemViewProvider<AdImage, AdImageViewProvider.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_ad_image, parent, false);
        root.measure(0, 0);
        int measuredWidth = BaseAppUtil.getDeviceWidth(parent.getContext());
        int height = (int) (measuredWidth * AppApi.AD_IMAGE_HW_RATA);
        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        } else {
            layoutParams.height = height;
        }
        root.setLayoutParams(layoutParams);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final AdImage adImage) {
        holder.ivAd.setAdImage(adImage);
        if(!adImage.isRequestPadding()){
            holder.itemView.setPadding(0,0,0,0);
        }else{
            holder.itemView.setPadding(BaseAppUtil.dip2px(holder.itemView.getContext(),12),0,BaseAppUtil.dip2px(holder.itemView.getContext(),12),0);
        }
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        if(adImage.isRequestBottomMargin()){//设置是否需要底部margin
            layoutParams.bottomMargin=BaseAppUtil.dip2px(holder.itemView.getContext(),8);
        }else{
            layoutParams.bottomMargin=BaseAppUtil.dip2px(holder.itemView.getContext(),0);
        }
        holder.itemView.setLayoutParams(layoutParams);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_ad)
        AdImageView ivAd;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}