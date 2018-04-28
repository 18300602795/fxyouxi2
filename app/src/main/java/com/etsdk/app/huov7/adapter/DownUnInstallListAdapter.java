package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.down.TasksManagerModel;
import com.liang530.utils.BaseAppUtil;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/12/6.
 */

public class DownUnInstallListAdapter extends RecyclerView.Adapter {

    private List<TasksManagerModel> downUnInstallModelList;

    public DownUnInstallListAdapter(List<TasksManagerModel> downUnInstallModelList) {
        this.downUnInstallModelList = downUnInstallModelList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_down_un_install, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder= (ViewHolder) holder;
        final TasksManagerModel tasksManagerModel = downUnInstallModelList.get(position);
        viewHolder.btnInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseAppUtil.installApk(v.getContext(),new File(tasksManagerModel.getPath()));
            }
        });
        viewHolder.tvGameName.setText(tasksManagerModel.getGameName());
//        GlideDisplay.display(viewHolder.ivGameImg,tasksManagerModel.getGameIcon(),R.mipmap.ic_launcher);
        Glide.with(viewHolder.context).load(tasksManagerModel.getGameIcon()).placeholder(R.mipmap.ic_launcher).into(viewHolder.ivGameImg);
    }

    @Override
    public int getItemCount() {
        return downUnInstallModelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_game_img)
        RoundedImageView ivGameImg;
        @BindView(R.id.tv_game_name)
        TextView tvGameName;
        @BindView(R.id.btn_install)
        Button btnInstall;
        Context context;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
            context = view.getContext();
        }
    }
}
