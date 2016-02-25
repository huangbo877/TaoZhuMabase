package com.ruiyu.taozhuma.adapter;

import java.text.DecimalFormat;
import java.util.List;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.ProductDetailActivity;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.ActivityGoodsModel.ActivityGoods;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TimeActivityProductAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<ActivityGoods> list;
	private Context c;
	xUtilsImageLoader imageLoader;
	DecimalFormat df;

	public TimeActivityProductAdapter(Context c, List<ActivityGoods> list) {
		this.list = list;
		this.c = c;
		df = new DecimalFormat("#");
		this.imageLoader = new xUtilsImageLoader(c);
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

	/**
	 * 
	 * 
	 * @author Toby
	 * 
	 */
	private class ViewHolder {
		TextView tv_product, tv_price, tv_skuvalue;
		ImageView im_product;
		RelativeLayout rl_mian;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;

		if (view == null) {
			view = layoutInflater.inflate(R.layout.time_activity_list_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.tv_product = (TextView) view
					.findViewById(R.id.tv_product);
			viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
			viewHolder.im_product = (ImageView) view
					.findViewById(R.id.im_product);
			viewHolder.rl_mian = (RelativeLayout) view
					.findViewById(R.id.rl_mian);
			viewHolder.tv_skuvalue = (TextView) view
					.findViewById(R.id.tv_skuvalue);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final ActivityGoods info = this.list.get(position);

		viewHolder.tv_product.setText(info.productName);
		viewHolder.tv_skuvalue.setText(info.skuValue);
		viewHolder.tv_price.setText("¥ " + info.productPrice);
		imageLoader.display(viewHolder.im_product, info.productImage);
		OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(c, ProductDetailActivity.class);
				intent.putExtra("id", info.productId);
				c.startActivity(intent);

			}
		};
		viewHolder.rl_mian.setOnClickListener(clickListener);
		// viewHolder.im_product.setOnClickListener(clickListener);
		// viewHolder.tv_product.setOnClickListener(clickListener);
		return view;
	}

}
