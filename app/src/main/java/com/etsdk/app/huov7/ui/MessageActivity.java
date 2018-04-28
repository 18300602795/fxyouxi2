package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.MessageAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.ListRequestBean;
import com.etsdk.app.huov7.model.Message;
import com.etsdk.app.huov7.model.MessageDeleteRequestBean;
import com.etsdk.app.huov7.model.MessageRequestBean;
import com.etsdk.app.huov7.model.ShowMsg;
import com.etsdk.app.huov7.provider.MessageViewProvider;
import com.etsdk.app.huov7.ui.dialog.DeleteMessageDialogUtil;
import com.etsdk.app.huov7.util.RecyclerViewNoAnimator;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class MessageActivity extends ImmerseActivity implements AdvRefreshListener {
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
    @BindView(R.id.activity_message)
    LinearLayout activityMessage;
    MessageAdapter messageAdapter;
    BaseRefreshLayout baseRefreshLayout;
    private Items items = new Items();
    private MultiTypeAdapter multiTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        setupUI();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(Boolean isLogin) {
        if (isLogin) {
            baseRefreshLayout.refresh();
        } else {
            finish();
        }
    }


    private void setupUI() {
        tvTitleName.setText("消息中心");
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new RecyclerViewNoAnimator());
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(Message.class, new MessageViewProvider(this));
        // 设置适配器
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.refresh();
    }

    public MultiTypeAdapter getMultiTypeAdapter() {
        return multiTypeAdapter;
    }

    @Override
    public void getPageData(final int pageNo) {
        final ListRequestBean baseRequestBean = new ListRequestBean();
        baseRequestBean.setPage(pageNo + "");
        baseRequestBean.setOffset("20");
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<MessageRequestBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(MessageRequestBean data) {
//                if(data!=null){
//                    if(data.getList()==null){
//                        baseRefreshLayout.resultLoadData(items,new ArrayList(),1);
//                    }else{
//                        baseRefreshLayout.resultLoadData(items,data.getList(),1);
//                    }
//                }else{
//                    baseRefreshLayout.resultLoadData(items,new ArrayList(),1);
//                }
                if (data != null && data.getList() != null) {
                    int maxPage = (int) Math.ceil(data.getCount() / 20.);
                    baseRefreshLayout.resultLoadData(items, data.getList(), maxPage);
                } else {
                    baseRefreshLayout.resultLoadData(items, new ArrayList(), pageNo - 1);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                baseRefreshLayout.resultLoadData(items, null, pageNo);
                EventBus.getDefault().postSticky(new ShowMsg(false, true));
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userMsgListApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            EventBus.getDefault().post(true);
        }
        return super.onKeyDown(keyCode, event);
    }


    public static void start(Context context) {
        Intent starter = new Intent(context, MessageActivity.class);
        context.startActivity(starter);
    }

    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                EventBus.getDefault().post(true);
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
        }
    }

    /**
     * 删除消息
     *
     * @param message
     */
    public void hintDeleteMessage(final Message message) {
        new DeleteMessageDialogUtil().showDeleteMessageDialog(this, message.getTitle(), new DeleteMessageDialogUtil.ConfirmDialogListener() {
            @Override
            public void ok() {
                deleteMessage(message);
            }

            @Override
            public void cancel() {
            }
        });
    }

    private void deleteMessage(final Message message) {
        MessageDeleteRequestBean messageDeleteRequestBean = new MessageDeleteRequestBean();
        messageDeleteRequestBean.setMsgid(message.getMsgid());
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(messageDeleteRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<String>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(String data) {
                items.remove(message);
                multiTypeAdapter.notifyDataSetChanged();
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userMsgDeleteApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }
}
