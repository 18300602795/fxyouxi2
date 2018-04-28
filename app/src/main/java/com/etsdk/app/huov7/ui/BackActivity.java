package com.etsdk.app.huov7.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.BackBean;
import com.etsdk.app.huov7.model.BackResult;
import com.etsdk.app.huov7.shop.model.SelectGameEvent;
import com.etsdk.app.huov7.shop.ui.MyGameListActivity;
import com.etsdk.app.huov7.ui.dialog.BackHintDialogUtil;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.log.T;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/28.
 */

public class BackActivity extends ImmerseActivity {
    @BindView(R.id.back_return)
    ImageView back_return;
    @BindView(R.id.back_title)
    TextView back_title;
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
    private String gameIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back);
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, BackActivity.class);
        context.startActivity(starter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectGameEvent(SelectGameEvent selectGameEvent) {
        back_game_name.setText(selectGameEvent.getGameName());
        gameIcon = selectGameEvent.getGameIcon();
    }


    @OnClick({R.id.back_return, R.id.back_post, R.id.back_select_date, R.id.back_game_name})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_return:
                finish();
                break;
            case R.id.back_post:
                new BackHintDialogUtil().show(BackActivity.this, new BackHintDialogUtil.Listener() {
                    @Override
                    public void ok() {
                        sumitLogin();
                    }

                    @Override
                    public void cancel() {
                    }
                });

                break;
            case R.id.back_select_date:
                showDateDialog();
                break;
            case R.id.back_game_name:
                MyGameListActivity.start(mContext);
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
        backBean.setGame_icon(gameIcon);
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
        RxVolley.post(AppApi.getUrl(AppApi.backup), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    /**
     * 显示时间选择器
     */
    private void showDateDialog() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(BackActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                String time = "" + year + "-" + (month + 1) + "-" + dayOfMonth;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = format.parse(time);
                    if (date.getTime() > System.currentTimeMillis()){
                        T.s(mContext, "请选择正确的充值时间");
                        select_date.setText("");
                    }else {
                        select_date.setText(time);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, year, month, day).show();
    }
}
