package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.ChargeRecordRcyAadapter;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.ChargeRecordListBean;
import com.etsdk.app.huov7.model.ChargeRrcordListRequestBean;
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

/**
 * 2017/5/18.
 * 平台币、游戏币充值记录fragment
 */

public class ChargeRecordFragment extends AutoLazyFragment implements AdvRefreshListener {
    public static final int TYPE_PTB = 0;//平台币
    public static final int TYPE_GAME = 1;//游戏币

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;

    private int type = 0;
    BaseRefreshLayout baseRefreshLayout;
    ChargeRecordRcyAadapter adapter;

    public static ChargeRecordFragment newInstance(int type) {
        ChargeRecordFragment newFragment = new ChargeRecordFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_comm_list);
        setupUI();
    }

    private void setupUI() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            type = arguments.getInt("type", 0);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        recyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, BaseAppUtil.dip2px(getContext(),8), getResources().getColor(R.color.bg_comm)));

        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        // 设置适配器
        adapter = new ChargeRecordRcyAadapter(type);
        baseRefreshLayout.setAdapter(adapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.refresh();
    }


    @Override
    public void getPageData(final int requestPageNo) {
        final ChargeRrcordListRequestBean requestBean =new ChargeRrcordListRequestBean();
        requestBean.setPage(requestPageNo);
        requestBean.setOffset(10);
        HttpParamsBuild httpParamsBuild=new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<ChargeRecordListBean>(getActivity(), httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(final ChargeRecordListBean data) {
                if(data!=null&&data.getList()!=null){
                    baseRefreshLayout.resultLoadData(adapter.getData(),data.getList(),data.getCount(),10);
                }else{
                    baseRefreshLayout.resultLoadData(adapter.getData(),new ArrayList(),requestPageNo-1);
                }
            }
            @Override
            public void onFailure(String code, String msg) {
                L.e(TAG, code+" "+msg);
                baseRefreshLayout.resultLoadData(adapter.getData(),null,null);
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        String url = "";
        if(type == TYPE_PTB){
            url = AppApi.userPTBChargeRecord;
        }else if(type == TYPE_GAME){
            url = AppApi.userGameChargeRecord;
        }
        RxVolley.post(AppApi.getUrl(url), httpParamsBuild.getHttpParams(),httpCallbackDecode);
    }
}
