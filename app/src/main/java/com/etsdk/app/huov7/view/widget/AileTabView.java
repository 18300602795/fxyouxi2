package com.etsdk.app.huov7.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.utils.FragmentChangeManager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/20 0020.
 */

public class AileTabView extends FrameLayout {

    private Context mContext = null;
    private LinearLayout mTabsContainer;
    private ArrayList<CustomTabEntity> mTabEntitys = new ArrayList<>();
    private int mTabCount;
    private int mTextSelectColor = Color.parseColor("#46b400");
    private int mTextUnselectColor = Color.parseColor("#000000");
    private OnTabSelectListener mListener;
    private int mCurrentTab;
    private int mLastTab;

    public AileTabView(Context context) {

        this(context, null);
    }

    public AileTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AileTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    //-------------------------------------------------

    private void initView() {
        mTabsContainer = new LinearLayout(mContext);
        addView(mTabsContainer);

    }

    public void setTabData(ArrayList<CustomTabEntity> tabEntitys) {
        if (tabEntitys == null || tabEntitys.size() == 0) {
            throw new IllegalStateException("TabEntitys can not be NULL or EMPTY !");
        }

        this.mTabEntitys.clear();
        this.mTabEntitys.addAll(tabEntitys);

        notifyDataSetChanged();
    }

    public void setTabViewBackgroud(int resid, int position) {

        if (mTabsContainer.getChildAt(position) != null) {

            mTabsContainer.getChildAt(position).setBackgroundResource(resid);
        }

    }

    /**
     * 更新数据
     */
    public void notifyDataSetChanged() {
        mTabsContainer.removeAllViews();
        this.mTabCount = mTabEntitys.size();
        for (int i = 0; i < mTabCount; i++) {
            View tabView = LayoutInflater.from(mContext).inflate(R.layout.layout_tab_item, null);
            tabView.setTag(i);
            //添加子View
            addTab(i, tabView);
        }

        updateTabStyles();
    }


    public void setOnTabSelectListener(OnTabSelectListener listener) {
        this.mListener = listener;
    }

    private FragmentChangeManager mFragmentChangeManager;

    public void setTextsize(float textsize) {
        updateTabStyles();
    }

    public void setTextSelectColor(int textSelectColor) {
        this.mTextSelectColor = textSelectColor;
        updateTabStyles();
    }

    public void setTextUnselectColor(int textUnselectColor) {
        this.mTextUnselectColor = textUnselectColor;
        updateTabStyles();
    }

    private boolean mTabSpaceEqual = true;

    public void setTabSpaceEqual(boolean tabSpaceEqual) {
        this.mTabSpaceEqual = tabSpaceEqual;
        updateTabStyles();
    }


    /**
     * 创建并添加tab
     */
    private void addTab(final int position, View tabView) {
        View tabbgView = tabView.findViewById(R.id.ll_tab_item);
        LinearLayout tip_ll = (LinearLayout) tabView.findViewById(R.id.tip_ll);
        tip_ll.setVisibility(GONE);
        TextView tv_tab_title = (TextView) tabbgView.findViewById(R.id.tv_tab_title);
        tv_tab_title.setText(mTabEntitys.get(position).getTabTitle());

        Log.e("AileTabView", "position:" + position + "=" + mTabEntitys.get(position).getTabTitle());
        ImageView iv_tab_icon = (ImageView) tabbgView.findViewById(R.id.iv_tab_icon);
        iv_tab_icon.setImageResource(mTabEntitys.get(position).getTabUnselectedIcon());

        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                if (mCurrentTab != position) {
                    setCurrentTab(position);
                    if (mListener != null) {
                        mListener.onTabSelect(position);
                    }
                } else {
                    if (mListener != null) {
                        mListener.onTabReselect(position);
                    }
                }
            }
        });

        /** 每一个Tab的布局参数 */
        LinearLayout.LayoutParams lp_tab = mTabSpaceEqual ?
                new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f) :
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        mTabsContainer.addView(tabView, position, lp_tab);
    }

    //setter and getter
    public void setCurrentTab(int currentTab) {
        mLastTab = this.mCurrentTab;
        this.mCurrentTab = currentTab;
        updateTabSelection(currentTab);
        if (mFragmentChangeManager != null) {
            mFragmentChangeManager.setFragments(currentTab);
        }
        invalidate();
    }

    public void updateTip(int position, boolean isTip, String num) {
        View tabView = mTabsContainer.getChildAt(position);
        View tabbgView = tabView.findViewById(R.id.tip_ll);
        TextView textView = (TextView) tabbgView.findViewById(R.id.tip_tv);
        if (isTip) {
            textView.setText(num);
            tabbgView.setVisibility(VISIBLE);
        } else {
            tabbgView.setVisibility(GONE);
        }
    }


    private void updateTabSelection(int position) {
        for (int i = 0; i < mTabCount; ++i) {
            View tabView = mTabsContainer.getChildAt(i);
            CustomTabEntity tabEntity = mTabEntitys.get(i);
            View tabbgView = tabView.findViewById(R.id.ll_tab_item);
            final boolean isSelect = i == position;
            //设置文字
            TextView tab_title = (TextView) tabbgView.findViewById(R.id.tv_tab_title);
            tab_title.setTextColor(isSelect ? mTextSelectColor : mTextUnselectColor);

            //设icon
            ImageView iv_tab_icon = (ImageView) tabbgView.findViewById(R.id.iv_tab_icon);
            iv_tab_icon.setImageResource(isSelect ? tabEntity.getTabSelectedIcon() : tabEntity.getTabUnselectedIcon());

//            tabbgView.setBackgroundResource(isSelected ? R.mipmap.bg_tab_item : android.R.color.transparent);
        }
    }

    private void updateTabStyles() {
        for (int i = 0; i < mTabCount; i++) {
            View tabView = mTabsContainer.getChildAt(i);
            View tabbgView = tabView.findViewById(R.id.ll_tab_item);
            TextView tv_tab_title = (TextView) tabbgView.findViewById(R.id.tv_tab_title);
            tv_tab_title.setTextColor(i == mCurrentTab ? mTextSelectColor : mTextUnselectColor);
//            tv_tab_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextsize);

            ImageView iv_tab_icon = (ImageView) tabbgView.findViewById(R.id.iv_tab_icon);
            iv_tab_icon.setVisibility(View.VISIBLE);
            CustomTabEntity tabEntity = mTabEntitys.get(i);
            iv_tab_icon.setImageResource(i == mCurrentTab ? tabEntity.getTabSelectedIcon() : tabEntity.getTabUnselectedIcon());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            lp.bottomMargin = (int) mIconMargin;

            iv_tab_icon.setLayoutParams(lp);
        }
    }

    protected int sp2px(float sp) {
        final float scale = this.mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }


}
