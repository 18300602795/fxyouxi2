package com.etsdk.hlrefresh;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by liu hong liang on 2017/1/21.
 */

public class HSwiperefreshLayout extends SwipeRefreshLayout {
    public HSwiperefreshLayout(Context context) {
        super(context);
    }

    public HSwiperefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    private int mLastXIntercept =0;
    private int mLastYIntercept = 0;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastXIntercept=x;
                mLastYIntercept=y;
                intercepted= false;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx =  mLastXIntercept-x;
                int dy =  mLastYIntercept-y;
                if (Math.abs(dx)<Math.abs(dy)) {//竖向滑动
                    intercepted=true;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercepted= false;
                break;
        }
        return intercepted;
    }
}
