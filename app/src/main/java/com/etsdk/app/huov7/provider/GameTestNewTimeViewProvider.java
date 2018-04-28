package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.GameTestNewTime;
import com.etsdk.app.huov7.ui.StartTestGameListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/18.
 */
public class GameTestNewTimeViewProvider
        extends ItemViewProvider<GameTestNewTime, GameTestNewTimeViewProvider.ViewHolder> {
    private boolean startServer;

    public GameTestNewTimeViewProvider(boolean startServer) {
        this.startServer = startServer;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_game_test_new_time, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final GameTestNewTime gameTestNewTime) {
        String dateStr = gameTestNewTime.getDateStr();
        Log.e("hongliang","dateStr="+dateStr);
        if("1".equals(dateStr)){
            dateStr="今天开测";
        }else if("2".equals(dateStr)){
            dateStr="即将开测";
        }else{
            dateStr="已开测";
        }
        if(startServer){
            dateStr=dateStr.replace("测","服");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //去往更多开测界面
                StartTestGameListActivity.start(v.getContext(),
                        startServer?StartTestGameListActivity.TYPE_START:StartTestGameListActivity.TYPE_TEST,
                        Integer.parseInt(gameTestNewTime.getDateStr())
                );
            }
        });
        holder.tvStartTime.setText(dateStr);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_start_time)
        TextView tvStartTime;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}