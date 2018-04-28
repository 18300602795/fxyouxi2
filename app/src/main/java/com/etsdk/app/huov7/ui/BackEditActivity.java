package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.BackBean;
import com.etsdk.app.huov7.model.BackRecordList;
import com.etsdk.app.huov7.model.BackResult;
import com.etsdk.app.huov7.shop.model.SelectGameEvent;
import com.etsdk.app.huov7.ui.dialog.HintDialogUtil;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.log.T;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/28.
 */

public class BackEditActivity extends ImmerseActivity {
    @BindView(R.id.back_return)
    ImageView back_return;
    @BindView(R.id.back_title)
    TextView back_title;
    @BindView(R.id.state_iv)
    ImageView state_iv;
    @BindView(R.id.back_why)
    EditText back_why;
    @BindView(R.id.back_select_date)
    EditText select_date;
    @BindView(R.id.back_money)
    EditText back_money;
    @BindView(R.id.back_game_name)
    EditText back_game_name;
    @BindView(R.id.back_person_id)
    EditText back_person_id;
    @BindView(R.id.back_game_id)
    EditText back_game_id;
    @BindView(R.id.back_person_name)
    EditText back_person_name;
    @BindView(R.id.back_area)
    EditText back_area;
    @BindView(R.id.back_explain)
    EditText back_explain;
    @BindView(R.id.back_post)
    Button back_post;
    private BackRecordList.BackRecord backRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_edit);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        backRecord = (BackRecordList.BackRecord) getIntent().getSerializableExtra("backBean");
        setupUI();
    }

    private void setupUI() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        select_date.setText(format.format(new Date(Long.valueOf(backRecord.getTime()) * 1000)));
        back_why.setText(backRecord.getObligate());
        back_game_name.setText(backRecord.getGame_name());
        back_person_id.setText(backRecord.getGame_id());
        back_person_name.setText(backRecord.getRole_name());
        back_area.setText(backRecord.getArea_clothing());
        back_money.setText(backRecord.getMoney());
        back_explain.setText(backRecord.getReq());
        switch (backRecord.getStatus()) {
            case "1":
                back_post.setVisibility(View.GONE);
                back_money.setFocusable(false);
                back_explain.setFocusable(false);
                state_iv.setImageResource(R.mipmap.state_succeed);
                break;
            case "2":
                state_iv.setImageResource(R.mipmap.state_loading);
                break;
            case "3":
                back_post.setVisibility(View.GONE);
                back_money.setFocusable(false);
                back_explain.setFocusable(false);
                state_iv.setImageResource(R.mipmap.state_failed);
                break;
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, BackEditActivity.class);
        context.startActivity(starter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectGameEvent(SelectGameEvent selectGameEvent) {
        back_game_name.setText(selectGameEvent.getGameName());
    }


    @OnClick({R.id.back_return, R.id.back_post})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_return:
                finish();
                break;
            case R.id.back_post:
                new HintDialogUtil().showHintDialog(BackEditActivity.this, "友情提示", "是否确定修改返利申请信息", "确定", "取消", new HintDialogUtil.HintDialogListener() {
                    @Override
                    public void ok(String content) {
                        sumitLogin();
                    }

                    @Override
                    public void cancel() {
                    }
                });
                break;
        }
    }

    private void sumitLogin() {
        if (TextUtils.isEmpty(select_date.getText().toString())) {
            T.s(mActivity, "充值时间不能为空");
            return;
        }

        if (TextUtils.isEmpty(back_game_name.getText().toString())) {
            T.s(mActivity, "游戏名不能为空");
            return;
        }

        if (TextUtils.isEmpty(back_person_name.getText().toString())) {
            T.s(mActivity, "角色名称不能为空");
            return;
        }

        if (TextUtils.isEmpty(back_area.getText().toString())) {
            T.s(mActivity, "游戏区服不能为空");
            return;
        }

        if (TextUtils.isEmpty(back_money.getText().toString())) {
            T.s(mActivity, "充值金额不能为空");
            return;
        }

        final BackBean backBean = new BackBean();
        backBean.setDate(select_date.getText().toString());
        backBean.setGame_name(back_game_name.getText().toString());
        backBean.setPerson_id(back_person_id.getText().toString());
        backBean.setPerson_name(back_person_name.getText().toString());
        backBean.setArea(back_area.getText().toString());
        backBean.setMoney(back_money.getText().toString());
        backBean.setReq(back_explain.getText().toString());
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(backBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<BackResult>(this, httpParamsBuild.getAuthkey(), true) {
            @Override
            public void onDataSuccess(BackResult data) {
                if (data.getStatus() == 200) {
                    T.s(mContext, "申请成功，请耐心等待审核通过");
//                    EventBus.getDefault().post(new ShopListRefreshEvent());
                } else {
                    T.s(mContext, "申请失败： " + data.getMsg());
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        httpCallbackDecode.setLoadMsg("正在申请...");
        RxVolley.post(AppApi.getUrl(AppApi.backEdit), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }
}
