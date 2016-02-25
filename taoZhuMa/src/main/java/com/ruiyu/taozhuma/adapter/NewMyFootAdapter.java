package com.ruiyu.taozhuma.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.ProductDetailActivity;
import com.ruiyu.taozhuma.base.BaseApplication;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.NewMyFootModel;
import com.ruiyu.taozhuma.model.NewMyFootModel.Products;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;

public class NewMyFootAdapter extends BaseAdapter {
	private String TAG = "NewMyFootAdapter";
	private LayoutInflater layoutInflater;
	private Context context;
	private ArrayList<NewMyFootModel> list;
	private int j = 0;
	View convertView[] = null;
	private xUtilsImageLoader bitmapUtils;

	public NewMyFootAdapter(Context context, ArrayList<NewMyFootModel> list,
			xUtilsImageLoader bitmapUtils) {
		this.context = context;
		this.list = list;
		layoutInflater = LayoutInflater.from(context);
		this.bitmapUtils = bitmapUtils;
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
		LogUtil.Log(TAG, "getView");
		final NewMyFootModel model = this.list.get(position);
		if (view == null) {
			view = layoutInflater.inflate(R.layout.myfoot_list, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			viewHolder.rl_list_item = (LinearLayout) view
					.findViewById(R.id.rl_list_item);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.tv_title.setText(model.viewTime + "（共查看：" + model.num
				+ "件宝贝）");
		if (viewHolder.rl_list_item.getChildCount() > 0) {
			viewHolder.rl_list_item.removeAllViews();
		}
		if (model.products.size() != 0) {
			for (int index = 0; index < model.products.size(); index++) {
				final int i = index;
				convertView = new View[model.products.size()];
				convertView[index] = (View) layoutInflater.inflate(
						R.layout.myfoot_list_item, null);
				j = index;
				convertView[index].setId(j);
				viewHolder.tv_productName = (TextView) convertView[index]
						.findViewById(R.id.tv_productName);
				viewHolder.tv_sellingPrice = (TextView) convertView[index]
						.findViewById(R.id.tv_sellingPrice);
				viewHolder.tv_sellNumber = (TextView) convertView[index]
						.findViewById(R.id.tv_sellNumber);
				viewHolder.iv_image = (ImageView) convertView[index]
						.findViewById(R.id.iv_image);
				viewHolder.iv_product_status = (ImageView) convertView[index]
						.findViewById(R.id.iv_product_status);
				
				
				
				// 未开始
				if (model.products.get(index).proStatus==0) {
					bitmapUtils.display(viewHolder.iv_image,
							model.products.get(index).image);
					
				}
				// 进行中
				if (model.products.get(index).proStatus==1) {
					bitmapUtils.display(viewHolder.iv_image,
							model.products.get(index).image);
					
				}
				// 已结束
				if (model.products.get(index).proStatus==2) {
					bitmapUtils.display(viewHolder.iv_image,
							model.products.get(index).image);
					viewHolder.iv_product_status.setVisibility(View.VISIBLE);

				}
				
				
				if (model.products.get(index).proStatus == 0) {
					viewHolder.tv_productName.setTextColor(context
							.getResources().getColor(R.color.tzm_text_gray));
					viewHolder.tv_productName
							.setText(model.products.get(index).productName
									+ "(已下架)");
				} else {
					viewHolder.tv_productName.setTextColor(context
							.getResources().getColor(R.color.black));
					viewHolder.tv_productName
							.setText(model.products.get(index).productName);
				}

				viewHolder.tv_sellingPrice.setText("¥ "
						+ model.products.get(index).distributionPrice);
				viewHolder.tv_sellNumber.setText("月销:"
						+ model.products.get(index).sellNumber);
				convertView[index].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						LogUtil.Log(TAG, "getView----------onClick");
						Intent intent = new Intent(context,
								ProductDetailActivity.class);
						intent.putExtra("id", Integer.parseInt(model.products
								.get(i).productId));
						intent.putExtra("activityId",
								model.products.get(i).activityId);
						LogUtil.Log("TAG", "--专场活动ID:"
								+ model.products.get(i).activityId.toString());
						context.startActivity(intent);

					}
				});
				viewHolder.rl_list_item.addView(convertView[index]);
				LogUtil.Log("TAG", "--专场活动ID:"
						+ model.products.get(index).activityId.toString());

			}
		}

		return view;
	}

	private class ViewHolder {
		TextView tv_title, tv_sellingPrice, tv_productName, tv_sellNumber;
		ImageView iv_image,iv_product_status;
		LinearLayout rl_list_item;
	}

}
