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
package com.etsdk.hlrefresh.vary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

/**
 * 用于切换布局,用一个新的布局替换掉原先的布局
 * 
 * @author LuckyJayce
 *
 */
public class VaryViewHelper implements IVaryViewHelper {
	private View originalView;
	private ViewGroup parentView;
	private int viewIndex;
	private LayoutParams originalParams;
	private LayoutParams selfParams;
	private View currentView;

	public VaryViewHelper(View view) {
		super();
		this.originalView = view;
	}

	/**
	 * 
	 * @param view
	 * @param selfParams
	 *            替换view的时候用selfParams设置，originalView依然使用originalParams设置
	 */
	public VaryViewHelper(View view, LayoutParams selfParams) {
		super();
		this.originalView = view;
		this.selfParams = selfParams;
	}

	private void init() {
		originalParams = originalView.getLayoutParams();
		if (originalView.getParent() != null) {
			parentView = (ViewGroup) originalView.getParent();
		} else {
			parentView = (ViewGroup) originalView.getRootView().findViewById(android.R.id.content);
		}
		int count = parentView.getChildCount();
		for (int index = 0; index < count; index++) {
			if (originalView == parentView.getChildAt(index)) {
				viewIndex = index;
				break;
			}
		}
		currentView = originalView;
	}

	@Override
	public View getCurrentLayout() {
		return currentView;
	}

	@Override
	public void restoreView() {
		showLayout(originalView);
	}

	@Override
	public void showLayout(View view) {
		if (parentView == null) {
			init();
		}
		this.currentView = view;
		// 如果已经是那个view，那就不需要再进行替换操作了
		if (parentView.getChildAt(viewIndex) != view) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null) {
				parent.removeView(view);
			}
			parentView.getChildAt(viewIndex).clearAnimation();
			parentView.removeViewAt(viewIndex);
			if (view != originalView && selfParams != null) {
				parentView.addView(view, viewIndex, selfParams);
			} else {
				parentView.addView(view, viewIndex, originalParams);
			}
		}
	}
	public void showLayout(View view, LayoutParams selfParams) {
		if (parentView == null) {
			init();
		}
		this.currentView = view;
		// 如果已经是那个view，那就不需要再进行替换操作了
		if (parentView.getChildAt(viewIndex) != view) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null) {
				parent.removeView(view);
			}
			parentView.getChildAt(viewIndex).clearAnimation();
			parentView.removeViewAt(viewIndex);
			if (view != originalView && selfParams != null) {
				parentView.addView(view, viewIndex, selfParams);
			} else {
				parentView.addView(view, viewIndex, originalParams);
			}
		}
	}
	@Override
	public void showLayout(int layoutId) {
		showLayout(inflate(layoutId));
	}

	@Override
	public View inflate(int layoutId) {
		return LayoutInflater.from(originalView.getContext()).inflate(layoutId, null);
	}

	@Override
	public Context getContext() {
		return originalView.getContext();
	}

	@Override
	public View getView() {
		return originalView;
	}
}
