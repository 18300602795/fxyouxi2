package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brioal.adtextviewlib.entity.AdEntity;
import com.brioal.adtextviewlib.view.ADTextView;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.AdImage;
import com.etsdk.app.huov7.model.TjAdText;
import com.etsdk.app.huov7.ui.CouponDetailActivity;
import com.etsdk.app.huov7.ui.GameDetailV2Activity;
import com.etsdk.app.huov7.ui.GiftDetailActivity;
import com.etsdk.app.huov7.ui.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2016/12/21.
 */
public class TjAdTextViewProvider
        extends ItemViewProvider<TjAdText, TjAdTextViewProvider.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_recommand_ad_text, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final TjAdText tjAdText) {
        List<AdEntity> list = new ArrayList();
        final List<AdImage> adTextList = tjAdText.getAdTextList();
        for (int i = 0; i < adTextList.size(); i++) {
            list.add(new AdEntity(adTextList.get(i).getName(), "", "" + i));
        }
        if (list.size() > 0) {
            holder.adTextview.setTexts(list);
            holder.adTextview.setOnItemClickListener(new ADTextView.OnItemClickListener() {
                @Override
                public void onClick(String position) {
                    AdImage adImage = adTextList.get(Integer.parseInt(position));
                    if ("1".equals(adImage.getType())) {
                        WebViewActivity.start(holder.itemView.getContext(), "", adImage.getUrl());
                    } else if ("2".equals(adImage.getType())) {
                        GameDetailV2Activity.start(holder.itemView.getContext(), adImage.getTarget() + "");
                    } else if ("3".equals(adImage.getType())) {
                        GiftDetailActivity.start(holder.itemView.getContext(), adImage.getTarget() + "");
                    } else if ("4".equals(adImage.getType())) {
                        CouponDetailActivity.start(holder.itemView.getContext(), adImage.getTarget() + "");
                    }
                }
            });
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ad_textview)
        ADTextView adTextview;
        @BindView(R.id.tv_more)
        TextView tvMore;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}