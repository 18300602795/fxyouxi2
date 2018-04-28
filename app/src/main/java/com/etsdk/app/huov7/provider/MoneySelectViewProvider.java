package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.MoneySelect;
import com.etsdk.app.huov7.ui.GamePayActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/12.
 */
public class MoneySelectViewProvider
        extends ItemViewProvider<MoneySelect, MoneySelectViewProvider.ViewHolder> {
//    private View.OnClickListener listener;
    private RecyclerView recyclerview;
    private GamePayActivity gamePayActivity;

    public MoneySelectViewProvider(RecyclerView recyclerview,GamePayActivity gamePayActivity) {
        this.recyclerview = recyclerview;
        this.gamePayActivity=gamePayActivity;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_money_select, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final MoneySelect moneySelect) {
        holder.tvMoneyText.setText(moneySelect.getContent());
        int money = (int) recyclerview.getTag();
        if(moneySelect.getMoney()==money){
            holder.tvMoneyText.setBackgroundResource(R.mipmap.bg_money_select);
            holder.tvMoneyText.setTextColor(holder.itemView.getResources().getColor(R.color.bg_blue));
        }else{
            holder.tvMoneyText.setBackgroundResource(R.mipmap.bg_money_text_pre);
            holder.tvMoneyText.setTextColor(holder.itemView.getResources().getColor(R.color.text_black));
        }
        holder.tvMoneyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerview.setTag(moneySelect.getMoney());
                recyclerview.getAdapter().notifyDataSetChanged();
                gamePayActivity.setSelectTemplateMoney();//联网更新界面数据
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_money_text)
        TextView tvMoneyText;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}