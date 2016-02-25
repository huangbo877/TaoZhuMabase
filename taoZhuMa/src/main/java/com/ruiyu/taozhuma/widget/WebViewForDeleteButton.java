/**
 * 
 */
package com.ruiyu.taozhuma.widget;

import java.lang.reflect.Method;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.widget.ZoomButtonsController;

/**
 * @author 林尧 2015-12-17
 */
public class WebViewForDeleteButton extends WebView {
	// Webview内部的按钮控制对象
	private ZoomButtonsController zoomController = null;

	public WebViewForDeleteButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		disableZoomController();
	}

	public WebViewForDeleteButton(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		disableZoomController();
	}

	public WebViewForDeleteButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		disableZoomController();
	}

	// 使得控制按钮不可用
	private void disableZoomController() {
		// API version 大于11的时候，SDK提供了屏蔽缩放按钮的方法
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			this.getSettings().setBuiltInZoomControls(true);
			this.getSettings().setDisplayZoomControls(false);
		} else {
			// 如果是11- 的版本使用JAVA中的映射的办法
			getControlls();
		}
	}

	private void getControlls() {
		try {
			Class webview = Class.forName("android.webkit.WebView");
			Method method = webview.getMethod("getZoomButtonsController");
			zoomController = (ZoomButtonsController) method.invoke(this, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		super.onTouchEvent(ev);
		if (zoomController != null) {
			// 隐藏按钮
			// Hide the controlls AFTER they where made visible by the default
			// implementation.
			zoomController.setVisible(false);
		}
		return true;
	}
}