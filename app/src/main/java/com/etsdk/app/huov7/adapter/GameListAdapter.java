package com.etsdk.app.huov7.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.view.NewListGameItem;
import com.liang530.views.refresh.mvc.IDataAdapter;

/**
 * Created by liu hong liang on 2016/12/6.
 */

public class GameListAdapter extends RecyclerView.Adapter implements IDataAdapter{
    private boolean requestTopSplit =false;//是否需要顶部分割线
    private boolean showRank=false;
    private final static int TYPE_SPLIT=0;
    private final static int TYPE_ITEM=1;

    public void setRequestTopSplit(boolean requestTopSplit,boolean showRank) {
        this.requestTopSplit = requestTopSplit;
        this.showRank=showRank;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_ITEM){
            return new GameListAdapter.ItemViewHolder(new NewListGameItem(parent.getContext()));
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_split_line, parent, false);
            return new GameListAdapter.SplitViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof GameListAdapter.ItemViewHolder){
            GameListAdapter.ItemViewHolder itemViewHolder= (ItemViewHolder) holder;
            int index = position - (requestTopSplit ? 1 : 0);
            itemViewHolder.listGameItem.showLine(index!=0);
            itemViewHolder.listGameItem.setIsHotRank(showRank,index+1);
        }
    }

    @Override
    public int getItemCount() {
        if(requestTopSplit){
            return 1+3;
        }
        return 3;
    }

    @Override
    public void notifyDataChanged(Object o, boolean b) {

    }

    @Override
    public int getItemViewType(int position) {
        if(requestTopSplit &&position==0){
            return TYPE_SPLIT;
        }
        return TYPE_ITEM;

    }

    @Override
    public Object getData() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{
        NewListGameItem listGameItem;
        ItemViewHolder(NewListGameItem view) {
            super(view);
            listGameItem=view;
        }
    }
    static class SplitViewHolder extends RecyclerView.ViewHolder{
        SplitViewHolder(View view) {
            super(view);
        }
    }
}
