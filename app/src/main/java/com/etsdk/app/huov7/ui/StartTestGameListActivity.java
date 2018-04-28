package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.StartServerGameBean;
import com.etsdk.app.huov7.model.StartServerGameList;
import com.etsdk.app.huov7.model.TestGameBean;
import com.etsdk.app.huov7.model.TestGameList;
import com.etsdk.app.huov7.provider.StartServerGameItemViewProvider;
import com.etsdk.app.huov7.provider.TestServerGameItemViewProvider;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by liu hong liang on 2017/6/1.
 * 开服开测更多列表
 */
public class StartTestGameListActivity extends ImmerseActivity implements AdvRefreshListener {
    public static final int TYPE_START=1;
    public static final int TYPE_TEST=2;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    BaseRefreshLayout baseRefreshLayout;
    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    private Items items=new Items();
    private MultiTypeAdapter multiTypeAdapter;
    private int type;//开服还是开测列表
    private int dateType;//时间类型  1今日 2即将 3已测 20170601 v72 新增
    String[] titleList={"今天开测","即将开测","已开测"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        ButterKnife.bind(this);
        setupUI();
    }
    private void setupUI() {
        type = getIntent().getIntExtra("type",TYPE_START);
        dateType = getIntent().getIntExtra("dateType",1);
        tvTitleName.setText(titleList[dateType-1]);
        if(type==TYPE_START){
            tvTitleName.setText(titleList[dateType-1].replace("测","服"));
        }
        Intent intent = getIntent();
        if (intent != null) {
            String title =intent.getStringExtra("title");
            if (!TextUtils.isEmpty(title)) {
                tvTitleName.setText(title);
            }
        }
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        // 设置适配器
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(TestGameBean.class, new TestServerGameItemViewProvider());
        multiTypeAdapter.register(StartServerGameBean.class, new StartServerGameItemViewProvider());
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.refresh();
    }
    @Override
    public void getPageData( int requestPageNo) {
        if(type==TYPE_START){
            getPageDataForStart(requestPageNo);
        }else{
            getPageDataForTest(requestPageNo);
        }
    }
    private void getPageDataForTest(final int requestPageNo){
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.gameListApi);
        httpParams.put("page",requestPageNo);
        httpParams.put("offset",20);
        httpParams.put("test","2");
        httpParams.put("test_type",dateType);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.gameListApi),new HttpJsonCallBackDialog<TestGameList>(){
            @Override
            public void onDataSuccess(TestGameList data) {
                if(data!=null&&data.getData()!=null&&data.getData().getList()!=null){
                    int maxPage = (int)Math.ceil(data.getData().getCount() / 20.);
                    baseRefreshLayout.resultLoadData(items,data.getData().getList(),maxPage);
                }else{
                    baseRefreshLayout.resultLoadData(items,new ArrayList(),requestPageNo-1);
                }
            }
            @Override
            public void onJsonSuccess(int code, String msg,String data) {
                baseRefreshLayout.resultLoadData(items,null,null);
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                baseRefreshLayout.resultLoadData(items,null,null);
            }
        });
    }
    private void getPageDataForStart(final int requestPageNo){
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.gameListApi);
        httpParams.put("page",requestPageNo);
        httpParams.put("offset",20);
        httpParams.put("server","2");
        httpParams.put("server_type",dateType);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.gameListApi),new HttpJsonCallBackDialog<StartServerGameList>(){
            @Override
            public void onDataSuccess(StartServerGameList data) {
                if(data!=null&&data.getData()!=null&&data.getData().getList()!=null){
                    int maxPage = (int)Math.ceil(data.getData().getCount() / 20.);
                    baseRefreshLayout.resultLoadData(items,data.getData().getList(),maxPage);
                }else{
                    baseRefreshLayout.resultLoadData(items,new ArrayList(),requestPageNo-1);
                }
            }
            @Override
            public void onJsonSuccess(int code, String msg,String data) {
                baseRefreshLayout.resultLoadData(items,null,null);
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                baseRefreshLayout.resultLoadData(items,null,null);
            }
        });
    }



    public static void start(Context context,int type,int dateType) {
        Intent starter = new Intent(context, StartTestGameListActivity.class);
        starter.putExtra("type",type);
        starter.putExtra("dateType",dateType);
        context.startActivity(starter);
    }

    @OnClick({R.id.iv_titleLeft,R.id.tv_titleRight})
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

}
