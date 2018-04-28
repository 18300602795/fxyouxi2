package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.TjTestNewVpAdapter;
import com.etsdk.app.huov7.model.SwitchFragmentEvent;
import com.etsdk.app.huov7.model.TjTestNewVp;
import com.etsdk.app.huov7.ui.MainActivity;
import com.flyco.tablayout.SlidingTabLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/12.
 */
public class TjTestNewVpViewProvider
        extends ItemViewProvider<TjTestNewVp, TjTestNewVpViewProvider.ViewHolder> {

    private TjTestNewVpAdapter tjTestNewVpAdapter;
    private int testNewItemCount = 3;

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_tj_test_new_vp, parent, false);
        View tempView = inflater.inflate(R.layout.adapter_game_gift_list_item, parent, false);
        View tempViewImage = inflater.inflate(R.layout.item_ad_image, parent, false);
        tempView.measure(0, 0);
        tempViewImage.measure(0,0);
        int measuredHeight = tempView.getMeasuredHeight();
//        L.e("hongliang","计算的高度measuredHeight="+measuredHeight);
        ViewHolder viewHolder = new ViewHolder(root);
        int height = testNewItemCount * measuredHeight+tempViewImage.getMeasuredHeight();
        ViewGroup.LayoutParams layoutParams = viewHolder.vpTestNew.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        } else {
            layoutParams.height = height;
        }
        viewHolder.vpTestNew.setLayoutParams(layoutParams);
        return viewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull TjTestNewVp tjTestNewVp) {
        tjTestNewVpAdapter = new TjTestNewVpAdapter(tjTestNewVp.getTestGameList(), tjTestNewVp.getNewServerList());
        holder.vpTestNew.setAdapter(tjTestNewVpAdapter);
        holder.tabTestNew.setViewPager(holder.vpTestNew);
        holder.tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先跳转到游戏fragment，在跳转到开服开测fragment,在控制adapter切换到开服或者开测
                int currentTab = holder.tabTestNew.getCurrentTab();
                EventBus.getDefault().postSticky(new SwitchFragmentEvent(MainActivity.class.getName(),1,3,currentTab));
            }
        });
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tab_test_new)
        SlidingTabLayout tabTestNew;
        @BindView(R.id.vp_test_new)
        ViewPager vpTestNew;
        @BindView(R.id.tv_more)
        TextView tvMore;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}