package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.TjOptionColumn;
import com.etsdk.app.huov7.ui.CouponListActivity;
import com.etsdk.app.huov7.ui.DoScoreTaskActivity;
import com.etsdk.app.huov7.ui.RecommandTaskActivity;
import com.etsdk.app.huov7.ui.ScoreShopActivity;
import com.etsdk.app.huov7.ui.SignInActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2016/12/21.
 * 首页推荐顶部操作栏
 */
public class TjOptionColumnViewProvider
        extends ItemViewProvider<TjOptionColumn, TjOptionColumnViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_tj_option_column1, parent, false);
        ViewHolder viewHolder = new ViewHolder(root);
        viewHolder.llMakeMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecommandTaskActivity.start(v.getContext());
            }
        });
        viewHolder.llNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SignInActivity.start(v.getContext());//资讯
            }
        });
        viewHolder.llShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScoreShopActivity.start(v.getContext());
            }
        });
        viewHolder.llCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CouponListActivity.start(v.getContext(), "代金券列表", null, 0);
            }
        });
        return viewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull TjOptionColumn tjOptionColumn) {
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_coupon)
        LinearLayout llCoupon;
        @BindView(R.id.ll_shop)
        LinearLayout llShop;
        @BindView(R.id.ll_make_money)
        LinearLayout llMakeMoney;
        @BindView(R.id.ll_news)
        LinearLayout llNews;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}