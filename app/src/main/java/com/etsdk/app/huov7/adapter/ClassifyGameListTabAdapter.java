package com.etsdk.app.huov7.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.GameClassifyListModel;

import java.util.List;

/**
 * Created by liu hong liang on 2017/5/31.
 */

public class ClassifyGameListTabAdapter extends RecyclerView.Adapter {
    private int selectPosition=0;
    private List<GameClassifyListModel.GameClassify> datas;
    private OnItemSelectedListener onItemClickListener;
    public ClassifyGameListTabAdapter(List<GameClassifyListModel.GameClassify> datas,OnItemSelectedListener onItemClickListener) {
        this.datas = datas;
        this.onItemClickListener=onItemClickListener;
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classify_tab, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder= (ViewHolder) holder;
        viewHolder.tvName.setText(datas.get(position).getTypename());
        viewHolder.tvName.setEnabled(selectPosition!=position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPosition=position;
                notifyDataSetChanged();
                onItemClickListener.selected(selectPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas==null?0:datas.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName= (TextView) itemView;
        }
    }
    public interface OnItemSelectedListener{
        void selected(int position);
    }

}
