package com.ruiyu.taozhuma.adapter;

import java.util.List;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.model.SortingModel;

import android.content.Context;
import android.hardware.Camera.Area;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProductSortListAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<SortingModel> list;
	private Context c;

	public ProductSortListAdapter(Context c, List<SortingModel> list) {
		this.list = list;
		this.c = c;
		layoutInflater = LayoutInflater.from(c);
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
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;

		if (view == null) {
			view = layoutInflater.inflate(R.layout.type_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.txt_title);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		SortingModel info = this.list.get(position);

		viewHolder.tvTitle.setText(info.name);
		return view;
	}
	
	/**
	 * 
	 * 
	 * @author Toby
	 * 
	 */
	private class ViewHolder {
		TextView tvTitle;
	}
}
