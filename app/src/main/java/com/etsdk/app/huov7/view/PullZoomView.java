package com.etsdk.app.huov7.view;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

public class PullZoomView extends RelativeLayout implements NestedScrollingParent {
    private static final String TAG = PullZoomView.class.getSimpleName();
    private ImageView ivZoom;
    private int mTopViewHeight;
    private int originHeight=0;
    private float followScrollY=0;
    private RecyclerView recyclerView;
    private OnRecyScrollListener onRecyScrollListener=new OnRecyScrollListener();
    public PullZoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ivZoom = (ImageView) findViewById(R.id.iv_zoom);
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
    public void onStopNestedScroll(View target)
    {
        Log.e(TAG, "onStopNestedScroll");
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

//        boolean scrollVerticalDown = ViewCompat.canScrollVertically(target, -1);//正数表示下滑，负数表示上滑
//        Log.e(TAG, "onNestedPreScroll dx="+dx+"  dy="+dy+ "scrollVerticalDown="+scrollVerticalDown);
//        if(dy<0&&!scrollVerticalDown){
//            scrollBy(0,dy);
//            consumed[1]=dy;
//
//        }
//        L.e(TAG,"ivZoom y="+ivZoom.getY());
        //向上
        boolean hiddenTop = dy > 0 && ivZoom.getScrollY() < mTopViewHeight;
        boolean showTop = dy < 0 && ivZoom.getScrollY() >= 0 && !ViewCompat.canScrollVertically(target, -1);
        //用如下方法判断不精准，因为这个是滑之前判断的，dy=45 而滑动20后recyclerview就滑不动了
        boolean childCanDownScroll=dy<0&&ViewCompat.canScrollVertically(target, -1);
        boolean childCanDownUp=dy>0&&ViewCompat.canScrollVertically(target, 1);
        L.e(TAG,"childCanDownScroll="+childCanDownScroll);
        L.e(TAG,"childCanDownUp="+childCanDownUp);
        if(childCanDownScroll||childCanDownUp){
//            ivZoom.setY();
//            L.e(TAG,"ivZoom 滑动了："+dy);
        }

    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.e(TAG, "onNestedFling");
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.e(TAG, "onNestedPreFling");
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        Log.e(TAG, "getNestedScrollAxes");
        return 0;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = ivZoom.getMeasuredHeight();
        originHeight=getMeasuredHeight();
        L.e(TAG,"originHeight="+originHeight);
    }
    private class OnRecyScrollListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            followScrollY+=dy;
            ivZoom.setY(followScrollY);
            L.e(TAG,"ivZoom 滑动了："+followScrollY+" dy="+dy);
        }
    }

}
