package com.etsdk.app.huov7.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.ChargeRecordListBean;
import com.etsdk.app.huov7.ui.fragment.ChargeRecordFragment;
import com.liang530.views.refresh.mvc.IDataAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/9/26.
 */

public class ChargeRecordRcyAadapter extends RecyclerView.Adapter implements IDataAdapter<List<ChargeRecordListBean.DataBean>> {
    private final int type;
    List<ChargeRecordListBean.DataBean> listBeen = new ArrayList<>();

    public ChargeRecordRcyAadapter(int type) {
        this.type = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GameListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_charge_record, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(type == ChargeRecordFragment.TYPE_PTB){
            ((GameListViewHolder) holder).trGame.setVisibility(View.GONE);
            ((GameListViewHolder) holder).vGameLine.setVisibility(View.GONE);
        }else {
            ((GameListViewHolder) holder).trGame.setVisibility(View.VISIBLE);
            ((GameListViewHolder) holder).vGameLine.setVisibility(View.VISIBLE);
            ((GameListViewHolder) holder).tvGameName.setText(listBeen.get(position).getGamename());
        }
        ((GameListViewHolder) holder).tvCostYuan.setText(listBeen.get(position).getAmount() + "");
        ((GameListViewHolder) holder).tvPayType.setText(listBeen.get(position).getPaytype());
        ((GameListViewHolder) holder).tvOrderid.setText(listBeen.get(position).getOrderid());
//        String t = listBeen.get(position).getPay_time() + "000";
//        ((GameListViewHolder) holder).tvTime.setText(new SimpleDateFormat("yyyy/MM/dd hh:mm").format(new Date(t)));
        ((GameListViewHolder) holder).tvTime.setText(listBeen.get(position).getPay_time());
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
        @BindView(R.id.tv_cost_yuan)
        TextView tvCostYuan;
        @BindView(R.id.tv_game_name)
        TextView tvGameName;
        @BindView(R.id.tv_pay_type)
        TextView tvPayType;
        @BindView(R.id.tv_orderid)
        TextView tvOrderid;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tr_game)
        TableRow trGame;
        @BindView(R.id.v_game_line)
        View vGameLine;
        GameListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
