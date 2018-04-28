package com.etsdk.app.huov7.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.ChargeRecordListBean;
import com.etsdk.app.huov7.model.SpendRecordListBean;
import com.liang530.views.refresh.mvc.IDataAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/9/26.
 */

public class SpendRecordRcyAadapter extends RecyclerView.Adapter implements IDataAdapter<List<ChargeRecordListBean.DataBean>> {
    List<ChargeRecordListBean.DataBean> listBeen = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GameListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spend_record, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((GameListViewHolder) holder).tvGameName.setText(listBeen.get(position).getGamename());
        ((GameListViewHolder) holder).tvCostYuan.setText(listBeen.get(position).getAmount() + "");
        ((GameListViewHolder) holder).tvPayType.setText(listBeen.get(position).getPaytype());
        ((GameListViewHolder) holder).tvOrderid.setText(listBeen.get(position).getOrderid());
        ((GameListViewHolder) holder).tvTime.setText(listBeen.get(position).getPay_time());
        ((GameListViewHolder) holder).tvOrderState.setText(listBeen.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return listBeen.size();
    }

    @Override
    public void notifyDataChanged(List<ChargeRecordListBean.DataBean> listBeen, boolean isRefresh) {
        if (isRefresh) {
            this.listBeen.clear();
        }
        this.listBeen.addAll(listBeen);
        notifyDataSetChanged();
    }

    @Override
    public List<ChargeRecordListBean.DataBean> getData() {
        return listBeen;
    }

    @Override
    public boolean isEmpty() {
        return listBeen.isEmpty();
    }

    static class GameListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_game_name)
        TextView tvGameName;
        @BindView(R.id.tv_cost_yuan)
        TextView tvCostYuan;
        @BindView(R.id.tv_pay_type)
        TextView tvPayType;
        @BindView(R.id.tv_order_state)
        TextView tvOrderState;
        @BindView(R.id.tv_orderid)
        TextView tvOrderid;
        @BindView(R.id.tv_time)
        TextView tvTime;

        GameListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
