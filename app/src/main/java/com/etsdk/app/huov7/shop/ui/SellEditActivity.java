package com.etsdk.app.huov7.shop.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.shop.model.ShopAccountDetailBean;
import com.etsdk.app.huov7.view.LoadStatusView;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 编辑卖号信息
 */
public class SellEditActivity extends ImmerseActivity {

    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.fl)
    FrameLayout fl;
    @BindView(R.id.loadview)
    LoadStatusView loadview;

    private int sellId;
    private String title;
    private String gameId;
    private String gameName;
    private String memId;
    private String accountNickName;
    private String serverName;
    private float price;
    private String desc;
    private ArrayList<String> imgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_edit);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("修改");
        sellId = getIntent().getIntExtra("sellId", 0);
        getDetailInfo();
        loadview.setOnLoadRefreshListener(new LoadStatusView.OnLoadRefreshListener() {
            @Override
            public void onLoadRefresh() {
                getDetailInfo();
            }
        });
    }

    public void getDetailInfo() {
        loadview.showLoading();
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.dealAccountRead);
        httpParams.put("id", getIntent().getIntExtra("sellId", 0));
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.dealAccountRead), new HttpJsonCallBackDialog<ShopAccountDetailBean>() {
            @Override
            public void onDataSuccess(ShopAccountDetailBean data) {
                if("200".equals(data.getCode())) {
                    loadview.showSuccess();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl, SellFragment.newInstance(
                            SellFragment.MODE_EDIT, data.getData().getMg_mem_id(), data.getData().getId(), data.getData().getGameid()+"",
                            data.getData().getGamename(), data.getData().getMem_id()+"", data.getData().getNickname(),
                            data.getData().getServername(), Float.parseFloat(data.getData().getTotal_price()), data.getData().getTitle(),
                            data.getData().getDescription(), (ArrayList<String>) data.getData().getImage())).commit();
                }else{
                    loadview.showFail();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                loadview.showFail();
            }
        });
    }


    public static void start(Context context, int sellId) {
        Intent starter = new Intent(context, SellEditActivity.class);
        starter.putExtra("sellId", sellId);
        context.startActivity(starter);
    }

    @OnClick(R.id.iv_titleLeft)
    public void onClick() {
        finish();
    }
}
