package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.ui.GameDetailActivity;
import com.etsdk.app.huov7.ui.GiftListActivity;
import com.liang530.views.refresh.mvc.IDataAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/12/10.
 * FuliGiftActivity的adapter
 */

public class FuliGiftAdapter extends RecyclerView.Adapter implements IDataAdapter {
    public static final int MODULE_TOP = 0;
    public static final int HOT_REC_LIST = 1;
    public static final int HOT_GIFT_LIST = 2;
    public static final int NEW_GIFT_HEAD = 3;
    public static final int NEW_GIFT_LIST = 4;
    private int moduleTopSize = 1;
    private int hotRecSize = 1;
    private int hotGiftSize = 1;
    private int newGiftHeadSize = 1;
    private int newGiftSize = 5;
    private Context context;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        context = parent.getContext();
        switch (viewType) {
            case MODULE_TOP:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_fuli_gift_top, parent, false);
                return new ModuleTopViewHolder(view);
            case HOT_REC_LIST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_fuli_gift_hotrec, parent, false);
                return new HotRecViewHolder(view);
            case HOT_GIFT_LIST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_fuli_gift_hotgift, parent, false);
                return new HotGiftListViewHolder(view);
            case NEW_GIFT_HEAD:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_fuli_gift_newgift, parent, false);
                return new NewGiftHeadViewHolder(view);
            case NEW_GIFT_LIST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gift, parent, false);
                return new GiftViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e("hongliang", "hold=" + holder);
        if (holder instanceof ModuleTopViewHolder) {
            ModuleTopViewHolder moduleTopViewHolder = (ModuleTopViewHolder) holder;
            moduleTopViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GameDetailActivity.start(v.getContext());
                }
            });
        } else if (holder instanceof HotRecViewHolder) {
            HotRecViewHolder hotRecViewHolder = (HotRecViewHolder) holder;
            hotRecViewHolder.recyclerview.setLayoutManager(new GridLayoutManager(context,3));
            hotRecViewHolder.recyclerview.setNestedScrollingEnabled(false);
//            hotRecViewHolder.recyclerview.setAdapter(new HotRecGameGiftAdapter());
            hotRecViewHolder.ivHotRecImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GameDetailActivity.start(v.getContext());
                }
            });
        } else if (holder instanceof HotGiftListViewHolder) {
            HotGiftListViewHolder hotGiftListViewHolder = (HotGiftListViewHolder) holder;
            hotGiftListViewHolder.recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            hotGiftListViewHolder.recyclerview.setNestedScrollingEnabled(false);
            hotGiftListViewHolder.recyclerview.setAdapter(new GiftListAdapter());
            hotGiftListViewHolder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GiftListActivity.start(v.getContext(),"热门礼包",null,2,0,0,0);
                }
            });
            hotGiftListViewHolder.ivHotImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GameDetailActivity.start(v.getContext());
                }
            });
        } else if (holder instanceof NewGiftHeadViewHolder) {
            NewGiftHeadViewHolder newGiftHeadViewHolder = (NewGiftHeadViewHolder) holder;
            newGiftHeadViewHolder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GiftListActivity.start(v.getContext(),"最新礼包",null,0,2,0,0);
                }
            });
        }else if(holder instanceof GiftViewHolder){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    GiftDetailActivity.start(v.getContext());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < moduleTopSize) {
            return MODULE_TOP;
        }
        if (position < moduleTopSize + hotRecSize) {
            return HOT_REC_LIST;
        }
        if (position < moduleTopSize + hotRecSize + hotGiftSize) {
            return HOT_GIFT_LIST;
        }
        if (position < moduleTopSize + hotRecSize + hotGiftSize+newGiftHeadSize) {
            return NEW_GIFT_HEAD;
        }
        return NEW_GIFT_LIST;
    }

    @Override
    public int getItemCount() {
        int size = moduleTopSize + hotRecSize + hotGiftSize
                + newGiftHeadSize+newGiftSize;
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

    static class ModuleTopViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_top_img)
        ImageView ivTopImg;

        ModuleTopViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class HotRecViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recyclerview)
        RecyclerView recyclerview;
        @BindView(R.id.iv_hot_rec_img)
        ImageView ivHotRecImg;
        HotRecViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class HotGiftListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_more)
        TextView tvMore;
        @BindView(R.id.recyclerview)
        RecyclerView recyclerview;
        @BindView(R.id.iv_hot_img)
        ImageView ivHotImg;

        HotGiftListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class NewGiftHeadViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_more)
        TextView tvMore;

        NewGiftHeadViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    static class GiftViewHolder extends RecyclerView.ViewHolder{
        GiftViewHolder(View view) {
            super(view);
        }
    }
}
