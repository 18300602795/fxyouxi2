package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.GameTestNewTab;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/18.
 */
public class GameTestNewTabViewProvider
        extends ItemViewProvider<GameTestNewTab, GameTestNewTabViewProvider.ViewHolder> {
    private OnTestNewTabSelectListener listener;

    public GameTestNewTabViewProvider(OnTestNewTabSelectListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_game_test_new_tab, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final GameTestNewTab gameTestNewTab) {
        holder.llStartTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelectTab(true);
                gameTestNewTab.setStartServer(true);
            }
        });
        holder.llTestTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelectTab(false);
                gameTestNewTab.setStartServer(false);
            }
        });
        holder.llStartTab.setSelected(gameTestNewTab.isStartServer());
        holder.llTestTab.setSelected(!gameTestNewTab.isStartServer());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_start_tab)
        LinearLayout llStartTab;
        @BindView(R.id.ll_test_tab)
        LinearLayout llTestTab;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public static interface OnTestNewTabSelectListener{
        void onSelectTab(boolean isStartServer);
    }
}