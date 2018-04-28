package com.etsdk.hlrefresh.viewhandler;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.ILoadViewFactory;
import com.etsdk.hlrefresh.recyclerview.HFAdapter;
import com.etsdk.hlrefresh.recyclerview.HFRecyclerAdapter;


public class RecyclerViewHandler implements ViewHandler {

    @Override
    public boolean handleSetAdapter(View contentView, Object adapter, ILoadViewFactory.ILoadMoreView loadMoreView, OnClickListener onClickLoadMoreListener) {
        final RecyclerView recyclerView = (RecyclerView) contentView;
        boolean hasInit = false;
        Adapter srcAdapter= (Adapter) adapter;
        if (loadMoreView != null) {
            HFAdapter hfAdapter;
            if (srcAdapter instanceof HFAdapter) {
                hfAdapter = (HFAdapter) adapter;
            } else {
                hfAdapter = new HFRecyclerAdapter(srcAdapter);
            }
            srcAdapter=hfAdapter;
            loadMoreView.init(new RecyclerViewFootViewAdder(recyclerView, hfAdapter), onClickLoadMoreListener);
            hasInit = true;
        }
        if(recyclerView.getLayoutManager() instanceof GridLayoutManager){
            final GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            final int spanCount = layoutManager.getSpanCount();
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = layoutManager.getSpanSizeLookup();
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {

                    int itemViewType = recyclerView.getAdapter().getItemViewType(position);
                    if(itemViewType == HFAdapter.TYPE_FOOTER){
                        return spanCount;
                    }else if(itemViewType== HFAdapter.TYPE_HEADER){
                        return spanCount;
                    }else{

                        int index=position+(recyclerView.getAdapter().getItemViewType(0)== HFAdapter.TYPE_HEADER?1:0);
                        return spanSizeLookup.getSpanSize(index);
                    }
                }
            });
        }
        recyclerView.setAdapter(srcAdapter);
        return hasInit;
    }


    @Override
    public void setOnScrollBottomListener(View contentView, Integer autoLastLoadMorePosition, BaseRefreshLayout.OnScrollBottomListener onScrollBottomListener) {
        final RecyclerView recyclerView = (RecyclerView) contentView;
        Adapter adapter = recyclerView.getAdapter();
        if(adapter instanceof HFRecyclerAdapter){
            ((HFRecyclerAdapter) adapter).setAutoLoadMorePosition(autoLastLoadMorePosition,onScrollBottomListener);
        }
        RecyclerViewOnScrollListener listener = new RecyclerViewOnScrollListener(onScrollBottomListener);
        recyclerView.addOnScrollListener(listener);
        recyclerView.addOnItemTouchListener(listener);
    }

    /**
     * 滑动监听
     */
    private static class RecyclerViewOnScrollListener extends RecyclerView.OnScrollListener implements RecyclerView.OnItemTouchListener {
        private BaseRefreshLayout.OnScrollBottomListener onScrollBottomListener;

        public RecyclerViewOnScrollListener(BaseRefreshLayout.OnScrollBottomListener onScrollBottomListener) {
            super();
            this.onScrollBottomListener = onScrollBottomListener;
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (onScrollBottomListener != null) {
                    //(endY < startY) 如果放开的位置比按下去的位置大，说明手势向上移动。（之所以加这个判断是因为，之前列表数据较少的时候它已经在列表底部了，向下的刷新动作也可能触发）
                    //isScollBottom是否滚动到列表底部
                    if ((endY >= 0 && endY < startY) && isScollBottom(recyclerView)) {
                        onScrollBottomListener.onScorllBootom();
                    }
                }
            }
        }

        private boolean isScollBottom(RecyclerView recyclerView) {
            return !isCanScollVertically(recyclerView);
        }

        private boolean isCanScollVertically(RecyclerView recyclerView) {
            if (android.os.Build.VERSION.SDK_INT < 14) {
                return ViewCompat.canScrollVertically(recyclerView, 1) || recyclerView.getScrollY() < recyclerView.getHeight();
            } else {
                return ViewCompat.canScrollVertically(recyclerView, 1);
            }
        }


        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//            int lastVisibleItemPosition;
//            if(layoutManager instanceof LinearLayoutManager){
//                lastVisibleItemPosition= ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
//            }else if(layoutManager instanceof GridLayoutManager){
//                lastVisibleItemPosition=((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
//            }else if(layoutManager instanceof StaggeredGridLayoutManager){
//                int lastVisiblePostions[]=null;
//                lastVisiblePostions=((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastVisiblePostions);
//                for (int pos:lastVisiblePostions) {
//                    if(pos==)
//                }
//            }
//            int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    endY = -1;
                    startY = e.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    endY = e.getY();
                    break;
            }
            return false;
        }

        private float startY = -1f;
        private float endY = -1f;

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

    }

    private class RecyclerViewFootViewAdder implements ILoadViewFactory.FootViewAdder {
        private RecyclerView recyclerView;
        private HFAdapter hfAdapter;

        public RecyclerViewFootViewAdder(RecyclerView recyclerView, HFAdapter hfAdapter) {
            super();
            this.recyclerView = recyclerView;
            this.hfAdapter = hfAdapter;
        }

        @Override
        public View addFootView(int layoutId) {
            View view = LayoutInflater.from(recyclerView.getContext()).inflate(layoutId, recyclerView, false);
            return addFootView(view);
        }

        @Override
        public View addFootView(View view) {
            hfAdapter.addFooter(view);
            return view;
        }

        @Override
        public View getContentView() {
            return recyclerView;
        }

    }
}
