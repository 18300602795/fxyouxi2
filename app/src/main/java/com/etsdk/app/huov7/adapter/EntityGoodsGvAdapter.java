package com.etsdk.app.huov7.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.Goods;
import com.etsdk.app.huov7.ui.EntityDetailActivity;
import com.game.sdk.util.GsonUtil;
import com.liang530.utils.BaseAppUtil;
import com.liang530.utils.GlideDisplay;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2017/3/10.
 * 积分商城实物Gridview列表Adapter
 */

public class EntityGoodsGvAdapter extends BaseAdapter {
    private List<Goods> goodsList;

    public EntityGoodsGvAdapter(List<Goods> goodsList) {
        this.goodsList = goodsList;
        if (goodsList != null && goodsList.size() % 2 == 1) {
            goodsList.add(null);
        }
    }

    @Override
    public int getCount() {
        return goodsList == null ? 0 : goodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score_shop_entity, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Goods goods = goodsList.get(position);

        int goodsImageHeight= (BaseAppUtil.getDeviceWidth(parent.getContext())-BaseAppUtil.dip2px(parent.getContext(),13)*2)/4;
        ViewGroup.LayoutParams layoutParams = viewHolder.tvImage.getLayoutParams();
        layoutParams.height=goodsImageHeight;
        viewHolder.tvImage.setLayoutParams(layoutParams);
        viewHolder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goods != null) {
                    EntityDetailActivity.start(v.getContext(), GsonUtil.getGson().toJson(goods));
                }
            }
        });
        if (goods != null) {
            viewHolder.llItem.setVisibility(View.VISIBLE);
            viewHolder.tvName.setText(goods.getGoodsname());
            viewHolder.tvScoreNum.setText(goods.getIntegral() + "积分");
            GlideDisplay.dispalyWithFitCenterDef(viewHolder.tvImage, goods.getOriginal_img(), R.mipmap.ic_launcher);
        }else{
            viewHolder.llItem.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_image)
        ImageView tvImage;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_scoreNum)
        TextView tvScoreNum;
        @BindView(R.id.ll_item)
        LinearLayout llItem;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
