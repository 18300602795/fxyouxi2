package com.etsdk.app.huov7.getcash.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.getcash.model.AccountListBean;
import com.etsdk.app.huov7.getcash.ui.AddAccountActivity;
import com.etsdk.app.huov7.getcash.ui.ChooseAccountActivity;
import com.liang530.views.refresh.mvc.IDataAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/9/26.
 */

public class ChooseAccountRcyAadapter extends RecyclerView.Adapter implements IDataAdapter<List<AccountListBean.CashListBean>> {
    final int TYPE_ACCOUNT = 0;
    final int TYPE_BTN = 1;
    List<AccountListBean.CashListBean> data = new ArrayList();
    ChooseAccountActivity chooseAccountActivity;
    private int selectAccountId = -1;

    public ChooseAccountRcyAadapter(ChooseAccountActivity chooseAccountActivity) {
        this.chooseAccountActivity = chooseAccountActivity;
    }

    public void setSelectAccountId(int selectAccountId) {
        this.selectAccountId = selectAccountId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ACCOUNT://账号
                return new AccountViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_tochoose, parent, false));
            case TYPE_BTN://按钮
                return new BtnViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_btn_oringe, parent, false));
            default:
        }
        return new AccountViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_togetcash, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_ACCOUNT://账号
                final AccountListBean.CashListBean accountBean = data.get(position);
                AccountViewHolder accountViewHolder = (AccountViewHolder) holder;
                accountViewHolder.tvAccountName.setText(accountBean.getCardname());//开户名
                String account = null;
                String payType = null;
                if ("银行卡".equals(accountBean.getPaytype())) {
                    payType = accountBean.getBankname();
                    String bankAccount = accountBean.getAccount();
                    StringBuffer sb = new StringBuffer();
                    sb.append(bankAccount.substring(0, 4))
                            .append(" **** **** ")
                            .append(bankAccount.substring(bankAccount.length() - 4, bankAccount.length()));
                    account = sb.toString();
                } else if ("支付宝".equals(accountBean.getPaytype())) {
                    account = accountBean.getAccount();
                    payType = "支付宝";
                } else if ("微信".equals(accountBean.getPaytype())) {
                    account = accountBean.getAccount();
                    payType = "微信";
                }
                accountViewHolder.tvAccout.setText(account);//账号、卡号
                accountViewHolder.tvAccountType.setText(payType);//类型
                accountViewHolder.tvAccountTag.setVisibility(accountBean.getIsdefault() == 1 ? View.VISIBLE : View.INVISIBLE);
                accountViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent data = new Intent();
                        data.putExtra("data", accountBean);
                        chooseAccountActivity.setResult(Activity.RESULT_OK, data);
                        chooseAccountActivity.finish();
                    }
                });
                if(accountBean.getId() == selectAccountId){
                    accountViewHolder.ivSelect.setBackgroundResource(R.drawable.shape_round_troke_red);
                }else{
                    accountViewHolder.ivSelect.setBackgroundResource(R.drawable.shape_round_troke);
                }
                break;
            case TYPE_BTN://按钮
                ((BtnViewHolder) holder).btnAddAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddAccountActivity.start(v.getContext());
                    }
                });
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == data.size()) {
            return TYPE_BTN;
        } else {
            return TYPE_ACCOUNT;
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    @Override
    public void notifyDataChanged(List<AccountListBean.CashListBean> data, boolean isRefresh) {
        if (isRefresh) {
            this.data.clear();
        }
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public List<AccountListBean.CashListBean> getData() {
        return this.data;
    }

    public void release() {
        chooseAccountActivity = null;
    }

    @Override
    public boolean isEmpty() {
        return data == null;
    }

    static class AccountViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_account_type)
        TextView tvAccountType;
        @BindView(R.id.tv_account_name)
        TextView tvAccountName;
        @BindView(R.id.tv_account_tag)
        TextView tvAccountTag;
        @BindView(R.id.tv_accout)
        TextView tvAccout;
        @BindView(R.id.iv_select)
        ImageView ivSelect;

        AccountViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class BtnViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btn_add_account)
        TextView btnAddAccount;

        BtnViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
