package com.etsdk.app.huov7.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 活动页用于选择切换的tab
 * Created by Administrator on 2017/6/8 0008.
 */

public class ShopSelectTabView extends FrameLayout {
    @BindView(R.id.tv_buy)
    TextView tvActivityList;
    @BindView(R.id.tv_shell)
    TextView tvLaunchActivity;
    @BindView(R.id.tv_my_activity)
    TextView tvMyActivity;
    @BindView(R.id.iv_arow)
    ImageView ivArow;
    @BindView(R.id.rl_my_deal)
    RelativeLayout rlMyActivity;

    private ViewPager mViewPager;
    private PopupWindow popupWindow;

    public ShopSelectTabView(Context context) {
        super(context);
        setupUI();
    }

    public ShopSelectTabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupUI();
    }

    public ShopSelectTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupUI();
    }

    public void setupUI() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_shop_select_tab, this);
        ButterKnife.bind(this);
        tvActivityList.setTextColor(getResources().getColor(R.color.bg_green));
        tvLaunchActivity.setTextColor(getResources().getColor(R.color.text_black));
        tvMyActivity.setTextColor(getResources().getColor(R.color.text_black));
    }

    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
    }

    @OnClick({R.id.tv_buy, R.id.tv_shell, R.id.rl_my_deal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_buy:
                swichViewPager(0);
                break;
            case R.id.tv_shell:
                swichViewPager(1);
                break;
            case R.id.rl_my_deal:
                showPop();
                break;
        }
    }

    private void showPop() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_shop_tab, null);
        ListView listView = (ListView) contentView.findViewById(R.id.lv_tab);
        listView.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.item_shop_tab, new String[]{"已购买", "出售", "收藏"}));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismissPop();
                if (position == 0) {
                    swichViewPager(2);
                } else if (position == 1) {
                    swichViewPager(3);
                } else if(position == 2){
                    swichViewPager(4);
                }
            }
        });
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(rlMyActivity, 0, 0);
        ivArow.setBackgroundResource(R.mipmap.huosdk_down);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ivArow.setBackgroundResource(R.mipmap.huosdk_up);
            }
        });
    }

    private void dismissPop() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    private void swichViewPager(int position){
        if(mViewPager!=null){
            mViewPager.setCurrentItem(position, false);
            if(position!=1){
                //收起键盘
                ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            switch (position){
                case 0:
                    tvActivityList.setTextColor(getResources().getColor(R.color.bg_green));
                    tvLaunchActivity.setTextColor(getResources().getColor(R.color.text_black));
                    tvMyActivity.setTextColor(getResources().getColor(R.color.text_black));
                    tvMyActivity.setText("我的交易");
                    break;
                case 1:
                    tvActivityList.setTextColor(getResources().getColor(R.color.text_black));
                    tvLaunchActivity.setTextColor(getResources().getColor(R.color.bg_green));
                    tvMyActivity.setTextColor(getResources().getColor(R.color.text_black));
                    tvMyActivity.setText("我的交易");
                    break;
                case 2:
                    tvActivityList.setTextColor(getResources().getColor(R.color.text_black));
                    tvLaunchActivity.setTextColor(getResources().getColor(R.color.text_black));
                    tvMyActivity.setTextColor(getResources().getColor(R.color.bg_green));
                    tvMyActivity.setText("已购买");
                    break;
                case 3:
                    tvActivityList.setTextColor(getResources().getColor(R.color.text_black));
                    tvLaunchActivity.setTextColor(getResources().getColor(R.color.text_black));
                    tvMyActivity.setTextColor(getResources().getColor(R.color.bg_green));
                    tvMyActivity.setText("出售");
                    break;
                case 4:
                    tvActivityList.setTextColor(getResources().getColor(R.color.text_black));
                    tvLaunchActivity.setTextColor(getResources().getColor(R.color.text_black));
                    tvMyActivity.setTextColor(getResources().getColor(R.color.bg_green));
                    tvMyActivity.setText("收藏");
                    break;
            }
        }
    }
}
