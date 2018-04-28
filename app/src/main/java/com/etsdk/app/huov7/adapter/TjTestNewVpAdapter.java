package com.etsdk.app.huov7.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.etsdk.app.huov7.model.AdImage;
import com.etsdk.app.huov7.model.GameGiftItem;
import com.etsdk.app.huov7.model.SplitLine;
import com.etsdk.app.huov7.provider.AdImageViewProvider;
import com.etsdk.app.huov7.provider.GameGiftItemViewProvider;
import com.etsdk.app.huov7.provider.SplitLineViewProvider;

import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by liu hong liang on 2017/1/12.
 */

public class TjTestNewVpAdapter  extends PagerAdapter{
    private List<GameGiftItem> testGameList;
    private List<GameGiftItem> newServerList;

    public TjTestNewVpAdapter(List<GameGiftItem> testGameList, List<GameGiftItem> newServerList) {
        this.testGameList = testGameList;
        this.newServerList = newServerList;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RecyclerView recyclerView=new RecyclerView(container.getContext());
        ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        recyclerView.setLayoutParams(layoutParams);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        if(position==0){
            MultiTypeAdapter multiTypeAdapter=new MultiTypeAdapter(testGameList);
            multiTypeAdapter.register(GameGiftItem.class,new GameGiftItemViewProvider(false));
            multiTypeAdapter.register(SplitLine.class,new SplitLineViewProvider());
            multiTypeAdapter.register(AdImage.class,new AdImageViewProvider());
            recyclerView.setAdapter(multiTypeAdapter);
        }else{
            MultiTypeAdapter multiTypeAdapter=new MultiTypeAdapter(newServerList);
            multiTypeAdapter.register(GameGiftItem.class,new GameGiftItemViewProvider(true));
            multiTypeAdapter.register(SplitLine.class,new SplitLineViewProvider());
            multiTypeAdapter.register(AdImage.class,new AdImageViewProvider());
            recyclerView.setAdapter(multiTypeAdapter);
        }
        container.addView(recyclerView);
        return recyclerView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0){
            return "测试表";
        }else {
            return "新服表";
        }
    }
}
