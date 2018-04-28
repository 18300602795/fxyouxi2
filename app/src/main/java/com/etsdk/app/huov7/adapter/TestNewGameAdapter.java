package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.util.RecyclerViewNoAnimator;
import com.liang530.views.refresh.mvc.IDataAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/12/12.
 * MainTestNewGameFragment的adapter
 * 首页-游戏-开服开测
 */

public class TestNewGameAdapter extends RecyclerView.Adapter implements IDataAdapter {
    public static final int TOP_BANNER = 0;
    public static final int TAB_HEAD = 1;
    public static final int COMM_ITEM = 3;
    private int topBannerSize = 1;
    private int tabHeadSize = 1;
    private int commItemSize = 3;
    private Context context;
    private boolean isStartTab=true;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        context = parent.getContext();
        switch (viewType) {
            case TOP_BANNER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_main_game_tn_banner, parent, false);
                return new TopBannerViewHolder(view);
            case TAB_HEAD:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_main_game_tn_tab, parent, false);
                return new TabHeadViewHolder(view);
            case COMM_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_main_game_tn_item, parent, false);
                return new ItemViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TopBannerViewHolder) {

        } else if(holder instanceof TabHeadViewHolder){
            final TabHeadViewHolder tabHeadViewHolder = (TabHeadViewHolder) holder;
            tabHeadViewHolder.llStartTab.setSelected(isStartTab);
            tabHeadViewHolder.llTestTab.setSelected(!isStartTab);
            tabHeadViewHolder.llStartTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isStartTab){
                        isStartTab=true;
                        commItemSize=3;
                        notifyDataSetChanged();
//                        int adapterPosition = tabHeadViewHolder.getAdapterPosition();
//                        int itemCount = getItemCount() - adapterPosition;
//                        commItemSize=3;
//                        notifyItemRangeChanged(adapterPosition,itemCount);
                    }
                }
            });
            tabHeadViewHolder.llTestTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isStartTab){
                        isStartTab=false;
                        commItemSize=2;
                        notifyDataSetChanged();
//                        int adapterPosition = tabHeadViewHolder.getAdapterPosition();
//                        int itemCount = getItemCount() - adapterPosition;
//                        commItemSize=2;
//                        notifyItemRangeChanged(adapterPosition,itemCount);

                    }
                }
            });
        } else if (holder instanceof ItemViewHolder) {
            ItemViewHolder ItemViewHolder = (ItemViewHolder) holder;
            ItemViewHolder.recyclerview.setLayoutManager(new LinearLayoutManager(context));
            ItemViewHolder.recyclerview.setNestedScrollingEnabled(false);
            ItemViewHolder.recyclerview.setItemAnimator(new RecyclerViewNoAnimator());
            ItemViewHolder.recyclerview.setAdapter(new GameListAdapter());
        }
    }
    public void switchStartTab(boolean isStartTab){
        this.isStartTab=isStartTab;
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        if (position < topBannerSize) {
            return TOP_BANNER;
        }
        if (position < topBannerSize + tabHeadSize) {
            return TAB_HEAD;
        }
        return COMM_ITEM;
    }

    @Override
    public int getItemCount() {
        int size = topBannerSize + tabHeadSize + commItemSize;
        return size;
    }

    @Override
    public void notifyDataChanged(Object o, boolean b) {

    }

    @Override
    public Object getData() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    static class TopBannerViewHolder extends RecyclerView.ViewHolder {
        TopBannerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class TabHeadViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_start_tab)
        LinearLayout llStartTab;
        @BindView(R.id.ll_test_tab)
        LinearLayout llTestTab;
        TabHeadViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recyclerview)
        RecyclerView recyclerview;
        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
