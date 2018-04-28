package com.etsdk.app.huov7.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.model.GiftListItem;
import com.etsdk.app.huov7.provider.GiftListItemViewProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class TestRecyclerViewActivity extends ImmerseActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private Items items=new Items();
    private MultiTypeAdapter multiTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_game_detail);
        ButterKnife.bind(this);
        setupUI();

    }

    private void setupUI() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(GiftListItem.class,new GiftListItemViewProvider(multiTypeAdapter));
        for(int i=0;i<20;i++){
            items.add(new GiftListItem());
        }
        recyclerview.setAdapter(multiTypeAdapter);
    }
}
