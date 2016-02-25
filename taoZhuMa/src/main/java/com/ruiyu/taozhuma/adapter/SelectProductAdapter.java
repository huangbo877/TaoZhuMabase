package com.ruiyu.taozhuma.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.base.BaseApplication;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.AgencyProductListsModle;
import com.ruiyu.taozhuma.utils.StringUtils;

public class SelectProductAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private ArrayList<AgencyProductListsModle> list;
	private Context context;
	private static HashMap<Integer, Boolean> isSelected; // 用来记录产品的选中状况
//	private List<Integer> pids;
	xUtilsImageLoader imageLoader;

	public SelectProductAdapter(Context context,
			ArrayList<AgencyProductListsModle> list,
			HashMap<Integer, Boolean> isSelected) {
		this.list = list;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
		this.isSelected = isSelected;
		imageLoader = new xUtilsImageLoader(context);
//		this.pids = pids;
//		initData(); //初始化数据
	}

	// 初始化isSelected的数据
//	private void initData() {
//		for (int i = 0; i < list.size(); i++) {
//			getIsSelected().put(i, false);
//		}
//	}

	// 静态,直接调用
	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	// 在Activity中可以设置对应选中的产品的状况：选中true;为选中:false
	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		SelectProductAdapter.isSelected = isSelected;
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
			view = layoutInflater.inflate(R.layout.select_product_list_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.iv_pictrue = (ImageView) view
					.findViewById(R.id.iv_pictrue);
			viewHolder.tv_product_name = (TextView) view
					.findViewById(R.id.tv_product_name);
			viewHolder.tv_old_price = (TextView) view
					.findViewById(R.id.tv_old_price);
			viewHolder.cb_select = (CheckBox) view.findViewById(R.id.cb_select);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final AgencyProductListsModle productListsModle = this.list
				.get(position);
//		if(productListsModle.checked==0){
//			isSelected.put(index, false);
//		}else{
//			isSelected.put(index, true);
//		}
		
		viewHolder.cb_select.setChecked(getIsSelected().get(position));
		viewHolder.tv_product_name.setText(productListsModle.name);
		viewHolder.tv_old_price.setText("¥ " + productListsModle.price);
		imageLoader.display(viewHolder.iv_pictrue, productListsModle.image);
		return view;
	}

	private class ViewHolder {
		ImageView iv_pictrue;
		TextView tv_product_name, tv_old_price;
		CheckBox cb_select;
	}

}
