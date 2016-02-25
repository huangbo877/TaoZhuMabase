package com.ruiyu.taozhuma.adapter;

import java.util.List;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.model.TzmProductDetailCommentModel;
import com.ruiyu.taozhuma.widget.FlexibleRatingBar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TzmCommentListAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<TzmProductDetailCommentModel> list;
	private Context c;
	

	public TzmCommentListAdapter(Context c,
			List<TzmProductDetailCommentModel> list) {
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;

		if (view == null) {
			view = layoutInflater.inflate(R.layout.tzm_comment_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_username = (TextView) view
					.findViewById(R.id.tv_username);
			viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			viewHolder.tv_content = (TextView) view
					.findViewById(R.id.tv_content);
			viewHolder.tv_sku = (TextView) view.findViewById(R.id.tv_sku);
			viewHolder.rabarServer = (FlexibleRatingBar) view
					.findViewById(R.id.rabarServer);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final TzmProductDetailCommentModel info = this.list.get(position);
		/*String str;
		
			if (info.userName.length() > 2) {
				str = info.userName.substring(0, 2) + "***(匿名)";
			} else {
				str = info.userName + "***(匿名)";
			}*/
			viewHolder.tv_username.setText(info.userName);
			viewHolder.tv_sku.setText(info.skuValue);
			viewHolder.rabarServer.setRating(info.productScore);
			viewHolder.tv_time.setText(info.time);
			viewHolder.tv_content.setText(info.comment);
		
		return view;
	}

	/**
	 * 
	 * 
	 * @author Toby
	 * 
	 */
	private class ViewHolder {
		TextView tv_username, tv_time, tv_content, tv_sku;
		FlexibleRatingBar rabarServer;
	}
}
