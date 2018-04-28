package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.DoTaskItem;
import com.etsdk.app.huov7.model.DoTaskResultBean;
import com.etsdk.app.huov7.model.DoTaskTop;
import com.etsdk.app.huov7.model.DoTaskType;
import com.etsdk.app.huov7.provider.DoTaskItemViewProvider;
import com.etsdk.app.huov7.provider.DoTaskTopViewProvider;
import com.etsdk.app.huov7.provider.DoTaskTypeViewProvider;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class DoScoreTaskActivity extends ImmerseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    @BindView(R.id.activity_do_score_task)
    LinearLayout activityDoScoreTask;
    private MultiTypeAdapter multiTypeAdapter;
    private Items items=new Items();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_score_task);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("做任务赚积分");
        initRefreshLayout();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        // 设置适配器
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(DoTaskTop.class, new DoTaskTopViewProvider());
        multiTypeAdapter.register(DoTaskType.class, new DoTaskTypeViewProvider());
        multiTypeAdapter.register(DoTaskItem.class, new DoTaskItemViewProvider());
        swrefresh.setOnRefreshListener(this);
        recyclerView.setAdapter(multiTypeAdapter);
        onRefresh();
    }
    private void initRefreshLayout(){
        swrefresh.setColorSchemeResources(R.color.bg_blue,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    private void updateData(DoTaskResultBean data){
        items.clear();
        items.add(new DoTaskTop(data.getMyintegral()));
        TreeMap<String,List<DoTaskItem>> map=new TreeMap<>();
        //对数据分类
        if(data.getList()!=null){
            for(DoTaskItem doTaskItem:data.getList()){
                List<DoTaskItem> doTaskItemList = map.get(doTaskItem.getTypeid());
                if(doTaskItemList==null){
                    doTaskItemList=new ArrayList<>();
                    map.put(doTaskItem.getTypeid(),doTaskItemList);
                }
                doTaskItem.setMyMoney(data.getMymoney());//设置money
                doTaskItemList.add(doTaskItem);
            }
        }
        Set<Map.Entry<String, List<DoTaskItem>>> entries = map.entrySet();
        for (Map.Entry<String, List<DoTaskItem>> entry : entries) {
            items.add(new DoTaskType(entry.getKey()));
            Collections.sort(entry.getValue(),new DoTaskItemComparator());
            items.addAll(entry.getValue());
        }
        multiTypeAdapter.notifyDataSetChanged();
        swrefresh.setRefreshing(false);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, DoScoreTaskActivity.class);
        context.startActivity(starter);
    }

    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
        }
    }
    @Override
    public void onRefresh() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<DoTaskResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(DoTaskResultBean data) {
                if(data!=null){
                    updateData(data);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userActlistApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }
    class DoTaskItemComparator implements Comparator<DoTaskItem> {
        @Override
        public int compare(DoTaskItem o1, DoTaskItem o2) {
            return o1.getFinishflag().compareTo(o2.getFinishflag());
        }
    }

}
