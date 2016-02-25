package com.ruiyu.taozhuma.widget;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class CustomViewPager2 extends ViewPager {
	private boolean isPagingEnabled = false;

	
	private float x=-1,mx=-1;
	private float y=-1,my=-1;
	
	private final static int moveDistance = 50;	//移动距离(按像素来算)
	
	/**
	 * 状态用于检测滑动是否符合规定距离，如果符合状态则为规定状态的滑动，方向相当于锁死，不再计算是横向滑动还是竖向滑动
	 */
	private boolean status = false;
	/**
	 * 方向，用于存储方向，false为横，true为竖
	 */
	private Direction touchDirection;
	
	private enum Direction{
		横向,竖向;
	}
	public CustomViewPager2(Context context) {
		super(context);
	}

	public CustomViewPager2(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			actionDown(event);
//			break;
//		case MotionEvent.ACTION_MOVE:
//			actionMove(event);
//			break;
//		case MotionEvent.ACTION_UP:
//			actionUp(event);
//			break;
//		}
//		
//		return super.onTouchEvent(event);
//		
//	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		return super.onInterceptTouchEvent(event);
	}

	public void setPagingEnabled(boolean b) {
		this.isPagingEnabled = b;
	}

//	 @Override
//	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//	        int height = 0;
//	        for(int i = 0; i < getChildCount(); i++) {
//	            View child = getChildAt(i);
//	            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//	            int h = child.getMeasuredHeight();
//	            if(h > height) height = h;
//	        }
//
//	        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
//
//	        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//	    }
//	    
//	    private int measureHeight(int measureSpec, View view) {
//	        int result = 0;
//	        int specMode = MeasureSpec.getMode(measureSpec);
//	        int specSize = MeasureSpec.getSize(measureSpec);
//
//	        if (specMode == MeasureSpec.EXACTLY) {
//	            result = specSize;
//	        } else {
//	            // set the height from the base view if available
//	            if (view != null) {
//	                result = view.getMeasuredHeight();
//	            }
//	            if (specMode == MeasureSpec.AT_MOST) {
//	                result = Math.min(result, specSize);
//	            }
//	        }
//	        return result;
//	    }


	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}
	
	private void actionDown(MotionEvent event) {
		getParent().requestDisallowInterceptTouchEvent(true);
		x = event.getX();
		y = event.getY();
	}

	private void actionMove(MotionEvent event) {
		mx = event.getX();
		my = event.getY();

		// 先判断是否锁死方向
		if (!status) {
			// 先判断横向滑动
			if (Math.abs(x - mx) > moveDistance) {
				status = true;
				touchDirection = Direction.横向;
			}

			// 处理竖向滑动
			if (Math.abs(y - my) > moveDistance) {
				if (!status) {
					status = true;
					touchDirection = Direction.竖向;
				}
			}
		} else {
			if (touchDirection == Direction.横向) {
				Log.i("****", "****横向滑动...");
				getParent().requestDisallowInterceptTouchEvent(false);
			} else if (touchDirection == Direction.竖向) {
				Log.i("****", "****竖向滑动...");
				getParent().requestDisallowInterceptTouchEvent(false);
			}
		}
	}

	private void actionUp(MotionEvent event) {
		// 计算完之后把所有值初始化
		touchDirection = null;
		status = false;
		x = y = mx = my = -1;
		getParent().requestDisallowInterceptTouchEvent(true);
	}
	
}
