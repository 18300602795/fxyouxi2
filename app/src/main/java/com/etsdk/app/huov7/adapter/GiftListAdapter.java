package com.etsdk.app.huov7.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.liang530.views.refresh.mvc.IDataAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/12/6.
 */

public class GiftListAdapter extends RecyclerView.Adapter implements IDataAdapter {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gift, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                GiftDetailActivity.start(v.getContext(),);
            }
        });
        GiftListAdapter.ViewHolder viewHolder= (ViewHolder) holder;
//        viewHolder.ivApply.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new DialogGiftUtil().showConvertDialog(v.getContext(), "", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
//            }
//        });
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_gift_name)
        TextView tvGiftName;
        @BindView(R.id.iv_apply)
        ImageButton ivApply;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
