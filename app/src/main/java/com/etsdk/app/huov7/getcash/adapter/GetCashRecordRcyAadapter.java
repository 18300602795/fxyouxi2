package com.etsdk.app.huov7.getcash.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.getcash.model.CommRecordListBean;
import com.liang530.views.refresh.mvc.IDataAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 2017/5/24.
 */

public class GetCashRecordRcyAadapter extends RecyclerView.Adapter implements IDataAdapter<List<CommRecordListBean.DataBean>> {
    List<CommRecordListBean.DataBean> listBeen = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_get_cash_record, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        CommRecordListBean.DataBean dataBean = listBeen.get(position);
        StringBuilder account = new StringBuilder();
        account.append(dataBean.getWay_name()).append(" ");
        if("银行卡".equals(dataBean.getWay_name())){
            account.append(dataBean.getCard().substring(0, 4))
                    .append(" **** ")
                    .append(dataBean.getCard().substring(dataBean.getCard().length()-4, dataBean.getCard().length()));
        }else{
            account.append(dataBean.getCard());
        }
        viewHolder.tvAccount.setText(account.toString());
        viewHolder.tvId.setText(dataBean.getCash_id());
        viewHolder.tvMoney.setText(dataBean.getMoney());
        if("1".equals(dataBean.getStatus())){
            viewHolder.tvCheckStatus.setText("待审核");
            viewHolder.trReason.setVisibility(View.GONE);
            viewHolder.lineReason.setVisibility(View.GONE);
        }else if("2".equals(dataBean.getStatus())){
            viewHolder.tvCheckStatus.setText("待财务审核");
            viewHolder.trReason.setVisibility(View.GONE);
            viewHolder.lineReason.setVisibility(View.GONE);
        }else if("3".equals(dataBean.getStatus())){
            viewHolder.tvCheckStatus.setText("已结算");
            viewHolder.trReason.setVisibility(View.GONE);
            viewHolder.lineReason.setVisibility(View.GONE);
        }else if("4".equals(dataBean.getStatus())){
            viewHolder.tvCheckStatus.setText("财务审核不通过");
            if(!TextUtils.isEmpty(dataBean.getFailreason())) {
                viewHolder.trReason.setVisibility(View.VISIBLE);
                viewHolder.lineReason.setVisibility(View.VISIBLE);
                viewHolder.tvReason.setText(dataBean.getFailreason());
            }else{
                viewHolder.trReason.setVisibility(View.GONE);
                viewHolder.lineReason.setVisibility(View.GONE);
            }
        }
        String time = null;
        try {
            long t = dataBean.getCreate_time() * 1000;
            if (t > 0) {
                time = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date(t));
            }
        } catch (Exception e) {

        }
        viewHolder.tvTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return listBeen.size();
    }

    @Override
    public void notifyDataChanged(List<CommRecordListBean.DataBean> listBeen, boolean isRefresh) {
        if (isRefresh) {
            this.listBeen.clear();
        }
        this.listBeen.addAll(listBeen);
        notifyDataSetChanged();
    }

    @Override
    public List<CommRecordListBean.DataBean> getData() {
        return listBeen;
    }

    @Override
    public boolean isEmpty() {
        return listBeen.isEmpty();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_id)
        TextView tvId;
        @BindView(R.id.tv_account)
        TextView tvAccount;
        @BindView(R.id.tv_money)
        TextView tvMoney;
        @BindView(R.id.tv_check_status)
        TextView tvCheckStatus;
        @BindView(R.id.tv_reason)
        TextView tvReason;
        @BindView(R.id.tr_reason)
        TableRow trReason;
        @BindView(R.id.line_reason)
        View lineReason;
        @BindView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
