package com.etsdk.app.huov7.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AileApplication;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/12/8.
 */

public class ReportListAdapter extends RecyclerView.Adapter {
    private String datas[];
    private int selectPosition=0;
    public ReportListAdapter() {
        datas= AileApplication.getInstance().getResources().getStringArray(R.array.game_feedback);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_report_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder= (ViewHolder) holder;
        viewHolder.cbFeedbackType.setText(datas[position]);
        viewHolder.cbFeedbackType.setChecked(selectPosition==position);
        viewHolder.cbFeedbackType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPosition=viewHolder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });
    }
    public String getSelectTypeContent(){
        return datas[selectPosition];
    }
    @Override
    public int getItemCount() {
        return datas.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.cb_feedback_type)
        CheckBox cbFeedbackType;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
