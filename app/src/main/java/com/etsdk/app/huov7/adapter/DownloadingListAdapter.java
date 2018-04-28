package com.etsdk.app.huov7.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.etsdk.app.huov7.down.TasksManagerModel;
import com.etsdk.app.huov7.view.NewDownloadingGameView;

import java.util.List;

/**
 * Created by liu hong liang on 2016/12/6.
 */

public class DownloadingListAdapter extends RecyclerView.Adapter{
    private List<TasksManagerModel> downloadingModelList;

    public DownloadingListAdapter(List<TasksManagerModel> downloadingModelList) {
        this.downloadingModelList = downloadingModelList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(new NewDownloadingGameView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder= (ViewHolder) holder;
        viewHolder.downloadingGameItem.setTasksManagerModel(downloadingModelList.get(position));
//        viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new DownloadingDeleteDialog().showDialog(v.getContext(), new UpdateVersionDialog.ConfirmDialogListener() {
//                    @Override
//                    public void ok() {
//
//                    }
//
//                    @Override
//                    public void cancel() {
//
//                    }
//                });
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return downloadingModelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        NewDownloadingGameView downloadingGameItem;
        ViewHolder(NewDownloadingGameView view) {
            super(view);
            this.downloadingGameItem=view;
        }
    }
}
