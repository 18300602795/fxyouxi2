package com.etsdk.hlrefresh.viewhandler;

import android.view.View;
import android.view.View.OnClickListener;

import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.ILoadViewFactory;


public interface ViewHandler {

	/**
	 *
	 * @param contentView
	 * @param loadMoreView
	 * @param onClickLoadMoreListener
     * @return 是否有 init ILoadMoreView
     */
	public boolean handleSetAdapter(View contentView, Object Adapter, ILoadViewFactory.ILoadMoreView loadMoreView, OnClickListener onClickLoadMoreListener);


	public void setOnScrollBottomListener(View contentView, Integer autoLastLoadMorePosition, BaseRefreshLayout.OnScrollBottomListener onScrollBottomListener);
}
