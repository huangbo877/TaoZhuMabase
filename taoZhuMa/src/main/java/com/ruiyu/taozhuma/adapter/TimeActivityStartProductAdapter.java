package com.ruiyu.taozhuma.adapter;

import java.text.DecimalFormat;
import java.util.List;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.ProductDetailActivity;
import com.ruiyu.taozhuma.activity.TzmShopDetailActivity;
import com.ruiyu.taozhuma.activity.TzmTimeActivityProductDetailActivity;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.ActivityGoodsModel.ActivityGoods;
import com.ruiyu.taozhuma.utils.ToastUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TimeActivityStartProductAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<ActivityGoods> list;
	private Context c;
	xUtilsImageLoader imageLoader;
	DecimalFormat df;
	private Integer activityId;

	public TimeActivityStartProductAdapter(Context c, List<ActivityGoods> list,
			Integer activityId) {
		this.list = list;
		this.c = c;
		this.activityId = activityId;
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
		TextView tv_product, tv_sell_percent, tv_tips, tv_price;
		ImageView im_product, iv_tag_over;
		ProgressBar progressBar;
		Button btn_buy;
		RelativeLayout rl_mian;
		TextView tv_skuvalue;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;

		if (view == null) {
			view = layoutInflater.inflate(
					R.layout.time_activity_list_start_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_product = (TextView) view
					.findViewById(R.id.tv_product);
			viewHolder.tv_sell_percent = (TextView) view
					.findViewById(R.id.tv_sell_percent);
			viewHolder.tv_tips = (TextView) view.findViewById(R.id.tv_tips);
			viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
			viewHolder.im_product = (ImageView) view
					.findViewById(R.id.im_product);
			viewHolder.progressBar = (ProgressBar) view
					.findViewById(R.id.progressBar);
			viewHolder.btn_buy = (Button) view.findViewById(R.id.btn_buy);
			viewHolder.rl_mian = (RelativeLayout) view
					.findViewById(R.id.rl_mian);
			viewHolder.tv_skuvalue = (TextView) view
					.findViewById(R.id.tv_skuvalue);
			viewHolder.iv_tag_over = (ImageView) view
					.findViewById(R.id.iv_tag_over);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final ActivityGoods info = this.list.get(position);
		viewHolder.tv_skuvalue.setText(info.skuValue);
		viewHolder.tv_product.setText(info.productName);
		viewHolder.tv_tips.setText(info.tips);
		viewHolder.tv_price.setText("¥ " + info.productPrice);
		imageLoader.display(viewHolder.im_product, info.productImage);
		Integer resut = Integer.parseInt(df
				.format(((float) info.activityNum - info.activityStock)
						/ (float) info.activityNum * 100));
		viewHolder.tv_sell_percent.setText("已售" + resut + "%");
		if (resut == 100) {
			// 售完
			viewHolder.iv_tag_over.setVisibility(View.VISIBLE);
			viewHolder.tv_tips
					.setBackgroundResource(R.drawable.gray_solid_corner);
			viewHolder.btn_buy
					.setBackgroundResource(R.drawable.pink_stroke_corner);
			viewHolder.btn_buy.setTextColor(c.getResources().getColor(
					R.color.base));
			viewHolder.btn_buy.setText("去店铺");
			viewHolder.progressBar.setProgressDrawable(c.getResources()
					.getDrawable(R.drawable.progress_color_horizontal_gray));
			viewHolder.btn_buy.setClickable(true);
			OnClickListener clickListener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (v.getId()) {
					case R.id.rl_mian:
						Intent intent = new Intent(c,
								ProductDetailActivity.class);
						intent.putExtra("id", info.productId);
						c.startActivity(intent);
						break;

					case R.id.btn_buy:
						Intent intent2 = new Intent(c,
								TzmShopDetailActivity.class);
						intent2.putExtra("id", info.shopId);
						intent2.putExtra("shopName", info.shopName);
						c.startActivity(intent2);
						break;
					}

				}
			};
			viewHolder.rl_mian.setOnClickListener(clickListener);
			viewHolder.btn_buy.setOnClickListener(clickListener);

		} else {
			// 未售完
			viewHolder.iv_tag_over.setVisibility(View.GONE);
			viewHolder.tv_tips
					.setBackgroundResource(R.drawable.pink_solid_corner);
			viewHolder.btn_buy
					.setBackgroundResource(R.drawable.pink_solid_corner);
			viewHolder.btn_buy.setTextColor(c.getResources().getColor(
					R.color.white));
			viewHolder.btn_buy.setText("去抢购");
			viewHolder.progressBar.setProgressDrawable(c.getResources()
					.getDrawable(R.drawable.progress_color_horizontal));
			viewHolder.btn_buy.setClickable(false);
			OnClickListener clickListener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (v.getId()) {
					case R.id.rl_mian:
						Intent intent = new Intent(c,
								ProductDetailActivity.class);
						intent.putExtra("id", info.productId);
						intent.putExtra("activityId", activityId);
						c.startActivity(intent);

						break;
					}

				}
			};
			viewHolder.rl_mian.setOnClickListener(clickListener);

		}
		viewHolder.progressBar.setProgress(resut);

		// viewHolder.im_product.setOnClickListener(clickListener);
		// viewHolder.tv_product.setOnClickListener(clickListener);
		// viewHolder.btn_buy.setOnClickListener(clickListener);
		return view;
	}

}
