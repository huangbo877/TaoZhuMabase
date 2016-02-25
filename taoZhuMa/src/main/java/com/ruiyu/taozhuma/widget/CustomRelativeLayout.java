package com.ruiyu.taozhuma.widget;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class CustomRelativeLayout extends LinearLayout {
	PointF downP = new PointF();
	/** 触摸时当前的点 **/
	PointF curP = new PointF();

	public CustomRelativeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public CustomRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CustomRelativeLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			Log.i("CustomRelativeLayout", "dispatchTouchEvent--ACTION_DOWN");
//			break;
//		case MotionEvent.ACTION_MOVE:
//			Log.i("CustomRelativeLayout", "dispatchTouchEvent--ACTION_MOVE");
//			break;
//		case MotionEvent.ACTION_UP:
//			Log.i("CustomRelativeLayout", "dispatchTouchEvent--ACTION_UP");
//			break;
//		}

		return super.dispatchTouchEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// if(event.getAction()==MotionEvent.ACTION_DOWN){
		// curP.x = event.getX();
		// curP.y = event.getY();
		// // getParent().requestDisallowInterceptTouchEvent(false);
		// }
		// if(event.getAction()==MotionEvent.ACTION_UP){
		// float lastY = event.getY();
		// float lastX = event.getX();
		// if(Math.abs(curP.x - lastX)<10&&Math.abs(curP.y - lastY)<10){
		// return super.onTouchEvent(event);
		// }else{
		//
		// return ((View) getParent()).onTouchEvent(event);
		// }
		//
		// }
//		if(event.getAction()==MotionEvent.ACTION_UP){
//			getParent().requestDisallowInterceptTouchEvent(true);
//		}else{
//			getParent().requestDisallowInterceptTouchEvent(false);
//			
//		}
		return super.onTouchEvent(event);
	}
}
