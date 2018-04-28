package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.Goods;
import com.etsdk.app.huov7.ui.EntityDetailActivity;
import com.etsdk.app.huov7.ui.NewScoreShopActivity;
import com.game.sdk.util.GsonUtil;
import com.liang530.utils.GlideDisplay;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2016/12/30.
 */
public class EntityGridViewProvider extends ItemViewProvider<Goods, EntityGridViewProvider.ViewHolder> {
    private List<Goods> goodsList;
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_score_shop_entity, parent, false);
        if(parent.getContext() instanceof NewScoreShopActivity){//是积分商城界面
            goodsList = ((NewScoreShopActivity) parent.getContext()).getGoodsList();
        }
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Goods goods) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntityDetailActivity.start(v.getContext(), GsonUtil.getGson().toJson(goods));
            }
        });
        holder.tvName.setText(goods.getGoodsname());
        holder.tvScoreNum.setText(goods.getIntegral() + "积分");
        GlideDisplay.dispalyWithFitCenterDef(holder.tvImage, goods.getOriginal_img(), R.mipmap.ic_launcher);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_image)
        ImageView tvImage;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_scoreNum)
        TextView tvScoreNum;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}