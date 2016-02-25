package com.ruiyu.taozhuma.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.TzmShopListModel;

public class SelectShopAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<TzmShopListModel> list;
	private Context c;
	xUtilsImageLoader imageLoader;
	
	public SelectShopAdapter(Context c, List<TzmShopListModel> list) {
		this.list = list;
		this.c = c;
		layoutInflater = LayoutInflater.from(c);
		imageLoader = new xUtilsImageLoader(c);
	}
	@Override
	public int getCount() {
		return this.list.size();
	}

	@Override
	public Object getItem(int position) {
		return this.list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;

		if (view == null) {
			view = layoutInflater.inflate(R.layout.big_cart_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			viewHolder.tv_address = (TextView) view.findViewById(R.id.tv_address);
			viewHolder.tv_mainCategory = (TextView) view.findViewById(R.id.tv_mainCategory);
			viewHolder.im_shoplist = (ImageView) view.findViewById(R.id.im_shoplist);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final TzmShopListModel info = this.list.get(position);
		viewHolder.tv_name.setText(info.name);
		viewHolder.tv_address.setText(info.address);
		viewHolder.tv_mainCategory.setText(info.mainCategory);
		imageLoader.display(viewHolder.im_shoplist, info.image);

		return view;
	}

	
	private class ViewHolder {
		TextView tv_name,tv_mainCategory,tv_address;
		ImageView im_shoplist;
	}
	
}
