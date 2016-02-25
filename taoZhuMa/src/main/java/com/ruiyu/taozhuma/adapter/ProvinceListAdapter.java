package com.ruiyu.taozhuma.adapter;

import java.util.List;

import com.ruiyu.taozhuma.model.ProvinceModel;

import android.content.Context;
import android.widget.ArrayAdapter;

public class ProvinceListAdapter extends ArrayAdapter<String> {
	private List<ProvinceModel> mList;
	
	public ProvinceListAdapter(Context pContext,int resId, List<ProvinceModel> pList) {
		super(pContext, resId);
		this.mList = pList;
		this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public String getItem(int position) {
		return mList.get(position).province;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
//		convertView = _LayoutInflater.inflate(
//				android.R.layout.simple_spinner_item, null);
//		if (convertView != null) {
//			TextView _TextView1 = (TextView) convertView
//					.findViewById(android.R.id.text1);
//			_TextView1.setText(mList.get(position).name);
//		}
//		return convertView;
//	}

}
