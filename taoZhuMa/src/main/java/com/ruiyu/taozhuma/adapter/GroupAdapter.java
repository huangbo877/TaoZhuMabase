package com.ruiyu.taozhuma.adapter;

import java.util.List;

import com.ruiyu.taozhuma.R;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupAdapter extends BaseAdapter {

	private Context context;

	private List<String> list;
	private int[] icons = {R.drawable.icon_gongsi,R.drawable.icon_erweima2,R.drawable.icon_fenxiang,R.drawable.icon_fenxiang,R.drawable.icon_erweima2};

	public GroupAdapter(Context context, List<String> list) {

		this.context = context;
		this.list = list;

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {

		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {

		
		ViewHolder holder;
		if (convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.group_item_view, null);
			holder=new ViewHolder();
			
			convertView.setTag(holder);
			
			holder.groupItem=(TextView) convertView.findViewById(R.id.groupItem);			
			holder.groupIconItem = (ImageView) convertView.findViewById(R.id.groupIconItem); 
			
			
		}
		else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.groupItem.setText(list.get(position));
		holder.groupIconItem.setImageResource(icons[position]);
		
		return convertView;
	}

	static class ViewHolder {
		TextView groupItem;
		ImageView groupIconItem;
	}

}
