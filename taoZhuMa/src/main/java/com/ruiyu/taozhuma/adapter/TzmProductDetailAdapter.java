package com.ruiyu.taozhuma.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.ProductDetailActivity;
import com.ruiyu.taozhuma.model.TzmProductDetailModel.Attrs;

/*
 * fu
 * 
 * */
public class TzmProductDetailAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<Attrs> list;
	private Context c;

	public TzmProductDetailAdapter(Context c, List<Attrs> list) {
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
			view = layoutInflater.inflate(R.layout.tzm_product_detail_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.tv_product_name = (TextView) view
					.findViewById(R.id.tv_product_name);
			viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
			viewHolder.im_product = (ImageView) view
					.findViewById(R.id.im_product);
			viewHolder.rl_main = (RelativeLayout) view
					.findViewById(R.id.rl_main);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final Attrs info = this.list.get(position);
		viewHolder.tv_product_name.setText(info.name);
		viewHolder.tv_price.setText("¥ " + info.price);
		BitmapUtils bitmapUtils = new BitmapUtils(c);
		bitmapUtils.display(viewHolder.im_product, info.image);
		// if(!StringUtils.isEmpty(info.image)){
		// BaseApplication.getInstance().getImageWorker()
		// .loadBitmap(info.image, viewHolder.iv_picture);
		// }
		// LinearListOrderAffirmAdapter adapter = new
		// LinearListOrderAffirmAdapter(c, info.products);
		viewHolder.rl_main.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(c, ProductDetailActivity.class);
				intent.putExtra("id", info.productId);
				c.startActivity(intent);

			}
		});

		return view;
	}

	/**
	 * 
	 * 
	 * @author Toby
	 * 
	 */
	private class ViewHolder {
		TextView tv_price, tv_product_name;
		ImageView im_product;
		RelativeLayout rl_main;

	}
}
