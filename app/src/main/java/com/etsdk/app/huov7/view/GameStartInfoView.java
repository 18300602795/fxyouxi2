package com.etsdk.app.huov7.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.StartServerInfo;
import com.etsdk.app.huov7.model.StartServerInfo1;
import com.etsdk.app.huov7.provider.StartServerInfo1ViewProvider;
import com.etsdk.app.huov7.model.StartServerInfo2;
import com.etsdk.app.huov7.provider.StartServerInfo2ViewProvider;
import com.etsdk.app.huov7.provider.StartServerInfoViewProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by liu hong liang on 2016/12/7.
 * 游戏详情页的开服信息view
 *
 */

public class GameStartInfoView extends FrameLayout implements View.OnClickListener {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private Items items = new Items();
    private MultiTypeAdapter multiTypeAdapter;
    private boolean isHidderMore = true;
    List<StartServerInfo> datas;

    public GameStartInfoView(Context context) {
        super(context);
        initUI();
    }

    public GameStartInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public GameStartInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    private void initUI() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_game_start_info, this);
        ButterKnife.bind(this);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setNestedScrollingEnabled(false);
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(StartServerInfo1.class, new StartServerInfo1ViewProvider(this));
        multiTypeAdapter.register(StartServerInfo2.class, new StartServerInfo2ViewProvider(this));
        multiTypeAdapter.register(StartServerInfo.class, new StartServerInfoViewProvider(this));
        recyclerview.setAdapter(multiTypeAdapter);
    }

    public void setDatas(List<StartServerInfo> datas) {
        this.datas = datas;
        updateUI(true);
    }

    private void updateUI(boolean isHidderMore) {
        int arrowShowType=isHidderMore?1:2;
        if(datas.size()<=1){
            arrowShowType=0;
        }
        items.clear();
        for (int i = 0; i < datas.size(); i++) {
            if (i == 0) {
                items.add(new StartServerInfo1(datas.get(i).getSername(), datas.get(i).getStarttime(),datas.get(i).getStatus(),arrowShowType));
            } else if (i == 1) {
                items.add(new StartServerInfo2(datas.get(i).getSername(), datas.get(i).getStarttime(),datas.get(i).getStatus()));
                if (isHidderMore) {
                    break;
                }
            } else {
                items.add(datas.get(i));
            }
        }
        multiTypeAdapter.notifyDataSetChanged();
        this.isHidderMore=isHidderMore;
    }

    @Override
    public void onClick(View v) {
        updateUI(!isHidderMore);
    }
}
