package com.etsdk.hlrefresh.recyclerview;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

import com.etsdk.hlrefresh.BaseRefreshLayout;


public class HFRecyclerAdapter extends HFAdapter {

	private Adapter adapter;
	private Integer autoLastLoadMorePosition =null;
	private BaseRefreshLayout.OnScrollBottomListener onScrollBottomListener;
	public HFRecyclerAdapter(Adapter adapter) {
		super();
		this.adapter = adapter;
		adapter.registerAdapterDataObserver(adapterDataObserver);
	}

	private AdapterDataObserver adapterDataObserver = new AdapterDataObserver() {

		public void onChanged() {
			HFRecyclerAdapter.this.notifyDataSetChanged();
		}

		public void onItemRangeChanged(int positionStart, int itemCount) {
			HFRecyclerAdapter.this.notifyItemRangeChanged(positionStart + getHeadSize(), itemCount);
		}

		public void onItemRangeInserted(int positionStart, int itemCount) {
			HFRecyclerAdapter.this.notifyItemRangeInserted(positionStart + getHeadSize(), itemCount);
		}

		public void onItemRangeRemoved(int positionStart, int itemCount) {
			HFRecyclerAdapter.this.notifyItemRangeRemoved(positionStart + getHeadSize(), itemCount);
		}

		public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
			HFRecyclerAdapter.this.notifyItemMoved(fromPosition + getHeadSize(), toPosition + getHeadSize());
		}

	};

	@Override
	public ViewHolder onCreateViewHolderHF(ViewGroup viewGroup, int type) {
		return adapter.onCreateViewHolder(viewGroup, type);
	}

	@Override
	public void onBindViewHolderHF(ViewHolder vh, int position) {
		adapter.onBindViewHolder(vh, position);
		int loadMorePos = adapter.getItemCount() - autoLastLoadMorePosition;
		//到底部倒数autoLastLoadMorePosition位置时自动加载更多
		if(autoLastLoadMorePosition !=null&&position==loadMorePos &&onScrollBottomListener!=null){
			onScrollBottomListener.onScorllBootom();
		}
	}
	public void setAutoLoadMorePosition(Integer autoLastLoadMorePosition, BaseRefreshLayout.OnScrollBottomListener onScrollBottomListener){
		this.autoLastLoadMorePosition =autoLastLoadMorePosition;
		this.onScrollBottomListener=onScrollBottomListener;
	}

	@Override
	public int getItemCountHF() {
		return adapter.getItemCount();
	}

	@Override
	public int getItemViewTypeHF(int position) {
		return adapter.getItemViewType(position);
	}

	@Override
	public long getItemIdHF(int position) {
		return adapter.getItemId(position);
	}

}
