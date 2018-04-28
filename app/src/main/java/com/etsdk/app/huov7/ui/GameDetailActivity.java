package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.ui.fragment.DetailDescFragment;
import com.etsdk.app.huov7.ui.fragment.DetailFuliFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameDetailActivity extends ImmerseActivity {

    @BindView(R.id.iv_pay)
    ImageView ivPay;
    @BindView(R.id.tv_game_status)
    TextView tvGameStatus;
    @BindView(R.id.ll_size_type)
    LinearLayout llSizeType;
    @BindView(R.id.tv_game_name)
    TextView tvGameName;
    @BindView(R.id.iv_game_img)
    ImageView ivGameImg;
    @BindView(R.id.ll_detail_desc_tab)
    LinearLayout llDetailDescTab;
    @BindView(R.id.ll_detail_fuli_tab)
    LinearLayout llDetailFuliTab;
    @BindView(R.id.fl_detail_frame)
    FrameLayout flDetailFrame;
    @BindView(R.id.pb_download)
    ProgressBar pbDownload;
    @BindView(R.id.activity_game_detail)
    LinearLayout activityGameDetail;
    DetailDescFragment detailDescFragment;
    DetailFuliFragment detailFuliFragment;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        llDetailDescTab.setSelected(true);
        detailDescFragment = new DetailDescFragment();
        detailFuliFragment = new DetailFuliFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_detail_frame, detailDescFragment).commit();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, NewGameDetailActivity.class);
        context.startActivity(starter);
//        NewGameDetailActivity.start( context);
    }

    @OnClick({R.id.ll_detail_desc_tab, R.id.ll_detail_fuli_tab, R.id.iv_pay,R.id.iv_titleLeft, R.id.tv_titleRight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_detail_desc_tab:
                if (!llDetailDescTab.isSelected()) {
                    llDetailDescTab.setSelected(true);
                    llDetailFuliTab.setSelected(false);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_detail_frame, detailDescFragment).commit();

                }
                break;
            case R.id.ll_detail_fuli_tab:
                if (!llDetailFuliTab.isSelected()) {
                    llDetailDescTab.setSelected(false);
                    llDetailFuliTab.setSelected(true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_detail_frame, detailFuliFragment).commit();
                }
                break;
            case R.id.iv_pay:
//                GamePayActivity.start(mContext,);
                break;
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
        }
    }
}
