/*
Copyright 2015 shizhefei（LuckyJayce）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.etsdk.hlrefresh.imp;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.etsdk.app.huov7.R;
import com.etsdk.hlrefresh.ILoadViewFactory;
import com.etsdk.hlrefresh.vary.VaryViewHelper;


public class HsLoadViewFactory implements ILoadViewFactory {
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	@Override
	public ILoadMoreView madeLoadMoreView() {
		return new LoadMoreHelper();
	}

	@Override
	public ILoadView madeLoadView() {
		return new LoadViewHelper();
	}

	private class LoadMoreHelper implements ILoadMoreView {

		protected TextView footView;
		protected OnClickListener onClickRefreshListener;
		@Override
		public void init(FootViewAdder footViewHolder, OnClickListener onClickRefreshListener) {
			View contentView = footViewHolder.getContentView();

			Context context = contentView.getContext();
			TextView textView = new TextView(context);
			textView.setTextColor(Color.GRAY);
			textView.setPadding(0, dip2px(context, 16), 0, dip2px(context, 16));
			textView.setGravity(Gravity.CENTER);
			footViewHolder.addFootView(textView);
			footView = textView;
			this.onClickRefreshListener = onClickRefreshListener;
			showNormal();
		}

		@Override
		public void showNormal() {

			LayoutParams layoutParams = footView.getLayoutParams();
			if(layoutParams!=null){
				layoutParams.height= LayoutParams.WRAP_CONTENT;
			}else{
				layoutParams=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
				footView.setLayoutParams(layoutParams);
			}

			footView.setText("点击加载更多");
			footView.setOnClickListener(onClickRefreshListener);
		}

		@Override
		public void showLoading() {

			LayoutParams layoutParams = footView.getLayoutParams();
			if(layoutParams!=null){
				layoutParams.height= LayoutParams.WRAP_CONTENT;
			}else{
				layoutParams=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
				footView.setLayoutParams(layoutParams);
			}
			footView.setText("正在加载中..");
			footView.setOnClickListener(null);
		}

		@Override
		public void showFail(Exception exception) {

			LayoutParams layoutParams = footView.getLayoutParams();
			if(layoutParams!=null){
				layoutParams.height= LayoutParams.WRAP_CONTENT;
			}else{
				layoutParams=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
				footView.setLayoutParams(layoutParams);
			}
			footView.setText("加载失败，点击重新加载");
			footView.setOnClickListener(onClickRefreshListener);
		}

		@Override
		public void setCanLoadMore(boolean canLoadMore) {
			if(!canLoadMore&&footView!=null){
					LayoutParams layoutParams = footView.getLayoutParams();
					if(layoutParams==null){
						layoutParams=new LayoutParams(0,0);
						footView.setLayoutParams(layoutParams);
					}else{
						layoutParams.height=0;
						layoutParams.width=0;
					}
				}
		}
		@Override
		public void showNomore() {
			LayoutParams layoutParams = footView.getLayoutParams();
			if(layoutParams!=null){
				layoutParams.height=1;
			}else{
				layoutParams=new LayoutParams(LayoutParams.MATCH_PARENT,1);
				footView.setLayoutParams(layoutParams);
			}
			footView.setText("已经加载完毕");
			footView.setOnClickListener(null);

		}
	}

	private class LoadViewHelper implements ILoadView {
		private VaryViewHelper helper;
		private OnClickListener onClickRefreshListener;
		private Context context;

		@Override
		public void init(View switchView, OnClickListener onClickRefreshListener) {
			this.context = switchView.getContext().getApplicationContext();
			this.onClickRefreshListener = onClickRefreshListener;
			helper = new VaryViewHelper(switchView);
		}

		@Override
		public void restore() {
			helper.restoreView();
		}

		@Override
		public void showLoading() {
			Context context = helper.getContext();

			LinearLayout layout = new LinearLayout(context);
			layout.setOrientation(LinearLayout.VERTICAL);
			layout.setGravity(Gravity.CENTER);
			ProgressBar progressBar = new ProgressBar(context,null,android.R.attr.progressBarStyle);
			layout.addView(progressBar);

			TextView textView = new TextView(context);
			textView.setText("加载中...");
			textView.setGravity(Gravity.CENTER);
			layout.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			int top = dip2px(context, 12);
			params.setMargins(0, top, 0, 0);
			layout.addView(textView,params);
			LayoutParams layoutParams=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			helper.showLayout(layout,layoutParams);
		}

		@Override
		public void tipFail(Exception exception) {
			Toast.makeText(context, "网络加载失败", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void showFail(Exception exception) {
			Context context = helper.getContext();

			LinearLayout layout = new LinearLayout(context);
			layout.setOrientation(LinearLayout.VERTICAL);
			layout.setGravity(Gravity.CENTER);

			TextView textView = new TextView(context);
			textView.setText("网络加载失败");
			textView.setGravity(Gravity.CENTER);
			layout.setGravity(Gravity.CENTER);
			layout.addView(textView);
			ImageView imageView=new ImageView(context);
			imageView.setImageResource(R.mipmap.bga_refresh_loading);
			imageView.setOnClickListener(onClickRefreshListener);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			int top = dip2px(context, 12);
			params.setMargins(0, top, 0, 0);
			layout.addView(imageView,params);
			LayoutParams layoutParams=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			helper.showLayout(layout,layoutParams);
		}

		@Override
		public void showEmpty() {
			Context context = helper.getContext();

			LinearLayout layout = new LinearLayout(context);
			layout.setOrientation(LinearLayout.VERTICAL);
			layout.setGravity(Gravity.CENTER);

			TextView textView = new TextView(context);
			textView.setText("暂无数据");
			textView.setGravity(Gravity.CENTER);
			layout.addView(textView);

			ImageView imageView=new ImageView(context);
			imageView.setImageResource(R.mipmap.bga_refresh_loading);
			imageView.setOnClickListener(onClickRefreshListener);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			int top = dip2px(context, 12);
			params.setMargins(0, top, 0, 0);
			layout.addView(imageView,params);
			LayoutParams layoutParams=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			helper.showLayout(layout,layoutParams);
		}
	}
}
