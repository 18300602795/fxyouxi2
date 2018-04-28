package com.etsdk.app.huov7.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.BuildConfig;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.AdImage;
import com.etsdk.app.huov7.model.TjAdTop;
import com.etsdk.app.huov7.ui.BackRecordActivity;
import com.etsdk.app.huov7.ui.CouponDetailActivity;
import com.etsdk.app.huov7.ui.EarnActivity;
import com.etsdk.app.huov7.ui.FuliGiftActivity;
import com.etsdk.app.huov7.ui.GameDetailV2Activity;
import com.etsdk.app.huov7.ui.GameFirstClassifyActivity;
import com.etsdk.app.huov7.ui.GameTestNewActivity;
import com.etsdk.app.huov7.ui.GiftDetailActivity;
import com.etsdk.app.huov7.ui.MainActivity;
import com.etsdk.app.huov7.ui.WebViewActivity;
import com.liang530.views.convenientbanner.ConvenientBanner;
import com.liang530.views.convenientbanner.holder.CBViewHolderCreator;
import com.liang530.views.convenientbanner.listener.OnItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2016/12/21.
 * 首页推荐的顶部栏
 */
public class TjAdTopViewProvider
        extends ItemViewProvider<TjAdTop, TjAdTopViewProvider.ViewHolder> {
    private int bannerHeight;

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_tj_module_top, parent, false);
        ViewHolder viewHolder = new ViewHolder(root);
        if (bannerHeight != 0) {
            ViewGroup.LayoutParams layoutParams = viewHolder.rlBanner.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, bannerHeight);
            } else {
                layoutParams.height = bannerHeight;
            }
            viewHolder.rlBanner.setLayoutParams(layoutParams);
        }
        return viewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final TjAdTop tjAdTop) {
        final Context context = holder.itemView.getContext();
        if (context instanceof MainActivity) {
            holder.llHotGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ((MainActivity) context).switchGameFragment(1);
//                    new CouponExchangeDialogUtil().showExchangeDialog(context, "友情提示", "该功能正在开发中，敬请期待");
                    BackRecordActivity.start(context);
                }
            });
            holder.llNewGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ((MainActivity) context).switchGameFragment(0);
//                    GameListActivity.start(context, "GM榜单", true, true, 0, 0, 0, 0, 0, 0, 1, null);
                    GameFirstClassifyActivity.start(context);
                }
            });
            holder.llStartGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ((MainActivity) context).switchGameTestFragment();
                    GameTestNewActivity.start(context);
                }
            });
            holder.llGift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FuliGiftActivity.start(v.getContext());
                }
            });
            holder.llH5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    WebViewActivity.start(v.getContext(), "在线玩", AppApi.testUrl, WebViewActivity.TYPE_STATUS_NO_TITLE);
//                new CouponExchangeDialogUtil().showExchangeDialog(context, "友情提示", "该功能正在开发中，敬请期待");
                    EarnActivity.start(context);
                }
            });
        }
        holder.convenientBanner.setPages(new CBViewHolderCreator<NetImageHolderView>() {
            @Override
            public NetImageHolderView createHolder() {
                return new NetImageHolderView();
            }
        }, tjAdTop.getBannerList())
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        AdImage adImage = tjAdTop.getBannerList().get(position);
                        if ("1".equals(adImage.getType())) {
                            WebViewActivity.start(context, "", adImage.getUrl());
                        } else if ("2".equals(adImage.getType())) {
                            GameDetailV2Activity.start(context, adImage.getTarget() + "");
                        } else if ("3".equals(adImage.getType())) {
                            GiftDetailActivity.start(context, adImage.getTarget() + "");
                        } else if ("4".equals(adImage.getType())) {
                            CouponDetailActivity.start(context, adImage.getTarget() + "");
                        }
                    }
                });
        //TODO 切换监听，用于改变标题颜色
//        if(holder.convenientBanner.getOnPageChangeListener()!=null) {
//            holder.convenientBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                @Override
//                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//                }
//
//                @Override
//                public void onPageSelected(int position) {
//
//                }
//
//                @Override
//                public void onPageScrollStateChanged(int state) {
//
//                }
//            });
//        }
        if (!holder.convenientBanner.isTurning()) {
            if (tjAdTop.getBannerList().size() > 1) {
                holder.convenientBanner.startTurning(3000);
            } else {
                holder.convenientBanner.stopTurning();
            }
        }
    }

    public void setBannerHeight(int bannerHeight) {
        this.bannerHeight = bannerHeight;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rl_goto_msg)
        RelativeLayout rlGotoMsg;
        @BindView(R.id.convenientBanner)
        ConvenientBanner convenientBanner;
        @BindView(R.id.iv_tj_downManager)
        ImageView ivTjDownManager;
        @BindView(R.id.main_tjSearch)
        TextView mainTjSearch;
        @BindView(R.id.tv_hot_game)
        TextView tv_hot_game;
        @BindView(R.id.ll_hotGame)
        LinearLayout llHotGame;
        @BindView(R.id.ll_newGame)
        LinearLayout llNewGame;
        @BindView(R.id.ll_startGame)
        LinearLayout llStartGame;
        @BindView(R.id.ll_gift)
        LinearLayout llGift;
        @BindView(R.id.rl_banner)
        RelativeLayout rlBanner;
        @BindView(R.id.ll_h5)
        LinearLayout llH5;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (BuildConfig.projectCode == 200) {
                llH5.setVisibility(View.VISIBLE);
            } else {
                llH5.setVisibility(View.VISIBLE);
            }
            if (BuildConfig.projectCode == 137) {
                tv_hot_game.setText("返利");
            } else {
                tv_hot_game.setText("返利");
            }
        }
    }
}