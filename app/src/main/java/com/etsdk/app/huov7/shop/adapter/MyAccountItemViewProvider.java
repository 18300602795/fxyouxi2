package com.etsdk.app.huov7.shop.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.shop.model.MyAccountBean;
import com.etsdk.app.huov7.shop.model.SelectLittleAccountEvent;
import com.etsdk.app.huov7.shop.ui.MyAccountListActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2016/12/21.
 */
public class MyAccountItemViewProvider
        extends ItemViewProvider<MyAccountBean.DataBean.ListBean, MyAccountItemViewProvider.ViewHolder> {
    MyAccountListActivity myAccountListActivity;

    public MyAccountItemViewProvider(MyAccountListActivity myAccountListActivity) {
        this.myAccountListActivity = myAccountListActivity;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_my_account_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final MyAccountBean.DataBean.ListBean data) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myAccountListActivity != null) {
                    myAccountListActivity.finish();
                    myAccountListActivity = null;
                }
                EventBus.getDefault().post(new SelectLittleAccountEvent(data.getMg_mem_id() + "", data.getNickname()));
            }
        });
        holder.tvName.setText(data.getNickname());
        holder.tvMoney.setText("总充值todo");
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_money)
        TextView tvMoney;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}