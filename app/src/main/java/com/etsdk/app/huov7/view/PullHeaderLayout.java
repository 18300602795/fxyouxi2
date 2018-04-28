package com.etsdk.app.huov7.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.etsdk.app.huov7.R;
import com.liang530.log.L;

public class PullHeaderLayout extends LinearLayout implements NestedScrollingParent ,NestedScrollingChild {

    private static final String TAG = PullHeaderLayout.class.getSimpleName();
    private NestedScrollingParentHelper mParentHelper;
    private ScrollerCompat mScroller;
    private VelocityTracker mVelocityTracker;
    private boolean resetH = false;
    private int tabLayoutHeight;
    private int headViewHeight;
    private int maxHeadScrollHeight;
    private Integer originLayoutHeight=null;//最开始的整个布局高度
    private View contentView;
    private ViewGroup rootView;//根布局
    private View gameDetailDownView;//底部下载view
    private OnScrollListener onScrollListener;
    public PullHeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mParentHelper = new NestedScrollingParentHelper(this);
        mScroller = ScrollerCompat.create(this.getContext());
        mVelocityTracker = VelocityTracker.obtain();
    }

    public OnScrollListener getOnScrollListener() {
        return onScrollListener;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }
    public void setRootView(ViewGroup rootView){
        this.rootView=rootView;
    }
    /**
     * 设置改变高度时需要同时改变的view
     * @param rootView
     * @param gameDetailDownView
     */
    public void setChangeHeightView(ViewGroup rootView,View gameDetailDownView){
        this.rootView=rootView;
        this.gameDetailDownView=gameDetailDownView;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(originLayoutHeight==null){
            originLayoutHeight=getMeasuredHeight();
        }
        tabLayoutHeight = findViewById(R.id.tablayout).getMeasuredHeight();
        headViewHeight = findViewById(R.id.ll_headView).getMeasuredHeight();
        maxHeadScrollHeight=headViewHeight-tabLayoutHeight;
        contentView = getChildAt(1);
    }
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        mParentHelper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        int scrollY = getScrollY();
        //上滑，顶部没有完全隐藏则拦截滑动，自己滑
        if (dy > 0 && scrollY < maxHeadScrollHeight ) {
            int consumedY = Math.min(dy, maxHeadScrollHeight - scrollY);
            consumed[1] = consumedY;
            scrollBy(0, consumedY);
            changeHeight();
        }
        //下滑，子view自己能滑，那么子view滑，不拦截
        else if(dy<0){
            boolean childCanDownScroll=ViewCompat.canScrollVertically(target, -1);
            if(!childCanDownScroll){//子view不能滑，自己滑
                consumed[1] = dy;
                scrollBy(0, dy);
            }
        }
        //只要自己不是完全展开就拦截，自己滑
//        else if (dy < 0 && scrollY == maxHeadScrollHeight) {
//            consumed[1] = dy;
//            scrollBy(0, dy);
//        } else if (dy < 0 && scrollY < maxHeadScrollHeight && scrollY > 0) {
//            int consumedY = Math.max(dy, -scrollY);
//            consumed[1] = consumedY;
//            scrollBy(0, consumedY);
//        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        L.e(TAG,"onNestedScroll");
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        int scrollY = getScrollY();
        if (velocityY > 0 && scrollY < maxHeadScrollHeight && scrollY > 0) {
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
            mScroller.fling(0, scrollY, (int)velocityX, (int)velocityY, 0, 0, 0, maxHeadScrollHeight);
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }
        return false;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        L.e(TAG,"onNestedFling");
        return false;
    }

    @Override
    public void onStopNestedScroll(View child) {
        mParentHelper.onStopNestedScroll(child);
        restoreOrFinish();
        L.e(TAG,"onStopNestedScroll");
    }

    @Override
    public int getNestedScrollAxes() {
        return mParentHelper.getNestedScrollAxes();
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
        if(onScrollListener!=null){
            onScrollListener.onScroll(x,y);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }
    private void changeHeight(){
        if (!resetH) {//因为整体布局上滑，导致下面最多会走光向上滑动的最大高度 ，所以需要补高一次
            resetH = true;
            //根布局默认是匹配父元素高度的，如果在此处改变PullHeaderLayout的高度，是不起作用的，被父布局限制了，
            // 而PullHeaderLayout是匹配父布局高度，所以此处改变根布局高度即可
            //由于底部下载view是位于布局底部，当高度加高后，底部就看不到了，所以需要增加marginbottom
            RelativeLayout.LayoutParams bottomLayoutParams = (RelativeLayout.LayoutParams) gameDetailDownView.getLayoutParams();
            bottomLayoutParams.bottomMargin=maxHeadScrollHeight;
            gameDetailDownView.setLayoutParams(bottomLayoutParams);
            rootView.getLayoutParams().height=rootView.getMeasuredHeight()+maxHeadScrollHeight;
            rootView.setLayoutParams(rootView.getLayoutParams());
        }
    }
    /**
     * 还原还是关闭
     */
    public void restoreOrFinish(){
        int scrollY = getScrollY();
        L.e(TAG,"scrollY="+scrollY+" originLayoutHeight="+originLayoutHeight+"  "+(originLayoutHeight*1/2));
        if(scrollY<0){
            if(-scrollY>=originLayoutHeight*1/2){
                ((Activity)getContext()).finish();
            }else{
                mScroller.startScroll(0, getScrollY(), 0, -scrollY,500);
                invalidate();
            }
        }
    }
    // 分别记录上次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;
    // 分别记录上次滑动的坐标(onInterceptTouchEvent)
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;
    private int downInterceptRealY=0;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercepted = false;
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                intercepted = false;
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                    intercepted = true;
                }
                downInterceptRealY=y+getScrollY();
                L.e(TAG,"intercept按下的是：x="+x+"  y="+y+" scrollY="+getScrollY());
                L.e(TAG,"intercept contentView x="+contentView.getX()+"  y="+contentView.getY());
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int dx =  mLastXIntercept-x;
                int dy =  mLastYIntercept-y;
                boolean childCanDownScroll=dy<0&&ViewCompat.canScrollVertically(contentView, -1);
                boolean childCanDownUp=dy>0&&ViewCompat.canScrollVertically(contentView, 1);
                int scrollY = getScrollY();
                if (Math.abs(dx)<Math.abs(dy)) {//竖向滑动
                    if(downInterceptRealY<contentView.getY()){//按下的起始点不是contentView，拦截
                        intercepted=true;
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                intercepted = false;
                break;
            }
            default:
                break;
        }

        Log.d(TAG, "intercepted=" + intercepted);
        mLastX = x;
        mLastY = y;
        mLastXIntercept = x;
        mLastYIntercept = y;
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int dy =  mLastY-y;
                L.e(TAG,"deltaY="+dy);
                int scrollY = getScrollY();
                if (dy > 0 && scrollY < maxHeadScrollHeight ) {
                    int consumedY = Math.min(dy, maxHeadScrollHeight - scrollY);
                    scrollBy(0, consumedY);
                    changeHeight();
                }else if(dy<0){
                    scrollBy(0, dy);
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                restoreOrFinish();
                break;
            }
            default:
                break;
        }

        mLastX = x;
        mLastY = y;
        return true;
    }
    public static interface OnScrollListener{
        void onScroll(int x, int y);
    }
}