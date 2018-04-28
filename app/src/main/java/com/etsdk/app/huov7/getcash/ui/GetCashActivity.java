package com.etsdk.app.huov7.getcash.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.getcash.GetCaseApi;
import com.etsdk.app.huov7.getcash.model.AccountListBean;
import com.etsdk.app.huov7.getcash.model.AccountListRequestBean;
import com.etsdk.app.huov7.getcash.model.GetCashRequestBean;
import com.etsdk.app.huov7.getcash.model.ResultBean;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.control.LoginControl;
import com.liang530.log.L;
import com.liang530.log.T;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 提现页面
 */
public class GetCashActivity extends ImmerseActivity {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    @BindView(R.id.tv_account_type)
    TextView tvAccountType;
    @BindView(R.id.tv_account_name)
    TextView tvAccountName;
    @BindView(R.id.tv_accout)
    TextView tvAccout;
    @BindView(R.id.rl_account)
    RelativeLayout rlAccount;
    @BindView(R.id.tv_account_balance)
    TextView tvAccountBalance;
    @BindView(R.id.et_get_num)
    EditText etGetNum;
    @BindView(R.id.btn_getcash)
    TextView btnGetcash;

    float mBalance = 0;
    int accountId = -1;
    @BindView(R.id.tv_cash)
    TextView tvCash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_cash);
        ButterKnife.bind(this);
        setupUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupUI() {
        tvTitleName.setText("提现");
        swrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAccountData();
                getPtbBalance();
            }
        });
        //设置颜色
        swrefresh.setColorSchemeResources(R.color.bg_blue,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        getAccountData();
        getPtbBalance();
        etGetNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    tvCash.setText("元");
                } else {
                    tvCash.setText("元，到账："+Integer.parseInt(s.toString())*97/100f+"元");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setupAccountData(AccountListBean.CashListBean accountBean) {
        if (accountBean == null) {
            tvAccountType.setVisibility(View.GONE);
            tvAccout.setVisibility(View.GONE);
            tvAccountName.setText("对不起，您还没有选择提现账户，去添加");
            return;
        } else {
            tvAccountType.setVisibility(View.VISIBLE);
            tvAccout.setVisibility(View.VISIBLE);
        }
        accountId = accountBean.getId();
        tvAccountName.setText(accountBean.getCardname());//开户名
        String account = null;
        String payType = null;
        if ("银行卡".equals(accountBean.getPaytype())) {
            payType = accountBean.getBankname();
            String bankAccount = accountBean.getAccount();
            StringBuffer sb = new StringBuffer();
            sb.append(bankAccount.substring(0, 4))
                    .append(" **** **** ")
                    .append(bankAccount.substring(bankAccount.length() - 4, bankAccount.length()));
            account = sb.toString();
        } else if ("支付宝".equals(accountBean.getPaytype())) {
            account = accountBean.getAccount();
            payType = "支付宝";
        } else if ("微信".equals(accountBean.getPaytype())) {
            account = accountBean.getAccount();
            payType = "微信";
        }
        tvAccout.setText(account);//账号、卡号
        tvAccountType.setText(payType);//类型
    }

    /**
     * 获取默认账户
     */
    public void getAccountData() {
        swrefresh.setRefreshing(true);
        AccountListRequestBean requestBean = new AccountListRequestBean();
        requestBean.setUsername(LoginControl.getAccount());
        requestBean.setIsdefault(1);//取默认账户
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<AccountListBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(final AccountListBean data) {
                swrefresh.setRefreshing(false);
                if (data != null) {
                    setupAccountData(data.getCash_list().get(0));
                } else {
                    setupAccountData(null);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                L.e(TAG, code + " " + msg);
                swrefresh.setRefreshing(false);
                setupAccountData(null);
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(GetCaseApi.URL_ACOUNT_LIST), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    /**
     * 获取平台币余额
     */
    public void getPtbBalance(){
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserInfoResultBean>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                if (data != null) {
                    mBalance = data.getPtbcnt();
                    tvAccountBalance.setText(mBalance+"元");
                }
            }
        };
        httpCallbackDecode.setShowTs(false);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userDetailApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            setupAccountData((AccountListBean.CashListBean) data.getSerializableExtra("data"));
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, GetCashActivity.class);
        context.startActivity(starter);
    }

    @OnClick({R.id.iv_titleLeft, R.id.rl_account, R.id.btn_getcash})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.rl_account:
                ChooseAccountActivity.startForResualt(this, accountId);
                break;
            case R.id.btn_getcash:
                getCash();
                break;
        }
    }

    /**
     * 提现请求
     */
    private void getCash() {
        if (accountId < 0) {
            T.s(mContext, "请先选择要提现的账户");
            return;
        }
        float cash = 0;
        try {
            cash = Float.parseFloat(etGetNum.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (cash <= 0) {
            T.s(mContext, "提现数额需要大于0");
            return;
        }
        if (mBalance < cash) {
            T.s(mContext, "余额不足" + cash);
            return;
        }
        final GetCashRequestBean requestBean = new GetCashRequestBean();
        requestBean.setInfoid(accountId);
        requestBean.setMoney(cash);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<ResultBean>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(ResultBean data) {
            }

            @Override
            public void onDataSuccess(ResultBean data, String code, String msg) {
                super.onDataSuccess(data, code, msg);
                if ("200".equals(code)) {
                    T.s(mContext, "已成功申请提现！");
                    finish();
                } else {
                    T.s(mContext, "申请失败" + msg);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                T.s(mContext, "请求失败" + code);
                L.e(TAG, "请求失败 " + code + " " + msg);
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(GetCaseApi.URL_ACOUNT_GET_CASH), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }
}
