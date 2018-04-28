package com.etsdk.app.huov7.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.GameListAdapter;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.provider.GameItemViewProvider;
import com.etsdk.app.huov7.ui.GameListActivity;
import com.etsdk.app.huov7.util.RecyclerViewNoAnimator;
import com.liang530.views.refresh.mvc.AdvRefreshListener;

import butterknife.BindView;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by liu hong liang on 2016/12/10.
 * 游戏列表fragment
 */

public class TestGameListFragment extends AutoLazyFragment implements AdvRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    GameListAdapter gameListAdapter;
    private boolean requestTopSplit =false;//是否需要顶部分割线
    private boolean showRank=false;
    private Items items=new Items();
    public static TestGameListFragment newInstance(boolean requestTopSplit, boolean showRank) {
        TestGameListFragment newFragment = new TestGameListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("requestTopSplit", requestTopSplit);
        bundle.putBoolean("showRank", showRank);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.test_fragment_game_list);
        setupUI();
    }

    private void setupUI() {
        Bundle arguments = getArguments();
        MultiTypeAdapter multiTypeAdapter =new MultiTypeAdapter(items);
        multiTypeAdapter.register(GameBean.class,new GameItemViewProvider());
        if(arguments.getBoolean("showRank")){
            for(int i=0;i<20;i++){
                items.add(new GameBean());
            }
        }else{
            for(int i=0;i<3;i++){
                items.add(new GameBean());
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new RecyclerViewNoAnimator());
        // 设置适配器
        recyclerView.setAdapter(multiTypeAdapter);
    }

    @Override
    public void getPageData(int i) {

    }

    public static void start(Context context) {
        Intent starter = new Intent(context, GameListActivity.class);
        context.startActivity(starter);
    }

    @OnClick({})
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }
}
