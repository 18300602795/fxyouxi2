package com.etsdk.app.huov7.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etsdk.app.huov7.R;
import com.liang530.views.refresh.mvc.IDataAdapter;

/**
 * Created by liu hong liang on 2016/12/6.
 */

public class MineEntityListAdapter extends RecyclerView.Adapter implements IDataAdapter{

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mine_entity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EntityDetailActivity.start(v.getContext());
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
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

    static class ViewHolder extends RecyclerView.ViewHolder{
        ViewHolder(View view) {
            super(view);
        }
    }
}
