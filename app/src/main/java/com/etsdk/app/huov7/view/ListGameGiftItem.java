package com.etsdk.app.huov7.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.etsdk.app.huov7.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/12/6.
 */

public class ListGameGiftItem extends FrameLayout {
    @BindView(R.id.v_line)
    View vLine;

    public ListGameGiftItem(Context context) {
        super(context);
        initUI();
    }

    public ListGameGiftItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public ListGameGiftItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    private void initUI() {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        setLayoutParams(layoutParams);
        LayoutInflater.from(getContext()).inflate(R.layout.adapter_game_gift_list_item, this, true);
        ButterKnife.bind(this);
    }
    public void showLine(boolean showLine) {
        if (showLine) {
            vLine.setVisibility(VISIBLE);
        } else {
            vLine.setVisibility(GONE);
        }
    }
}
