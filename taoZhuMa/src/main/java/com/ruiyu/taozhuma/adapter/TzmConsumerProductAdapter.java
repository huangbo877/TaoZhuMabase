package com.ruiyu.taozhuma.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.TzmCollectModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author yangfeng
 */
public class TzmConsumerProductAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private ArrayList<TzmCollectModel> list;
	private Context context;
	private static HashMap<Integer, Boolean> isSelected; // 用来记录产品的选中状况
	private static Boolean isVISIBLE = false;
	xUtilsImageLoader imageLoader;

	@SuppressWarnings("static-access")
	public TzmConsumerProductAdapter(Context context,
			ArrayList<TzmCollectModel> list,
			HashMap<Integer, Boolean> isSelected, Boolean isVISIBLE) {
		this.list = list;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
		this.isSelected = isSelected;
		this.isVISIBLE = isVISIBLE;
		imageLoader = new xUtilsImageLoader(context);
		// initData(); //初始化数据
	}

	public static void setViewStatus(Boolean isVISIBLE) {
		TzmConsumerProductAdapter.isVISIBLE = isVISIBLE;
	}


	// 静态,直接调用
	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	// 在Activity中可以设置对应选中的产品的状况：选中true;为选中:false
	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		TzmConsumerProductAdapter.isSelected = isSelected;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.list.size();

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		ViewHolder viewHolder;
		if (view == null) {
			view = layoutInflater.inflate(R.layout.tzm_consumer_product_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.iv_pictrue = (ImageView) view
					.findViewById(R.id.iv_pictrue);
			viewHolder.tv_product_name = (TextView) view
					.findViewById(R.id.tv_product_name);
			viewHolder.tv_old_price = (TextView) view
					.findViewById(R.id.tv_old_price);
			viewHolder.cb_select = (CheckBox) view.findViewById(R.id.cb_select);
			viewHolder.layout = (FrameLayout) view.findViewById(R.id.layout);
			// boolean isClick = getIsSelected().get(index);
			// if(isClick){
			// viewHolder.iv_Selected.setVisibility(View.VISIBLE);//可见
			// }else{
			// viewHolder.iv_Selected.setVisibility(View.GONE);//不可见
			// }
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final TzmCollectModel tzmCollectModel = this.list.get(position);
		if (isVISIBLE) {
			viewHolder.cb_select.setVisibility(View.VISIBLE);
		} else {
			viewHolder.cb_select.setVisibility(View.GONE);
		}
		viewHolder.cb_select.setChecked(getIsSelected().get(position));
		viewHolder.tv_product_name.setText(tzmCollectModel.favName);
		viewHolder.tv_old_price.setText("¥ " + tzmCollectModel.favPrice);
		if (tzmCollectModel.status == 0) {
			viewHolder.layout.setVisibility(View.VISIBLE);
		} else {
			viewHolder.layout.setVisibility(View.GONE);
		}
		imageLoader.display(viewHolder.iv_pictrue, tzmCollectModel.favImage);
		return view;
	}

	private class ViewHolder {
		ImageView iv_pictrue;
		TextView tv_product_name, tv_old_price;
		CheckBox cb_select;
		FrameLayout layout;
	}
}
