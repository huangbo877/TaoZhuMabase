package com.ruiyu.taozhuma.widget;

import javax.crypto.Mac;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * @author 底部监听的scrollview
 * 
 */
public class BottomScrollView extends ScrollView {
	private OnScrollToBottomListener onScrollToBottom;
	PointF curP = new PointF();

	public BottomScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BottomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BottomScrollView(Context context) {
		super(context);
	}

	/**
	 * 方法名: setOnScrollToBottomLintener
	 * 
	 * 功能描述:设置监听
	 * 
	 * @param listener
	 * @param layout
	 *            ScrollView包含的Layout
	 * @return void
	 * 
	 */
	public void setOnScrollToBottomLintener(OnScrollToBottomListener listener,
			LinearLayout layout) {
		this.layout = layout;
		onScrollToBottom = listener;
	}

	public boolean isBottom() {

		if (getScrollY() + getHeight() >= computeVerticalScrollRange()) {
			return true;
		} else {
			return false;
		}
	}

	public interface OnScrollToBottomListener {

		// 手指离开了屏幕
		public void FingerUpLinstener(boolean moveDistance);
	}

	private LinearLayout layout;
	private int moveDistance = 0;// 移动的距离

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:// 手指按下
			curP.y = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:// 手指移动
			break;
		case MotionEvent.ACTION_UP:// 手指离开

			float lastY = ev.getY();
			if (curP.y - lastY > 100 && isBottom()) {
				onScrollToBottom.FingerUpLinstener(true);
			} else {
				onScrollToBottom.FingerUpLinstener(false);
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
}