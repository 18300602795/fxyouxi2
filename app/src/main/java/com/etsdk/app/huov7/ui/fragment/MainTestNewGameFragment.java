package com.etsdk.app.huov7.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.TestNewGameAdapter;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.ui.GameListActivity;
import com.etsdk.app.huov7.util.RecyclerViewNoAnimator;
import com.liang530.views.refresh.mvc.AdvRefreshListener;
import com.liang530.views.refresh.mvc.BaseRefreshLayout;
import com.liang530.views.refresh.mvc.UltraRefreshLayout;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Created by liu hong liang on 2016/12/12.
 * 首页-游戏-开服开测界面
 *
 */

public class MainTestNewGameFragment extends AutoLazyFragment implements AdvRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptrRefresh)
    PtrClassicFrameLayout ptrRefresh;
    TestNewGameAdapter testNewGameAdapter;
    BaseRefreshLayout baseRefreshLayout;
    private int position;
    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_main_test_new_game);
        setupUI();
    }
    private void setupUI() {
        baseRefreshLayout = new UltraRefreshLayout(ptrRefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new RecyclerViewNoAnimator());
        testNewGameAdapter = new TestNewGameAdapter();
        // 设置适配器
        baseRefreshLayout.setAdapter(testNewGameAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        Bundle arguments = getArguments();
        if(arguments!=null){
            position = arguments.getInt("position", 0);
        }
        switchFragment(position);
    }

    @Override
    public void getPageData(int pageNo) {

    }

    public static void start(Context context) {
        Intent starter = new Intent(context, GameListActivity.class);
        context.startActivity(starter);
    }
    public void switchFragment(int position){
        this.position=position;
        if(testNewGameAdapter!=null){
            testNewGameAdapter.switchStartTab(position==0);
        }
    }
}
