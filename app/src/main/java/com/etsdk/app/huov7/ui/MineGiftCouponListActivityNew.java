package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.VpAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.ui.fragment.MineCardFragment;
import com.etsdk.app.huov7.ui.fragment.MineCouponFragment;
import com.etsdk.app.huov7.ui.fragment.MineEntityFragment;
import com.etsdk.app.huov7.ui.fragment.MineGiftFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MineGiftCouponListActivityNew extends ImmerseActivity {
    public static final int TYPE_GIFT = 0;
    public static final int TYPE_COUPON = 1;

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_coupon_list_new);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        type = getIntent().getIntExtra("type", 0);
        String title = getIntent().getStringExtra("title");
        tvTitleName.setText(title);
        if(type == TYPE_GIFT) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment, new MineGiftFragment()).commit();
        }else if(type == TYPE_COUPON){
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment, new MineCouponFragment()).commit();
        }
    }

    @OnClick({R.id.iv_titleLeft})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
        }
    }

    public static void start(Context context, int type, String title) {
        Intent starter = new Intent(context, MineGiftCouponListActivityNew.class);
        starter.putExtra("type", type);
        starter.putExtra("title", title);
        context.startActivity(starter);
    }
}
