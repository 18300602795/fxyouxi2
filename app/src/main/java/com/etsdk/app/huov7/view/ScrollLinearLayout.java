package com.etsdk.app.huov7.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.etsdk.app.huov7.R;

public class ScrollLinearLayout extends LinearLayout {
    private OverScroller mScroller;
    private int maxScrollHeight;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mMaximumVelocity, mMinimumVelocity;
    private PullZoomLLView pullZoomLLView;
    private RecyclerView recyclerview;
    private float mLastY;
    private boolean mDragging;
    public ScrollLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new OverScroller(context);
    }

    public int getMaxScrollHeight() {
        return maxScrollHeight;
    }

    public void init(PullZoomLLView pullZoomLLView,int maxScrollHeight) {
        this.maxScrollHeight = maxScrollHeight;
        this.pullZoomLLView=pullZoomLLView;
        recyclerview= (RecyclerView) findViewById(R.id.recyclerview);
    }

    public void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, maxScrollHeight);
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y) {
        //判断最大滑动距离
        if(y>maxScrollHeight){
            y=maxScrollHeight;
        }
        super.scrollTo(x, y);
        pullZoomLLView.changeLayoutParams();
    }

    //        @Override
//        public void scrollTo(int x, int y) {
//            if (y < 0) {
//                y = 0;
//            }
//            if (y > maxScrollHeight) {
//                y = maxScrollHeight;
//            }
//            if (y != getScrollY()) {
//                super.scrollTo(x, y);
//            }
//        }
    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
    private boolean isRecyclerTopShow(){
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerview.getLayoutManager();
        int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
        return firstCompletelyVisibleItemPosition==0;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(event);
        int action = event.getAction();
        float y = event.getY();
        if(!isRecyclerTopShow()){//recyclerview不在顶部时不允许滑动
            return super.onTouchEvent(event);
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished())
                    mScroller.abortAnimation();
                mLastY = y;
                return true;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;

                if (!mDragging && Math.abs(dy) > mTouchSlop) {
                    mDragging = true;
                }
                if (mDragging) {
                    scrollBy(0, (int) -dy);
                }

                mLastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
                mDragging = false;
                recycleVelocityTracker();
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_UP:
                mDragging = false;
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int velocityY = (int) mVelocityTracker.getYVelocity();
                if (Math.abs(velocityY) > mMinimumVelocity) {
                    fling(-velocityY);
                }
                recycleVelocityTracker();
                pullZoomLLView.restoreOrFinish();
                break;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }
}