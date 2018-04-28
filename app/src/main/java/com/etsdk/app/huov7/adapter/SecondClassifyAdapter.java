package com.etsdk.app.huov7.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.GameClassifyListModel;
import com.etsdk.app.huov7.ui.ClassifyGameListActivity;
import com.game.sdk.util.GsonUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/12/9.
 */

public class SecondClassifyAdapter extends RecyclerView.Adapter {
    private List<GameClassifyListModel.GameClassify> datas;
    private String firstClassifyName;

    public SecondClassifyAdapter(List<GameClassifyListModel.GameClassify> datas, String firstClassifyName) {
        this.datas = datas;
        this.firstClassifyName = firstClassifyName;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_second_classify, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //把分类数据带过去
                ClassifyGameListActivity.start(v.getContext(),firstClassifyName, GsonUtil.getGson().toJson(datas),position);
            }
        });
        ViewHolder viewHolder= (ViewHolder) holder;
        if(datas.get(position)==null){//额外补的
            viewHolder.tvSecondClassifyName.setText("");
        }else {
            viewHolder.tvSecondClassifyName.setText(datas.get(position).getTypename());
        }
    }

    @Override
    public int getItemCount() {

        return datas!=null?datas.size():0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_secondClassifyName)
        TextView tvSecondClassifyName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
