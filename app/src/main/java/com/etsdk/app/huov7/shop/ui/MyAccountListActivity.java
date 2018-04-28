package com.etsdk.app.huov7.shop.ui;

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
import com.etsdk.app.huov7.shop.adapter.MyAccountItemViewProvider;
import com.etsdk.app.huov7.shop.model.MyAccountBean;
import com.etsdk.app.huov7.shop.model.MyAccountListRequstBean;
import com.etsdk.app.huov7.ui.ServiceActivity;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.L;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 用户的游戏小号列表
 */
public class MyAccountListActivity extends ImmerseActivity implements AdvRefreshListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    BaseRefreshLayout baseRefreshLayout;
    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    private Items items = new Items();
    private MultiTypeAdapter multiTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account_list);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("选择小号");
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        // 设置适配器
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(MyAccountBean.DataBean.ListBean.class, new MyAccountItemViewProvider(this));
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.refresh();
        //测试数据
//        ArrayList<GameBean> list = new ArrayList<>();
//        list.add(new GameBean());
//        list.add(new GameBean());
//        list.add(new GameBean());
//        list.add(new GameBean());
//        list.add(new GameBean());
//        baseRefreshLayout.resultLoadData(items, list, 1);
    }

    @Override
    public void getPageData(final int requestPageNo) {
        final MyAccountListRequstBean requestBean =new MyAccountListRequstBean();
        requestBean.setGameid(getIntent().getStringExtra("gameid"));
        HttpParamsBuild httpParamsBuild=new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<MyAccountBean.DataBean>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(final MyAccountBean.DataBean data) {
            }

            @Override
            public void onDataSuccess(MyAccountBean.DataBean data, String code, String msg) {
                super.onDataSuccess(data, code, msg);
                if(data!=null&&data.getList().size()>0){
//                    int maxPage = (int)Math.ceil(data.getCount() / 10.);
                    baseRefreshLayout.resultLoadData(items,data.getList(),1);
                }else{
                    baseRefreshLayout.resultLoadData(items,new ArrayList(),requestPageNo-1);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                L.e(TAG, code+" "+msg);
                baseRefreshLayout.resultLoadData(items,null,null);
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.dealAccountGetAccounts), httpParamsBuild.getHttpParams(),httpCallbackDecode);
    }

    public static void start(Context context, String gameid) {
        Intent starter = new Intent(context, MyAccountListActivity.class);
        starter.putExtra("gameid", gameid);
        context.startActivity(starter);
    }

    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight, R.id.iv_service, R.id.tv_service})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.iv_service:
            case R.id.tv_service:
                ServiceActivity.start(mContext);
                break;
        }
    }
}
