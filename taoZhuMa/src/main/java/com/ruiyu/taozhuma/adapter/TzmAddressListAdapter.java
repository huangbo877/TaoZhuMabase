package com.ruiyu.taozhuma.adapter;

import java.util.List;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.model.TzmAddressModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TzmAddressListAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<TzmAddressModel> list;
	private Context c;

	public TzmAddressListAdapter(Context c, List<TzmAddressModel> list) {
		this.list = list;
		this.c = c;
		layoutInflater = LayoutInflater.from(c);
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
			view = layoutInflater.inflate(R.layout.tzm_address_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_userMessage = (TextView) view.findViewById(R.id.tv_userMessage);
			viewHolder.tv_address = (TextView) view.findViewById(R.id.tv_address);
			viewHolder.isDefault = (TextView) view.findViewById(R.id.isDefaulet);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag(); 
		}
		final TzmAddressModel info = this.list.get(position);
		viewHolder.tv_userMessage.setText(info.name+"   "+info.tel);
		viewHolder.tv_address.setText(info.address);
		if(info.isDefault == 0){
			viewHolder.isDefault.setText("");
		}
		if(info.isDefault == 1){
			viewHolder.isDefault.setText("榛樿鍦板潃");
		}
		
		return view;
	}

	/**
	 * 
	 * 
	 * @author Toby
	 * 
	 */
	private class ViewHolder {
		TextView tv_userMessage,tv_address,isDefault;
	}
}
