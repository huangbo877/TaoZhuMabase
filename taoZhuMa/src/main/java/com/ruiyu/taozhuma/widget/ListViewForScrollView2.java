package com.ruiyu.taozhuma.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class ListViewForScrollView2 extends ListView {
    public ListViewForScrollView2(Context context) {
        super(context);
    }

    public ListViewForScrollView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewForScrollView2(Context context, AttributeSet attrs,
        int defStyle) {
        super(context, attrs, defStyle);
    }
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		
		return super.onInterceptTouchEvent(ev);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		return false;
	}
    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
        MeasureSpec.AT_MOST);
        //setMeasuredDimension(widthMeasureSpec, expandSpec);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
