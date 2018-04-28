package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.SearchEvent;
import com.etsdk.app.huov7.util.HistoryDBHelper;
import com.etsdk.app.huov7.util.SearchHistoryUtil;
import com.liang530.log.SP;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/12/9.
 */

public class SearchHistoryAdapter extends RecyclerView.Adapter {
    final int TITLE = 0;
    final int LIST = 1;
    List<String> datas = new ArrayList<>();
    private final SQLiteDatabase db;

    public SearchHistoryAdapter(Context context) {
        db = new HistoryDBHelper(context, null, 1).getWritableDatabase();
        datas = SearchHistoryUtil.getSearchHistory(db);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == LIST) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_search_history, parent, false);
            return new ViewHolder(view);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_search_history_title, parent, false);
            return new TitleViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(getItemViewType(position)==TITLE){
            if(getItemCount() == 1){
                holder.itemView.setVisibility(View.GONE);//没内容时不显示
                return;
            }
            holder.itemView.setVisibility(View.VISIBLE);
            TitleViewHolder viewHolder = (TitleViewHolder) holder;
            ((TitleViewHolder) holder).tvClearHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteAll();
                }
            });
        }else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 发送搜索事件
                    EventBus.getDefault().post(new SearchEvent(datas.get(position-1)));
                }
            });
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.tvHistoryItem.setText(datas.get(position-1));
            viewHolder.ivClearItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(datas.get(position-1));
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TITLE;
        }else{
            return LIST;
        }
    }

    public void closeDb(){
        if(db!=null&&db.isOpen()){
            db.close();
        }
    }

    public void add(String name){
        SearchHistoryUtil.addSearchHistory(db, name);
        refresh();
    }

    public  void delete(String name){
        SearchHistoryUtil.deleteSearchHistory(db, name);
        refresh();
    }

    public  void deleteAll(){
        SearchHistoryUtil.deleteAllSearchHistory(db);
        refresh();
    }

    public void refresh(){
        datas.clear();
        datas = SearchHistoryUtil.getSearchHistory(db);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datas.size()+1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_history_item)
        TextView tvHistoryItem;
        @BindView(R.id.iv_clear_item)
        ImageView ivClearItem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_clear_history)
        TextView tvClearHistory;

        TitleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
