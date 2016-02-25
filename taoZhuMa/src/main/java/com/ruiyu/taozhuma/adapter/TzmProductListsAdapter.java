package com.ruiyu.taozhuma.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.model.TzmProductListsModel;

public class TzmProductListsAdapter extends BaseAdapter {

	private Context context;
	private List<TzmProductListsModel> list;
	private LayoutInflater layoutInflater;
	private BitmapUtils bitmapUtils;

	public TzmProductListsAdapter(Context context,
			List<TzmProductListsModel> list) {
		this.context = context;
		this.list = list;
		layoutInflater = LayoutInflater.from(context);
		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.tzm_defult_img);
		bitmapUtils.configDefaultLoadingImage(R.drawable.tzm_defult_img);
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
			view = layoutInflater.inflate(R.layout.tzm_shop_detail_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
			viewHolder.iv_picture = (ImageView) view
					.findViewById(R.id.iv_picture);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final TzmProductListsModel info = this.list.get(position);
		viewHolder.tv_name.setText(info.name);
		viewHolder.tv_price.setText("¥ " + info.price);
		bitmapUtils.display(viewHolder.iv_picture, "" + info.image);
		return view;
	}
	
	private class ViewHolder {
		TextView tv_name, tv_price;
		ImageView iv_picture;
	}
}
