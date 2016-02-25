package com.ruiyu.taozhuma.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.model.UserWelletDetailModel.source;

public class MineWalletAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<source> list;
	private Context c;

	public MineWalletAdapter(Context context, List<source> list) {
		this.list = list;
		this.c = context;
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;

		if (view == null) {
			view = layoutInflater.inflate(R.layout.mine_wallet_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_who = (TextView) view.findViewById(R.id.tv_who);
			viewHolder.tv_act = (TextView) view.findViewById(R.id.tv_act);
			viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		source info = this.list.get(position);
		viewHolder.tv_who.setText(info.source);
		viewHolder.tv_act.setText(info.digest);
		viewHolder.tv_time.setText(info.addTime);
		if(info.type ==1){
			viewHolder.tv_price.setText("扣除"+info.receive+"元");
		}else{
			viewHolder.tv_price.setText("获得"+info.receive+"元");
		}
		
		return view;
	}

	private class ViewHolder {
		TextView tv_who, tv_act, tv_time, tv_price;
	}

}
