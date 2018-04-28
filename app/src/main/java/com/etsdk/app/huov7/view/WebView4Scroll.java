package com.etsdk.app.huov7.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class WebView4Scroll extends WebView {
    private SwipeRefreshLayout swipeRefreshLayout;

    public WebView4Scroll(Context context) {
        super(context);
    }

    public WebView4Scroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebView4Scroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout){
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(swipeRefreshLayout != null) {
            if (this.getScrollY() == 0) {
                swipeRefreshLayout.setEnabled(true);
            } else {
                swipeRefreshLayout.setEnabled(false);
            }
        }
    }
}
