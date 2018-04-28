package com.etsdk.app.huov7.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.CouponPay;
import com.etsdk.app.huov7.ui.GamePayActivity;
import com.game.sdk.log.T;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Tang on 2017/1/19 0019.
 */

public class NumberSwitchView extends LinearLayout {
    private Context mContext;
    View mRootView;
    EditText mNumberUpEditView;
    private GamePayActivity gamePayActivity;
    private CouponPay couponPay;
    public NumberSwitchView(Context context) {
        this(context, null);
    }

    public NumberSwitchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.layout_widget_switchnumber, this);
        ButterKnife.bind(mRootView);
        mNumberUpEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int num = Integer.parseInt(mNumberUpEditView.getText().toString());
                handleData(num);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 判断是否可以做出改变
     */
    public void handleData(int num){
        if(num<0){
            num=0;
        }
        int beforeCount = couponPay.getSelectCount();
        couponPay.setSelectCount(num);
        if(!gamePayActivity.isSelectCouponMoneyOK()){
            couponPay.setSelectCount(beforeCount);
            mNumberUpEditView.setText(beforeCount+"");
            T.s(mContext,"所选代金券的总额不得支付总额的一半");
        }else{
            mNumberUpEditView.setText(num+"");
        }
    }


    @OnClick({R.id.tv_widget_number_up, R.id.tv_widget_number_down})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_widget_number_up:
                handleData(couponPay.getSelectCount()+1);
                break;
            case R.id.tv_widget_number_down:
                handleData(couponPay.getSelectCount()-1);
                break;
            default:
                break;
        }
    }
    public void setGamePayActivity(GamePayActivity gamePayActivity, CouponPay couponPay) {
        this.gamePayActivity = gamePayActivity;
        this.couponPay=couponPay;
    }
}
