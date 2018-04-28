package com.etsdk.app.huov7.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.GameListAdapter;
import com.etsdk.app.huov7.ui.GameDetailActivity;
import com.etsdk.app.huov7.ui.GameListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/12/6.
 */

public class RecommandAdapterHelp {



    public static void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        final RecommandBaseViewHolder recommandBaseViewHolder = (RecommandBaseViewHolder) holder;
        if (holder instanceof NewGameSFViewHolder) {
            recommandBaseViewHolder.tjRcyGame.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL, false));
            recommandBaseViewHolder.tjRcyGame.setNestedScrollingEnabled(false);
            recommandBaseViewHolder.tjRcyGame.setAdapter(new GameListAdapter());
            recommandBaseViewHolder.tvTypeName.setText("新游首发");
        }else if(holder instanceof ShouYouFengViewHolder){
            recommandBaseViewHolder.tjRcyGame.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL, false));
            recommandBaseViewHolder.tjRcyGame.setNestedScrollingEnabled(false);
            recommandBaseViewHolder.tjRcyGame.setAdapter(new GameListAdapter());
            recommandBaseViewHolder.tvTypeName.setText("手游风向标");
            recommandBaseViewHolder.ivHint.setVisibility(View.GONE);
        }else if(holder instanceof XinYouTJViewHolder){
            recommandBaseViewHolder.tjRcyGame.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL, false));
            recommandBaseViewHolder.tjRcyGame.setNestedScrollingEnabled(false);
            recommandBaseViewHolder.tjRcyGame.setAdapter(new GameListAdapter());
            recommandBaseViewHolder.tvTypeName.setText("新游推荐");
            recommandBaseViewHolder.ivHint.setVisibility(View.GONE);
        }
        recommandBaseViewHolder.tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameListActivity.start(v.getContext(), recommandBaseViewHolder.tvTypeName.getText().toString(),true, true, 2,0,0,0,0,0,0,null);
            }
        });
        recommandBaseViewHolder.ivBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameDetailActivity.start(v.getContext());
            }
        });
    }

    public static class NewGameSFViewHolder extends RecommandBaseViewHolder{
        public NewGameSFViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    public static class ShouYouFengViewHolder extends RecommandBaseViewHolder{
        public ShouYouFengViewHolder(View view) {
            super(view);
        }
    }
    public static class XinYouTJViewHolder extends RecommandBaseViewHolder{
        public XinYouTJViewHolder(View view) {
            super(view);
        }
    }

    /**
     * 基础的共用的首要推荐的BaseViewHolder
     */
    public static class RecommandBaseViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_hint)
        ImageView ivHint;
        @BindView(R.id.tv_typeName)
        TextView tvTypeName;
        @BindView(R.id.tv_more)
        TextView tvMore;
        @BindView(R.id.tj_rcy_Game)
        RecyclerView tjRcyGame;
        @BindView(R.id.iv_broadcast)
        ImageView ivBroadcast;
        RecommandBaseViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
