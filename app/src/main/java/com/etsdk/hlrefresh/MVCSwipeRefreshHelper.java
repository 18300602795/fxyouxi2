package com.etsdk.hlrefresh;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.etsdk.app.huov7.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 注意 ：<br>
 * SwipeRefreshLayout必须有Parent
 * 
 * @author zsy
 *
 */
public class MVCSwipeRefreshHelper extends BaseRefreshLayout {
	public MVCSwipeRefreshHelper(SwipeRefreshLayout swipeRefreshLayout) {
		super(new RefreshView(swipeRefreshLayout));
	}
	public MVCSwipeRefreshHelper(SwipeRefreshLayout swipeRefreshLayout, ILoadViewFactory.ILoadView loadView, ILoadViewFactory.ILoadMoreView loadMoreView) {
		super(new RefreshView(swipeRefreshLayout), loadView, loadMoreView);
	}

	private static class RefreshView implements IRefreshView {
		private SwipeRefreshLayout swipeRefreshLayout;
		private View mTarget;

		public RefreshView(SwipeRefreshLayout swipeRefreshLayout) {
			this.swipeRefreshLayout = swipeRefreshLayout;
			//设置颜色
			swipeRefreshLayout.setColorSchemeResources(R.color.bg_blue,
					android.R.color.holo_green_light, android.R.color.holo_orange_light,
					android.R.color.holo_red_light);
			if (swipeRefreshLayout.getParent() == null) {
				throw new RuntimeException("PtrClassicFrameLayout 必须有Parent");
			}
			try {
				Method method = swipeRefreshLayout.getClass().getDeclaredMethod("ensureTarget");
				method.setAccessible(true);
				method.invoke(swipeRefreshLayout);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Field field = swipeRefreshLayout.getClass().getDeclaredField("mTarget");
				field.setAccessible(true);
				mTarget = (View) field.get(swipeRefreshLayout);
			} catch (Exception e) {
				e.printStackTrace();
			}
			swipeRefreshLayout.setOnRefreshListener(listener);
		}

		@Override
		public View getContentView() {
			return mTarget;
		}

		@Override
		public View getSwitchView() {
			return swipeRefreshLayout;
		}

		private OnRefreshListener onRefreshListener;

		@Override
		public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
			this.onRefreshListener = onRefreshListener;
		}

		@Override
		public void showRefreshComplete() {
			swipeRefreshLayout.setRefreshing(false);
		}

		@Override
		public void showRefreshing() {
			swipeRefreshLayout.setRefreshing(true);
		}

		private SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {

			@Override
			public void onRefresh() {
				if (onRefreshListener != null) {
					onRefreshListener.onRefresh();
				}
			}
		};

	}
}
