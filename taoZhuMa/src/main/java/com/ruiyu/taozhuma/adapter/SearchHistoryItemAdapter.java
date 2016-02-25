package com.ruiyu.taozhuma.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ruiyu.taozhuma.R;

public class SearchHistoryItemAdapter extends BaseAdapter{
	private LayoutInflater layoutInflater;
	private List<String> list;
	private Context context;
	public SearchHistoryItemAdapter(Context context,List<String> list) {
		this.list=list;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
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
			view = layoutInflater.inflate(R.layout.search_history_item1, null);
			viewHolder = new ViewHolder();
			viewHolder.string = (TextView) view.findViewById(R.id.textView1);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.string.setText(this.list.get(position));
		return view;
	}
	private class ViewHolder {
		TextView string;
	}
}
