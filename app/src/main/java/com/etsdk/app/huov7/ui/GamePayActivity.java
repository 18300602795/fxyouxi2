package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.CouponPay;
import com.etsdk.app.huov7.model.GamePayPreRequestBean;
import com.etsdk.app.huov7.model.GamePayRequestBean;
import com.etsdk.app.huov7.model.GamePayResultBean;
import com.etsdk.app.huov7.model.MoneySelect;
import com.etsdk.app.huov7.model.PayOrderInfoRequestBean;
import com.etsdk.app.huov7.model.PayResultBean;
import com.etsdk.app.huov7.model.PreOrderInfoResultBean;
import com.etsdk.app.huov7.model.QueryOrderRequestBean;
import com.etsdk.app.huov7.model.QueryOrderResultBean;
import com.etsdk.app.huov7.pay.ChargeActivityForWap;
import com.etsdk.app.huov7.pay.CommonJsForWeb;
import com.etsdk.app.huov7.pay.IPayListener;
import com.etsdk.app.huov7.provider.CouponPayViewProvider;
import com.etsdk.app.huov7.provider.MoneySelectViewProvider;
import com.etsdk.app.huov7.ui.dialog.SelectPayTypeDialog;
import com.etsdk.app.huov7.view.LoadStatusView;
import com.game.sdk.db.impl.UserLoginInfodao;
import com.game.sdk.domain.UserInfo;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.log.T;
import com.liang530.utils.BaseAppUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class GamePayActivity extends ImmerseActivity implements SelectPayTypeDialog.SelectPayTypeListener, IPayListener {
    @BindView(R.id.tv_userName)
    TextView tvUserName;
    @BindView(R.id.tv_gameName)
    TextView tvGameName;
    @BindView(R.id.recycler_money)
    RecyclerView recyclerMoney;
    @BindView(R.id.et_inputMoney)
    EditText etInputMoney;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.tv_ailebi)
    TextView tvAilebi;
    @BindView(R.id.tv_useCouponHint)
    TextView tvUseCouponHint;
    @BindView(R.id.recyclerview_coupon)
    RecyclerView recyclerviewCoupon;
    @BindView(R.id.tv_pay_money)
    TextView tvPayMoney;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.tv_rewardScoreHint)
    TextView tvRewardScoreHint;
    @BindView(R.id.tv_payDesc)
    TextView tvPayDesc;
    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView tvTitleRight;
    @BindView(R.id.activity_game_pay)
    LinearLayout activityGamePay;
    @BindView(R.id.tv_couponUseDesc)
    TextView tvCouponUseDesc;
    @BindView(R.id.loadview)
    LoadStatusView loadview;
    private Items moneyItems = new Items();
    private Items couponItems = new Items();
    private String gameId;
    private String gameName;
    private GamePayResultBean mGamePayResultBean;//支付数据
    private PreOrderInfoResultBean preOrderInfoResultBean;
    SelectPayTypeDialog selectPayTypeDialog;
    CommonJsForWeb commonJsForWeb = new CommonJsForWeb(this, "", this);
    private float beforeSelectMoney;//上一次要支付的金额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_pay);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        Intent intent = getIntent();
        gameId = intent.getStringExtra("gameId");
        gameName = intent.getStringExtra("gameName");
        tvGameName.setText(gameName);
        UserInfo userInfoLast = UserLoginInfodao.getInstance(mActivity).getUserInfoLast();
        if (userInfoLast != null) {
            tvUserName.setText(userInfoLast.username);
        }
        tvTitleName.setText("支付中心");
        recyclerMoney.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerMoney.setTag(6);//设置默认选中
        moneyItems.add(new MoneySelect("6元", 6, true));
        moneyItems.add(new MoneySelect("50元", 50, false));
        moneyItems.add(new MoneySelect("100元", 100, false));
        moneyItems.add(new MoneySelect("200元", 200, false));
        moneyItems.add(new MoneySelect("500元", 500, false));
        moneyItems.add(new MoneySelect("1000元", 1000, false));
        moneyItems.add(new MoneySelect("2000元", 2000, false));
        moneyItems.add(new MoneySelect("5000元", 5000, false));
        MultiTypeAdapter moneyAdapter = new MultiTypeAdapter(moneyItems);
        moneyAdapter.register(MoneySelect.class, new MoneySelectViewProvider(recyclerMoney, this));
        recyclerMoney.setAdapter(moneyAdapter);
        int dp6Px = BaseAppUtil.dip2px(this, 6);
        etInputMoney.getLayoutParams().width = (BaseAppUtil.getDeviceWidth(this) - 12 * dp6Px) / 4;
        etInputMoney.setLayoutParams(etInputMoney.getLayoutParams());
        //代金券
        recyclerviewCoupon.setLayoutManager(new LinearLayoutManager(this));
        MultiTypeAdapter couponAdapter = new MultiTypeAdapter(couponItems);
        couponAdapter.register(CouponPay.class, new CouponPayViewProvider(this));
        recyclerviewCoupon.setAdapter(couponAdapter);
        etInputMoney.addTextChangedListener(textWatcher);
        selectPayTypeDialog = new SelectPayTypeDialog(this);
        loadview.showLoading();
        loadview.setOnLoadRefreshListener(new LoadStatusView.OnLoadRefreshListener() {
            @Override
            public void onLoadRefresh() {
                getGamePayData();
            }
        });
        getGamePayData();
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if ("0".equals(etInputMoney.getText().toString())) {
                etInputMoney.setText("1");
            }
            if (TextUtils.isEmpty(etInputMoney.getText().toString())) {
                if ((int) recyclerMoney.getTag() == 0) {//
                    recyclerMoney.setTag(6);
                }
                recyclerMoney.getAdapter().notifyDataSetChanged();
                updateGamePayUi();
                getGamePayData();//联网刷新数据
            } else {
                recyclerMoney.setTag(0);
                recyclerMoney.getAdapter().notifyDataSetChanged();
                updateGamePayUi();
                getGamePayData();//联网刷新数据
            }
        }
    };

    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight, R.id.btn_submit, R.id.tv_couponUseDesc})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
            case R.id.btn_submit:
                submitPreOrder();
                break;
            case R.id.tv_couponUseDesc:
                CardCouponIntroduceActivity.start(mContext);
                break;
        }
    }

    /**
     * 每次点击后，先更新ui数据（判断是否重置代金券）,后请求能获得积分
     *
     * @param gamePayResultBean
     */
    void updateGamePayData(GamePayResultBean gamePayResultBean) {

        if (mGamePayResultBean == null) {
            mGamePayResultBean = new GamePayResultBean();
        }
        mGamePayResultBean.setIntegral(gamePayResultBean.getIntegral());
        mGamePayResultBean.setRate(gamePayResultBean.getRate());
        mGamePayResultBean.setMaxcoupon(gamePayResultBean.getMaxcoupon());
        mergeOldSelectCouponList(gamePayResultBean.getList());
        recyclerviewCoupon.getAdapter().notifyDataSetChanged();
        updateGamePayUi();
    }

    /**
     * 使用新的代金券列表合并老的代金券列表
     * 老的选中的代金券可能数量没那么多了，也可能被用掉了，需要合并处理
     *
     * @param newCouponPayList
     */
    private void mergeOldSelectCouponList(List<CouponPay> newCouponPayList) {
        if (newCouponPayList == null) {
            newCouponPayList = new ArrayList<>();
        }
        List<CouponPay> oldCouponList = mGamePayResultBean.getList();
        if (oldCouponList == null) {
            oldCouponList = new ArrayList<>();
        }
        for (CouponPay oldCouponPay : oldCouponList) {
            for (CouponPay newCouponPay : newCouponPayList) {
                if (newCouponPay.getCouponid() == oldCouponPay.getCouponid()) {//在新的中找到了
                    if (oldCouponPay.getSelectCount() <= newCouponPay.getTotal()) {//数量足够
                        newCouponPay.setSelectCount(oldCouponPay.getSelectCount());
                    } else {
                        newCouponPay.setSelectCount(newCouponPay.getTotal());
                    }
                }
            }
        }
        couponItems.clear();
        oldCouponList.clear();
        oldCouponList.addAll(newCouponPayList);
        couponItems.addAll(oldCouponList);
        mGamePayResultBean.setList(oldCouponList);
    }

    public void updateGamePayUi() {
        if (mGamePayResultBean == null) {
            return;
        }
        float rechargeMoney;
        String inputMoney = etInputMoney.getText().toString().trim();
        if (!TextUtils.isEmpty(inputMoney)) {
            rechargeMoney = Float.valueOf(inputMoney);
        } else {
            rechargeMoney = (int) recyclerMoney.getTag();
        }
        float ailibi = rechargeMoney * mGamePayResultBean.getRate();
        tvAilebi.setText("" + ailibi);
        if (((int) ailibi) == ailibi) {
            tvAilebi.setText("" + (int) ailibi);
        }
        tvRewardScoreHint.setText("可获得" + mGamePayResultBean.getIntegral() + "积分");
        tvUseCouponHint.setText("当前可使用代金券" + mGamePayResultBean.getMaxcoupon() + "元");
        float realPayMoney;//实际需要支付
        //计算所选代金券的金额是否超出
        if (!isSelectCouponMoneyOK()) {
            List<CouponPay> couponPayList = mGamePayResultBean.getList();
            if (couponPayList != null) {
                for (CouponPay couponPay : couponPayList) {
                    couponPay.setSelectCount(0);
                }
            }
            recyclerviewCoupon.getAdapter().notifyDataSetChanged();
            realPayMoney = rechargeMoney;

        } else {
            realPayMoney = rechargeMoney - calculateCouponMoney();
            tvPayMoney.setText((rechargeMoney - calculateCouponMoney()) + "");
        }
        if (((int) realPayMoney) == realPayMoney) {
            tvPayMoney.setText(((int) realPayMoney) + "");
        } else {
            tvPayMoney.setText(realPayMoney + "");
        }

    }

    /**
     * 获取最大可用代金券额度
     *
     * @return
     */
    public String getMaxCouponMoney() {
        if (mGamePayResultBean != null) {
            return mGamePayResultBean.getMaxcoupon();
        }
        return "0";
    }

    /**
     * 要充值的金额
     *
     * @return
     */
    private float getRechargeMoney() {
        float realMoney;
        String inputMoney = etInputMoney.getText().toString().trim();
        if (!TextUtils.isEmpty(inputMoney)) {
            realMoney = Float.valueOf(inputMoney);
        } else {
            realMoney = (int) recyclerMoney.getTag();
        }
        return realMoney;
    }

    /**
     * 判断当前所选的代金券是否ok
     *
     * @return true ok  false 超出额度
     */
    public boolean isSelectCouponMoneyOK() {
        if (mGamePayResultBean != null) {
            try {
                if (calculateCouponMoney() > Float.parseFloat(mGamePayResultBean.getMaxcoupon())) {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private float calculateCouponMoney() {
        List<CouponPay> couponPayList = mGamePayResultBean.getList();
        float allCouponMoney = 0;
        if (couponPayList != null) {
            for (CouponPay couponPay : couponPayList) {
                try {
                    allCouponMoney += Float.parseFloat(couponPay.getMoney()) * couponPay.getSelectCount();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return allCouponMoney;
    }

    /**
     * 获取选中代金券的拼接串
     */
    private String getCouponContent() {
        if (mGamePayResultBean == null) return "";
        List<CouponPay> couponPayList = mGamePayResultBean.getList();
        StringBuffer couponContent = new StringBuffer();
        if (couponPayList != null) {
            for (CouponPay couponPay : couponPayList) {
                try {
                    if (couponPay.getSelectCount() != 0) {
                        couponContent.append(couponPay.getCouponid() + ":" + couponPay.getSelectCount() + ",");
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        String content = couponContent.toString();
        if (content.endsWith(",")) {
            return content.substring(0, content.length() - 1);
        } else {
            return content;
        }
    }

    public void setSelectTemplateMoney() {
        if ((int) recyclerMoney.getTag() != 0) {//选择了，清除输入框
            etInputMoney.setText("");
        }
        updateGamePayUi();//更新ui
        getGamePayData();//重新联网获取积分值
    }

    /**
     * 获取页面数据
     */
    public void getGamePayData() {
        float rechargeMoney = getRechargeMoney();
        if (rechargeMoney < beforeSelectMoney) {//实际想要支付的金额小于上一次的金额，降级代金券要清零
            List<CouponPay> couponPayList = mGamePayResultBean.getList();
            if (couponPayList != null) {
                for (CouponPay couponPay : couponPayList) {
                    couponPay.setSelectCount(0);
                }
            }
        }
        beforeSelectMoney = rechargeMoney;
        final GamePayPreRequestBean gamePayPreRequestBean = new GamePayPreRequestBean();
        gamePayPreRequestBean.setGameid(gameId);
        gamePayPreRequestBean.setMoney(rechargeMoney + "");
        gamePayPreRequestBean.setCouponcontent(getCouponContent());
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(gamePayPreRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<GamePayResultBean>(mActivity, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(GamePayResultBean data) {
                if (data != null) {
                    updateGamePayData(data);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                //联网结束后，数据还没拿到，联网失败，显示加载失败页面
                if (mGamePayResultBean==null){
                    loadview.showFail();
                }else{
                    loadview.showSuccess();
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.gameMoneyChargeApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);

    }

    /**
     * 预下单，获取支付方式
     */
    private void submitPreOrder() {
        final PayOrderInfoRequestBean payOrderInfoRequestBean = new PayOrderInfoRequestBean();
        PayOrderInfoRequestBean.OrderInfo orderInfo = new PayOrderInfoRequestBean.OrderInfo();
        orderInfo.setReal_money(tvPayMoney.getText().toString());
        orderInfo.setCouponcontent(getCouponContent());
        orderInfo.setMoney(getRechargeMoney() + "");
        orderInfo.setGamemoney((getRechargeMoney() * mGamePayResultBean.getRate()) + "");
        orderInfo.setIntegral(mGamePayResultBean.getIntegral() + "");
        payOrderInfoRequestBean.setOrderinfo(orderInfo);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(payOrderInfoRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<PreOrderInfoResultBean>(mActivity, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(PreOrderInfoResultBean data) {
                if (data != null) {
                    preOrderInfoResultBean = data;
                    //显示支付方式对话框
                    if (data.getList() != null) {
                        selectPayTypeDialog.showPayTypeDialog(mContext, data);
                    } else {
                        T.s(mContext, "未获取到支付方式");
                    }
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.gamePreorderApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    /**
     * 获取对应支付的支付数据
     */
    public void getPayTypeInfo(String paytype) {
        final GamePayRequestBean gamePayRequestBean = new GamePayRequestBean();
        gamePayRequestBean.setPaytype(paytype);
        gamePayRequestBean.setOrderid(preOrderInfoResultBean.getOrder_id());
        gamePayRequestBean.setPaytoken(preOrderInfoResultBean.getPaytoken());
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(gamePayRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<PayResultBean>(mActivity, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(PayResultBean data) {
                if (data != null) {
//                    commonJsForWeb.huoPay(data);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.gamePayTypeApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }


    public static void start(Context context, String gameId, String gameName) {
        //web支付
        ChargeActivityForWap.start(context, AppApi.getUrl(AppApi.chargeGame),
                gameName, gameId);
//        Intent starter = new Intent(context, GamePayActivity.class);
//        starter.putExtra("gameId", gameId);
//        starter.putExtra("gameName", gameName);
//        context.startActivity(starter);
    }

    @Override
    public void selectPayType(String paytype) {
        getPayTypeInfo(paytype);
    }

    @Override
    public void paySuccess(String orderId, float money) {
        T.s(mContext, "支付成功");
        GameMoneyListActivity.start(mContext);
    }

    @Override
    public void payFail(String orderId, float money, boolean queryOrder, String msg) {
        if (queryOrder) {
            queryOrder(orderId, money, msg);
        } else {
            T.s(mContext, "支付失败");
        }
    }

    /**
     * 向服务器查询支付结果
     */
    private void queryOrder(String orderId, final float money, final String msg) {
        //向服务器查询订单结果
        QueryOrderRequestBean queryOrderRequestBean = new QueryOrderRequestBean();
        queryOrderRequestBean.setOrder_id(orderId);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(queryOrderRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<QueryOrderResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(QueryOrderResultBean data) {
                if (data != null) {
                    if ("2".equals(data.getStatus())) {
                        T.s(mContext, "支付成功");
                        GameMoneyListActivity.start(mContext);
                    } else {
                        T.s(mContext, "支付失败");
                    }
                } else {
                    T.s(mContext, "支付失败");
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                T.s(mContext, "支付失败");
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        httpCallbackDecode.setLoadMsg("查询支付结果中……");
        RxVolley.post(AppApi.queryorderApi, httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        commonJsForWeb.onDestory();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        commonJsForWeb.onActivityResult(requestCode, resultCode, data);
    }
}
