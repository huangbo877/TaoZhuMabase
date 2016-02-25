package com.ruiyu.taozhuma.widget;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;

public class CustomWebView extends WebView {
	private Context context;
	private boolean isScroll = true;// webview 是否滚动

	PointF downP = new PointF();
	PointF curP = new PointF();
	boolean status = false;
	private String orientation;
	boolean istop;
	
	public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomWebView(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// 当拦截触摸事件到达此位置的时候，返回true，
		// 说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent
		// 和android的触屏事件由上至下一层一层传播有关
		return false;
	}

	private boolean isBottom() {
		// WebView的总高度
		float contentHeight = getContentHeight() * getScale();
		// WebView的现高度
		float currentHeight = getHeight() + getScrollY();
		// 之间的差距小于2便认为滑动到底部
		return contentHeight - currentHeight < 1;
	}

	public boolean isTop() {
		// 当ScrollY为0是到达顶部
		return getScrollY() == 0;
	}

//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		System.out.println("CustomWebView  "+"dispatchTouchEvent");
//		if(ev.getAction()==MotionEvent.ACTION_DOWN){
//			curP.x = ev.getX();
//			curP.y = ev.getY();
//		}
//		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
//			float lastY = ev.getY(ev.getPointerCount() - 1);
//			float lastX = ev.getX(ev.getPointerCount() - 1);
//			if (isBottom()) {// 如果到达底部，先设置为不能滚动
//				return true;
//			}
//			// 如果到达底部，但开始向上滚动，那么webview可以滚动
//			if (isBottom() && (curP.y - lastY < 0)) {
//				return false;
//			}
//			if (isTop()) {// 滑到顶部不能再滑
//				return true;
//			}
//			if (isTop() && (curP.y - lastY > 0)) {// 滑动到顶部，向下滑，可以滑到
//				return false;
//			}
//		}
//
//		return super.dispatchTouchEvent(ev);
//	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// webview被点击到，即可滑动
			isScroll = true;
			curP.x = event.getX();
			curP.y = event.getY();
			// 通知父控件现在进行的是本控件的操作，不要对我的操作进行干扰
			getParent().requestDisallowInterceptTouchEvent(true);
			break;

		case MotionEvent.ACTION_MOVE:
			float lastY = event.getY(event.getPointerCount() - 1);
			float lastX = event.getX(event.getPointerCount() - 1);
			if(istop){
				if (!status) {
					// 先判断横向滑动
					if (Math.abs(curP.x - lastX) > 50) {
						if (!status) {
							status = true;
							orientation = "横向";
						}
					}
					
					// 处理竖向滑动
					if (Math.abs(curP.y - lastY) > 50) {
						if (!status) {
							status = true;
							orientation = "竖向";
						}
					}
				} else {
					if (orientation.equals("横向")) {
						Log.i("****", "****横向滑动...");
						requestDisallowInterceptTouchEvent(true);
					} else if (orientation.equals("竖向")) {
						Log.i("****", "****竖向滑动...");
						if (isBottom()) {// 如果到达底部，先设置为不能滚动
							isScroll = false;
						}
						// 如果到达底部，但开始向上滚动，那么webview可以滚动
						if (isBottom() && (curP.y - lastY < 0)) {
							isScroll = true;
						}
						if (isTop()) {// 滑到顶部不能再滑
							isScroll = false;
						}
						if (isTop() && (curP.y - lastY > 0)) {// 滑动到顶部，向下滑，可以滑到
							isScroll = true;
						}
						getParent().requestDisallowInterceptTouchEvent(isScroll);
					}
				}
				
			}else{
				getParent().requestDisallowInterceptTouchEvent(false);
			}

			break;
		case MotionEvent.ACTION_UP:
			orientation = null;
			status = false;
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		}
		requestDisallowInterceptTouchEvent(true);
		return super.onTouchEvent(event);
	}
	
	public void setIsTop(boolean istop) {
		this.istop=istop;
	}
	
}
