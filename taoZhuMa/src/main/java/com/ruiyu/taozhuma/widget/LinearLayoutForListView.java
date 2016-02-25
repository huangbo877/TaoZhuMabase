package com.ruiyu.taozhuma.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

/**
 * 取代ListView的LinearLayout，使之能够成功嵌套在ScrollView中
 * 
 * @author terry_龙
 */
public class LinearLayoutForListView extends LinearLayout {

	private BaseAdapter adapter;
	private OnClickListener onClickListener = null;
	private OnItemClickListener onItemClickListener;

	/**
	 * 绑定布局
	 */
	public void bindLinearLayout() {
		int count = adapter.getCount();
		this.removeAllViews();
		for (int i = 0; i < count; i++) {
			View v = adapter.getView(i, null, null);
			v.setOnClickListener(this.onClickListener);
			addView(v, i);
		}
		Log.v("countTAG", "" + count);
	}

	public LinearLayoutForListView(Context context) {
		super(context);

	}

	public LinearLayoutForListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

	}

	/**
	 * 获取Adapter
	 * 
	 * @return adapter
	 */
	public BaseAdapter getAdpater() {
		return adapter;
	}

	/**
	 * 设置数据
	 * 
	 * @param adpater
	 */
	public void setAdapter(BaseAdapter adpater) {
		this.adapter = adpater;
		bindLinearLayout();
	}

	/**
	 * 获取点击事件
	 * 
	 * @return
	 */
	public OnClickListener getOnclickListner() {
		return onClickListener;
	}

	/**
	 * 设置点击事件
	 * 
	 * @param onClickListener
	 */
	public void setOnclickLinstener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	/**
	 * 
	 * 回调接口
	 */
	public interface OnItemClickListener {
		/**
		 * 
		 * @param v
		 *            点击的 view
		 * @param obj
		 *            点击的 view 所绑定的对象
		 * @param position
		 *            点击位置的 index
		 */
		public void onItemClicked(View v, Object obj, int position);
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;

	}

}