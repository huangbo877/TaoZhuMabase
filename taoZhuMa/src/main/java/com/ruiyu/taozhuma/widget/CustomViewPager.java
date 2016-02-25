package com.ruiyu.taozhuma.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {
	private boolean isScrollable = false;

	public CustomViewPager(Context context) {
		super(context);
	}

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isScrollable == false) {
			return false;
		} else {
			return super.onTouchEvent(event);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (isScrollable == false) {
			return false;
		} else {
			return super.onInterceptTouchEvent(event);
		}
	}

	// public void setPagingEnabled(boolean b) {
	// this.isPagingEnabled = b;
	// }
	//
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);

	}

}
