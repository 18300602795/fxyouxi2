package com.etsdk.app.huov7.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.RelativeLayout;

import com.etsdk.app.huov7.R;
import com.liang530.log.L;

/**
 * Created by liu hong liang on 2017/1/6.
 * 下拉缩放view
 *
 *  titleview 顶部，   固定不动  高度titleHeight=30dp
 *  zoomview   缩放view 原始高度zoomOriginHeight=200dp
 *  view      遮罩层    固定高度 200dp
 *  headview  头部      固定高度400dp             状态：滑动中，完全展开，完全收缩
 *  recyclerview
 *
 *  recyclerview协同滑动：
 *      dy>0上滑:
 *          headview滑动中状态，拦截事件，
 *
 *
 *
 */

public class PullZoomLLView extends RelativeLayout implements NestedScrollingParent {
    private static final String TAG = PullZoomLLView.class.getSimpleName();
    private final OverScroller mScroller;
    private int mHeadViewHeight;
    private Integer originHeight=null;
    private float followScrollY=0;
    private RecyclerView recyclerView;
    private OnRecyScrollListener onRecyScrollListener=new OnRecyScrollListener();
    private LinearLayout llHeadView;
    private ScrollLinearLayout llScrollView;
    private LinearLayout llTab;
    private int tabHeight;
    private RelativeLayout rlTitle;
    private int titleHeight;
    private int mHeadCanScrollHeight;

    public PullZoomLLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new OverScroller(context);
    }


    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.e(TAG, "onStartNestedScroll");
        if(child instanceof RecyclerView){
            recyclerView= (RecyclerView) child;
            recyclerView.addOnScrollListener(onRecyScrollListener);
        }
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        Log.e(TAG, "onNestedScrollAccepted");
    }

    @Override
    public void onStopNestedScroll(View target) {
        Log.e(TAG, "onStopNestedScroll");
       restoreOrFinish();
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.e(TAG, "onNestedScroll");
    }

    /**
     *
     * @param target
     * @param dx
     * @param dy 正数表示向上滑动，负数向下滑动
     * @param consumed
     *
     * canScrollVertically 1表示返回能否向上滑  -1表示能否向下滑,此函数在滑动中判断不好用，
     *
     */
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        //用如下方法判断不精准，因为这个是滑之前判断的，dy=45 而滑动20后recyclerview就滑不动了
        boolean childCanDownScroll=dy<0&&ViewCompat.canScrollVertically(target, -1);
        boolean childCanDownUp=dy>0&&ViewCompat.canScrollVertically(target, 1);
        if(dy<0){//下滑
            if(!childCanDownScroll){//recy已经是在顶部了，考虑拦截,
                llScrollView.scrollBy(0,dy);
            }
        }else{//上滑
            int scrollY = llScrollView.getScrollY();
            int canScrollY = mHeadCanScrollHeight - scrollY;
            L.e(TAG,"dy="+dy+"  scrollY="+scrollY+"  canScrollY="+canScrollY);
            if(scrollY>=0&&canScrollY>0){//headview还没到顶部，拦截事件
                if(dy>canScrollY){//headview消耗不完，剩下的给recy
                    llScrollView.scrollBy(0,canScrollY);
                    consumed[1]=canScrollY;
                    L.e(TAG,"拦截Y="+canScrollY);
                }else{
                    llScrollView.scrollBy(0,dy);
                    consumed[1]=dy;
                    L.e(TAG,"拦截Y="+dy);
                }
            }

        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.e(TAG, "onNestedFling");
        restoreOrFinish();
        return !consumed;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.e(TAG, "onNestedPreFling");
        if(llScrollView.getScrollY()>=mHeadCanScrollHeight) return false;
//        llScrollView.fling((int)velocityY,mHeadCanScrollHeight);
        return true;
    }

    @Override
    public int getNestedScrollAxes() {
        Log.e(TAG, "getNestedScrollAxes");
        return 0;
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        llHeadView = (LinearLayout) findViewById(R.id.ll_headView);
        llScrollView = (ScrollLinearLayout) findViewById(R.id.ll_scrollView);
        llTab = (LinearLayout) findViewById(R.id.ll_tab);
        rlTitle = (RelativeLayout) findViewById(R.id.rl_title);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(originHeight==null){//初始化
            mHeadViewHeight = llHeadView.getMeasuredHeight();
            originHeight=llScrollView.getMeasuredHeight();
            tabHeight =llTab.getMeasuredHeight();
            titleHeight =rlTitle.getMeasuredHeight();
            mHeadCanScrollHeight=mHeadViewHeight-titleHeight-tabHeight;
            llScrollView.init(this,mHeadCanScrollHeight);
        }
//        L.e(TAG,"originHeight="+originHeight);
//        L.e(TAG,"mHeadViewHeight="+mHeadViewHeight);
//        L.e(TAG,"tabHeight="+tabHeight);
//        L.e(TAG,"titleHeight="+titleHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 还原还是关闭
     */
    public void restoreOrFinish(){
        int scrollY = llScrollView.getScrollY();
        if(scrollY<0){
            if(-scrollY>=originHeight/4){
                ((Activity)getContext()).finish();
            }else{
                llScrollView.scrollBy(0,-scrollY);
            }
        }
    }

    public void changeLayoutParams(){
        int scrollY = llScrollView.getScrollY();
        //动态改变高度
        if(scrollY>0){
            getLayoutParams().height=originHeight+scrollY;
            requestLayout();
//            L.e(TAG,"scrollY="+scrollY+"   afterHeight="+getMeasuredHeight()+" destH="+(originHeight+scrollY));
        }
    }
    private class OnRecyScrollListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            L.e(TAG,"ivZoom 滑动了：dy="+dy);
        }
    }
}
