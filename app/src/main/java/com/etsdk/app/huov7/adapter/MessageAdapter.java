package com.etsdk.app.huov7.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etsdk.app.huov7.R;
import com.liang530.views.refresh.mvc.IDataAdapter;

/**
 * Created by liu hong liang on 2016/12/6.
 * 消息中心的adapter
 *
 */

public class MessageAdapter extends RecyclerView.Adapter implements IDataAdapter{

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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
