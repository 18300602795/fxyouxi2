package com.etsdk.app.huov7.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by liu hong liang on 2017/1/6.
 */

public class ZoomRecyclerView extends RecyclerView {
    private static final String TAG = ZoomRecyclerView.class.getSimpleName();

    public ZoomRecyclerView(Context context) {
        super(context);
    }

    public ZoomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ZoomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
//                L.e(TAG,"down x="+x+"  y="+y);
                break;
            case MotionEvent.ACTION_MOVE:
//                L.e(TAG,"move x="+x+"  y="+y);
                break;
            case MotionEvent.ACTION_UP:
//                L.e(TAG,"up x="+x+"  y="+y);
                break;
        }
        return super.onTouchEvent(event);
    }
}
