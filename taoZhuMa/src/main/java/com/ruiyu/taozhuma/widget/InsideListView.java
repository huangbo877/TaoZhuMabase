package com.ruiyu.taozhuma.widget;

import com.lidroid.xutils.view.annotation.event.OnScrollStateChanged;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class InsideListView extends ListView  {
	private Context context;
    private boolean isScroll = true;// webview 是否滚动
    
    private boolean isBottom;
    private boolean isTop;
    
	public InsideListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public InsideListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InsideListView(Context context) {
		super(context);
	}
	@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
            //当拦截触摸事件到达此位置的时候，返回true，
			//说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent
            //和android的触屏事件由上至下一层一层传播有关
            return isScroll;
    }
	
	 @Override
	    public boolean onTouchEvent(MotionEvent ev) {
	        switch (ev.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	            break;
	        case MotionEvent.ACTION_MOVE:
	            break;
	        case MotionEvent.ACTION_UP:
	            break;
	        }
	        return super.onTouchEvent(ev);
	    }

	@Override
	public void onScreenStateChanged(int screenState) {
		super.onScreenStateChanged(screenState);
		switch (screenState) {  
	    // 当不滚动时  
	    case OnScrollListener.SCROLL_STATE_IDLE:  
	    // 判断滚动到底部  
	    if (getLastVisiblePosition() == (getCount() - 1)) { 
	    	isBottom=true;
	    }else {
	    	isBottom=false;
	    }
	    // 判断滚动到顶部  
	    if(getFirstVisiblePosition() == 0){  
	    	isTop=true;
	    }  else{
	    	isTop=false;
	    }
	  
	     break;
		}
	}
}
