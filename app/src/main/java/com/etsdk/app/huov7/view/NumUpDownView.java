package com.etsdk.app.huov7.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.CouponPay;
import com.etsdk.app.huov7.ui.GamePayActivity;
import com.game.sdk.log.T;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liu hong liang on 2017/2/24.
 */
public class NumUpDownView extends LinearLayout {
    @BindView(R.id.tv_widget_number_down)
    TextView tvWidgetNumberDown;
    @BindView(R.id.et_widget_number)
    EditText etWidgetNumber;
    @BindView(R.id.tv_widget_number_up)
    TextView tvWidgetNumberUp;
    private GamePayActivity gamePayActivity;
    private CouponPay couponPay;

    public NumUpDownView(Context context) {
        super(context);
        initView();
    }

    public NumUpDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public NumUpDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_widget_switchnumber, this);
        ButterKnife.bind(this);
        etWidgetNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int num;
                try {
                    num = Integer.parseInt(etWidgetNumber.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    num=0;
                }
                handleData(num);
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
        if(Integer.valueOf(couponPay.getMyremain())<num){//数量不够
            couponPay.setSelectCount(beforeCount);
            etWidgetNumber.setText(beforeCount+"");
            return;
        }
        if(!gamePayActivity.isSelectCouponMoneyOK()){
            couponPay.setSelectCount(beforeCount);
            etWidgetNumber.setText(beforeCount+"");
            T.s(getContext(),"所选代金券的总额不得超过"+gamePayActivity.getMaxCouponMoney()+"元");
        }else{
            etWidgetNumber.setText(num+"");
            gamePayActivity.getGamePayData();//联网更新
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
        etWidgetNumber.setText(couponPay.getSelectCount()+"");
    }
}
