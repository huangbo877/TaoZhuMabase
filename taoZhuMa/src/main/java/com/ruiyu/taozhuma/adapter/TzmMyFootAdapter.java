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
import com.ruiyu.taozhuma.model.TzmMyFootModel;

public class TzmMyFootAdapter extends BaseAdapter{
	private LayoutInflater layoutInflater;
	private List<TzmMyFootModel> list;
	private Context c;
	private BitmapUtils bitmapUtils;
	
	public TzmMyFootAdapter(Context c, List<TzmMyFootModel> list) {
		this.list = list;
		this.c = c;
		layoutInflater = LayoutInflater.from(c);
		bitmapUtils = new BitmapUtils(c);
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
			view = layoutInflater.inflate(R.layout.tzm_myfoot_list, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_shopName = (TextView) view.findViewById(R.id.tv_shopName);
			viewHolder.tv_productName = (TextView) view.findViewById(R.id.tv_productName);
			viewHolder.tv_sellingPrice = (TextView) view.findViewById(R.id.tv_sellingPrice);
			viewHolder.tv_sellNumber = (TextView) view.findViewById(R.id.tv_sellNumber);
			viewHolder.iv_image = (ImageView) view
					.findViewById(R.id.iv_image);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final TzmMyFootModel info = this.list.get(position);
		viewHolder.tv_shopName.setText(info.shopName);
		viewHolder.tv_productName.setText(info.productName);
		viewHolder.tv_sellingPrice.setText("¥ " + info.sellingPrice);
		viewHolder.tv_sellNumber.setText(info.sellNumber+"");
		bitmapUtils.display(viewHolder.iv_image, "" + info.image);
		return view;
	}
	
	private class ViewHolder {
		TextView tv_shopName, tv_productName,tv_sellingPrice,tv_sellNumber;
		ImageView iv_image;
	}
	
}
