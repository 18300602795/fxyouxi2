package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.GameImageAdapter;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.GameBeanList;
import com.etsdk.app.huov7.model.StartServerInfo;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.etsdk.app.huov7.provider.GameItemViewProvider;
import com.etsdk.app.huov7.ui.GameFeedbackActivity;
import com.etsdk.app.huov7.view.GameStartInfoView;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by liu hong liang on 2016/12/7.
 */

public class DetailDescFragment extends AutoLazyFragment {
    @BindView(R.id.rcy_game_imgs)
    RecyclerView rcyGameImgs;
    @BindView(R.id.expandable_text)
    TextView expandableText;
    @BindView(R.id.expand_collapse)
    ImageButton expandCollapse;
    @BindView(R.id.expand_text_view)
    ExpandableTextView expandTextView;
    @BindView(R.id.welfare_text_view)
    ExpandableTextView expandTextView2;
    @BindView(R.id.rl_game_introduce)
    LinearLayout rlGameIntroduce;
    @BindView(R.id.tv_like_refresh)
    TextView tvLikeRefresh;
    @BindView(R.id.recycler_like)
    RecyclerView recyclerLike;
    @BindView(R.id.tv_goto_feedback)
    TextView tvGotoFeedback;
    @BindView(R.id.image_split_line)
    View imageSplitLine;
    @BindView(R.id.gameStartInfoView)
    GameStartInfoView gameStartInfoView;
    @BindView(R.id.tv_curVersion)
    TextView tvCurVersion;
    @BindView(R.id.tv_supportSystem)
    TextView tvSupportSystem;
    @BindView(R.id.tv_updateInfo)
    TextView tvUpdateInfo;
    @BindView(R.id.rl_likeGame)
    RelativeLayout rlLikeGame;
    private ArrayList<String> imageList = new ArrayList<>();
    private Items likeGameList = new Items();
    private GameBean gameBean;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_detail_desc);
        setupUI();
    }

    private void setupUI() {
        rcyGameImgs.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
//        imageList.add("http://16pt.cn/upload/shot/hc_1471488704_57b522c09e341.jpg");
//        imageList.add("http://16pt.cn/upload/shot/hc_1471488704_57b522c0b2e9f.jpg");
//        imageList.add("http://16pt.cn/upload/shot/hc_1471488704_57b522c0bf20e.jpg");
        rcyGameImgs.setAdapter(new GameImageAdapter(imageList));
        rcyGameImgs.setNestedScrollingEnabled(false);
        recyclerLike.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerLike.setNestedScrollingEnabled(false);
        MultiTypeAdapter likeRcyAdapter = new MultiTypeAdapter(likeGameList);
        likeRcyAdapter.register(GameBean.class, new GameItemViewProvider());
        recyclerLike.setAdapter(likeRcyAdapter);
        getLikeGameData();
        setupGameData(gameBean);
    }

    public void setupGameData(GameBean gameBean) {
        if (gameBean == null) return;
        this.gameBean = gameBean;
        if (rcyGameImgs == null) {//懒加载，资源还未被初始化
            return;
        }
        if (gameBean.getImage() == null) {
            rcyGameImgs.setVisibility(View.GONE);
            imageSplitLine.setVisibility(View.GONE);
        } else {
            imageList.addAll(gameBean.getImage());
            rcyGameImgs.getAdapter().notifyDataSetChanged();
        }
//        String des = gameBean.getDesc();
//        L.i("333", "des：" + des);
//        if (des.contains("\n")) {
//            int first = des.indexOf("\n");
//            String des1 = des.substring(0, first);
//            String des2 = des.substring(first + 1);
//            expandTextView2.setText(des2);
//            expandTextView.setText(des1);
//        } else {
//            expandTextView.setText(des);
//        }
        expandTextView.setText(gameBean.getDesc());
        expandTextView2.setText(gameBean.getWelfare());
        tvCurVersion.setText(gameBean.getVersion());
        tvSupportSystem.setText(gameBean.getSys());
        tvUpdateInfo.setText(gameBean.getUpinfo());
        setGameStartInfoView(gameBean.getSerlist());
    }

    private void setGameStartInfoView(List<StartServerInfo> startServerInfoList) {

        if (startServerInfoList == null || startServerInfoList.size() == 0) {
            gameStartInfoView.setVisibility(View.GONE);
        } else {
            gameStartInfoView.setVisibility(View.VISIBLE);
            gameStartInfoView.setDatas(startServerInfoList);
        }
    }

    /**
     * 获取猜你喜欢游戏列表
     */
    public void getLikeGameData() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.gameListApi);
        httpParams.put("rand", 1);
        httpParams.put("cnt", 3);
        httpParams.put("page", 1);
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.gameListApi), new HttpJsonCallBackDialog<GameBeanList>() {
            @Override
            public void onDataSuccess(GameBeanList data) {
                if (data != null && data.getData() != null && data.getData().getList() != null) {
                    likeGameList.clear();
                    likeGameList.addAll(data.getData().getList());
                    recyclerLike.getAdapter().notifyDataSetChanged();
                } else {
                    if (likeGameList.size() == 0) {
                        rlLikeGame.setVisibility(View.GONE);//没有数据时隐藏猜你喜欢
                    }
                }
            }
        });
    }


    @OnClick({R.id.tv_like_refresh, R.id.tv_goto_feedback})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_like_refresh:
                getLikeGameData();
                break;
            case R.id.tv_goto_feedback:
                getUserInfoData();
                break;
        }
    }

    public void getUserInfoData() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserInfoResultBean>(getContext(), httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                if (data != null) {
                    GameFeedbackActivity.start(mContext, gameBean.getGameid());
                }
            }

            @Override
            public void onFailure(String code, String msg) {

            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userDetailApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }
}
