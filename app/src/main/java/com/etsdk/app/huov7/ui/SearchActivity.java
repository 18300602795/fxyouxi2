package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.SearchHistoryAdapter;
import com.etsdk.app.huov7.adapter.SearchHotAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.GameBeanList;
import com.etsdk.app.huov7.model.MessageEvent;
import com.etsdk.app.huov7.model.SearchEvent;
import com.etsdk.app.huov7.model.SplitLine;
import com.etsdk.app.huov7.provider.GameItemViewProvider;
import com.etsdk.app.huov7.provider.SplitLineViewProvider;
import com.etsdk.app.huov7.util.RecyclerViewNoAnimator;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class SearchActivity extends ImmerseActivity implements AdvRefreshListener {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.iv_gotoMsg)
    ImageView ivGotoMsg;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.rcy_hot_search)
    RecyclerView rcyHotSearch;
    @BindView(R.id.ll_hot_search)
    LinearLayout llHotSearch;
    @BindView(R.id.rcy_search_resault)
    RecyclerView rcySearchReault;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    @BindView(R.id.activity_search)
    LinearLayout activitySearch;
    @BindView(R.id.rcy_history)
    RecyclerView rcyHistory;
    private SearchHotAdapter searchHotAdapter;
    private String searchKey;
    private MVCSwipeRefreshHelper baseRefreshLayout;
    private Items items = new Items();
    private List<GameBean> hotGameList = new ArrayList();
    private SearchHistoryAdapter searchHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setupUI();
    }

    private void setupUI() {
        //热门
        rcyHotSearch.setLayoutManager(new GridLayoutManager(mContext, 3));
        searchHotAdapter = new SearchHotAdapter(hotGameList);
        rcyHotSearch.setAdapter(searchHotAdapter);
        //历史 TODO adapter
        rcyHistory.setLayoutManager(new LinearLayoutManager(mContext));
        searchHistoryAdapter = new SearchHistoryAdapter(mContext);
        rcyHistory.setAdapter(searchHistoryAdapter);
        //搜索结果
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        rcySearchReault.setLayoutManager(new LinearLayoutManager(mContext));
        rcySearchReault.setItemAnimator(new RecyclerViewNoAnimator());
        MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(SplitLine.class, new SplitLineViewProvider());
        multiTypeAdapter.register(GameBean.class, new GameItemViewProvider(false));
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);

        //// 输入完后按键盘上的搜索键
//        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    search();
//                }
//                return false;
//            }
//        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s.toString()) || count == 0){
                    llHotSearch.setVisibility(View.VISIBLE);
                    swrefresh.setVisibility(View.GONE);
                }else{
                    search();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        llHotSearch.setVisibility(View.VISIBLE);
        swrefresh.setVisibility(View.GONE);
//        getHotGameList();//2017/5/18 不显示热搜
    }

    private void getHotGameList() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.gameListApi);
        httpParams.put("hot", 2);
        httpParams.put("page", 1);
        httpParams.put("offset", 6);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.gameListApi), new HttpJsonCallBackDialog<GameBeanList>() {
            @Override
            public void onDataSuccess(GameBeanList data) {
                if (data != null && data.getData() != null && data.getData().getList() != null) {
                    hotGameList.clear();
                    hotGameList.addAll(data.getData().getList());
                    rcyHotSearch.getAdapter().notifyDataSetChanged();
                }
            }
        });
    }

    @OnClick({R.id.iv_titleLeft, R.id.tv_refresh, R.id.iv_gotoMsg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_refresh:
                getHotGameList();
                break;
            case R.id.iv_gotoMsg:
                MessageActivity.start(mContext);
                break;
        }
    }

    private void search() {
        //收起键盘
//        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
//                getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        if (!TextUtils.isEmpty(etSearch.getText().toString().trim())) {
            swrefresh.setVisibility(View.VISIBLE);
            llHotSearch.setVisibility(View.GONE);
            searchKey = etSearch.getText().toString().trim();
            baseRefreshLayout.refresh();
            searchHistoryAdapter.add(searchKey);
        }
    }

    @Override
    public void getPageData(final int requestPageNo) {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.searchIndexApi);
        httpParams.put("searchtype", "game");
        httpParams.put("q", searchKey);
        httpParams.put("page", requestPageNo);
        httpParams.put("offset", 20);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.searchIndexApi), new HttpJsonCallBackDialog<GameBeanList>() {
            @Override
            public void onDataSuccess(GameBeanList data) {
                if (data != null && data.getData() != null && data.getData().getList() != null) {
                    int maxPage = (int) Math.ceil(data.getData().getCount() / 20.);
                    baseRefreshLayout.resultLoadData(items, data.getData().getList(), maxPage);
                } else {
                    baseRefreshLayout.resultLoadData(items, new ArrayList(), requestPageNo - 1);
                }
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                baseRefreshLayout.resultLoadData(items, null, null);
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                baseRefreshLayout.resultLoadData(items, null, null);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(searchHistoryAdapter != null){
            searchHistoryAdapter.closeDb();
        }
        EventBus.getDefault().unregister(this);
    }

    /**
     * 搜索事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN,priority = 2)
    public void onSwitchFragmentEvent(SearchEvent event){
        etSearch.setText(event.word);
        search();
    }

    /**
     * 用户消息
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        if (messageEvent != null && "2".equals(messageEvent.getNewMsg())) {
            ivGotoMsg.setImageResource(R.mipmap.syxiaoxi_red);
        } else {
            ivGotoMsg.setImageResource(R.mipmap.syxiaoxi_nomal);
        }
    }
    public static void start(Context context) {
        Intent starter = new Intent(context, SearchActivity.class);
        context.startActivity(starter);
    }
}
