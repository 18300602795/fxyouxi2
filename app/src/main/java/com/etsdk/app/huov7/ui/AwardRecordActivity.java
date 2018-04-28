package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.AwardRecordHead;
import com.etsdk.app.huov7.model.AwardRecordItem;
import com.etsdk.app.huov7.model.AwardRecordTime;
import com.etsdk.app.huov7.model.InviteFriendList;
import com.etsdk.app.huov7.provider.AwardRecordHeadViewProvider;
import com.etsdk.app.huov7.provider.AwardRecordItemViewProvider;
import com.etsdk.app.huov7.provider.AwardRecordTimeViewProvider;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class AwardRecordActivity extends ImmerseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    private Items items=new Items();
    private MultiTypeAdapter multiTypeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award_record);
        ButterKnife.bind(this);
        setupUI();
    }


    private void setupUI() {
        tvTitleName.setText("奖励记录");
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(AwardRecordHead.class,new AwardRecordHeadViewProvider());
        multiTypeAdapter.register(AwardRecordTime.class,new AwardRecordTimeViewProvider());
        multiTypeAdapter.register(AwardRecordItem.class,new AwardRecordItemViewProvider());
        recyclerview.setAdapter(multiTypeAdapter);
        swrefresh.setOnRefreshListener(this);
        getInviteFriend();
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
    private void updateUI(InviteFriendList data){
        swrefresh.setRefreshing(false);
        items.clear();
        items.add(new AwardRecordHead(data.getCount()));
        if(data.getList()!=null){
            for(AwardRecordItem awardRecordItem:data.getList()){
                Object temp = items.get(items.size() - 1);
                if(temp instanceof  AwardRecordItem){//上一个是item类型，判断时间
                    if(formatDate(awardRecordItem.getGettime()).equals(formatDate(((AwardRecordItem) temp).getGettime()))){
                        items.add(awardRecordItem);
                    }else{
                        items.add(new AwardRecordTime(formatDate(awardRecordItem.getGettime())));
                        items.add(awardRecordItem);
                    }
                }else{//上一个不是item类型
                    items.add(new AwardRecordTime(formatDate(awardRecordItem.getGettime())));
                    items.add(awardRecordItem);
                }
            }
        }
        multiTypeAdapter.notifyDataSetChanged();
    }
    private String formatDate(String dateTime){
        Date date=new Date(Long.parseLong(dateTime)*1000);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        StringBuffer buffer=new StringBuffer();
        if(calendar.get(Calendar.YEAR)!=Calendar.getInstance().get(Calendar.YEAR)){//不是今年
            buffer.append(Calendar.YEAR).append("年");
        }
        buffer.append((calendar.get(Calendar.MONTH)+1)).append("月");
        buffer.append(calendar.get(Calendar.DAY_OF_MONTH)).append("日");
        return buffer.toString();
    }
    private void getInviteFriend() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<InviteFriendList>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(InviteFriendList data) {
                if(data!=null){
                    updateUI(data);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userGetinvlistApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, AwardRecordActivity.class);
        context.startActivity(starter);
    }
    @Override
    public void onRefresh() {
        getInviteFriend();
    }
}
