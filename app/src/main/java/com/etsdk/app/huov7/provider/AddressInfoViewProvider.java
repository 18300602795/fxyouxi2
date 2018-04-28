package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.AddressInfo;
import com.etsdk.app.huov7.ui.dialog.SelectAddressDialog;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/2/7.
 */
public class AddressInfoViewProvider
        extends ItemViewProvider<AddressInfo, AddressInfoViewProvider.ViewHolder> {
    private final Map<Integer, AddressInfo> levelIdMap;
    SelectAddressDialog.SelectAddressInfoListener listener;

    public AddressInfoViewProvider(SelectAddressDialog.SelectAddressInfoListener listener, Map<Integer, AddressInfo> levelIdMap) {
        this.listener=listener;
        this.levelIdMap=levelIdMap;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_address_info, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final AddressInfo addressInfo) {
        holder.tvAddressName.setText(addressInfo.getName());
        AddressInfo selectAddress = levelIdMap.get(addressInfo.getLevel());
        if(selectAddress!=null&&selectAddress.getId().equals(addressInfo.getId())){
            holder.ivSelected.setVisibility(View.VISIBLE);
        }else{
            holder.ivSelected.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.selectAddressInfo(addressInfo);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_addressName)
        TextView tvAddressName;
        @BindView(R.id.iv_selected)
        ImageView ivSelected;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}