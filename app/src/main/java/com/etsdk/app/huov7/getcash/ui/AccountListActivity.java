package com.etsdk.app.huov7.getcash.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.getcash.GetCaseApi;
import com.etsdk.app.huov7.getcash.adapter.AccountListRcyAadapter;
import com.etsdk.app.huov7.getcash.model.AccountListBean;
import com.etsdk.app.huov7.getcash.model.AccountListRequestBean;
import com.etsdk.app.huov7.getcash.model.DeleteAccountRequestBean;
import com.etsdk.app.huov7.getcash.model.ResultBean;
import com.etsdk.app.huov7.getcash.model.UpdateAccountRequestBean;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.control.LoginControl;
import com.liang530.log.L;
import com.liang530.log.T;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 提现账户列表
 */
public class AccountListActivity extends ImmerseActivity implements AdvRefreshListener {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;

    BaseRefreshLayout baseRefreshLayout;
    AccountListRcyAadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("提现账户");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        // 设置适配器
        adapter = new AccountListRcyAadapter(this);
        baseRefreshLayout.setAdapter(adapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.setCanloadMore(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        baseRefreshLayout.refresh();
    }

    @Override
    public void getPageData(final int i) {
        AccountListRequestBean requestBean =new AccountListRequestBean();
        requestBean.setUsername(LoginControl.getAccount());
        requestBean.setPage(i);
        requestBean.setOffset(20);
//        requestBean.setIsdefault(1);
        HttpParamsBuild httpParamsBuild=new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<AccountListBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(final AccountListBean data) {
                if(data!=null&&data.getCash_list()!=null){
                    baseRefreshLayout.resultLoadData(adapter.getData(),data.getCash_list(),20);
                }else{
                    baseRefreshLayout.resultLoadData(adapter.getData(),new ArrayList(),i-1);
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
        RxVolley.post(AppApi.getUrl(GetCaseApi.URL_ACOUNT_LIST), httpParamsBuild.getHttpParams(),httpCallbackDecode);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, AccountListActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baseRefreshLayout.setAdvRefreshListener(null);
        adapter.release();
    }

    @OnClick({R.id.iv_titleLeft})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
        }
    }

    /**
     * 是否设为默认
     */
    public void updateAccount(int id, boolean isDefault){
        UpdateAccountRequestBean requestBean =new UpdateAccountRequestBean();
        requestBean.setInfoid(id);
        requestBean.setIsdefault(isDefault?1:0);
        HttpParamsBuild httpParamsBuild=new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<ResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(final ResultBean data) {
            }

            @Override
            public void onDataSuccess(ResultBean data, String code, String msg) {
                super.onDataSuccess(data, code, msg);
                if("200".equals(code)){
                    baseRefreshLayout.refresh();
                }else{
                    T.s(mContext, "设置失败 "+code);
                    L.e(TAG, "设置失败 "+code+" "+msg);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                T.s(mContext, "设置失败 "+code);
                L.e(TAG, "设置失败 "+code+" "+msg);
            }
        };
        httpCallbackDecode.setShowTs(false);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(GetCaseApi.URL_ACOUNT_UPDATE), httpParamsBuild.getHttpParams(),httpCallbackDecode);
        baseRefreshLayout.refresh();
    }

    /**
     * 删除账号
     * @param id
     */
    public void deleteAccount(final int id){
        new AlertDialog.Builder(mContext).setTitle("注意").setMessage("是否删除该账户信息？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRequest(id);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

    private void deleteRequest(int id){
        DeleteAccountRequestBean requestBean =new DeleteAccountRequestBean();
        requestBean.setInfoid(id);
        HttpParamsBuild httpParamsBuild=new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<ResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(final ResultBean data) {
            }

            @Override
            public void onDataSuccess(ResultBean data, String code, String msg) {
                super.onDataSuccess(data, code, msg);
                if("200".equals(code)){
                    T.s(mContext, "删除成功");
                    baseRefreshLayout.refresh();
                }else{
                    T.s(mContext, "删除失败 "+code);
                    L.e(TAG, "删除失败 "+code+" "+msg);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                T.s(mContext, "删除失败 "+code);
                L.e(TAG, code+" "+msg);
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(GetCaseApi.URL_ACOUNT_DELETE), httpParamsBuild.getHttpParams(),httpCallbackDecode);
    }
}
