package com.ruiyu.taozhuma.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.TzmCustomSearchModel;
import com.ruiyu.taozhuma.model.TzmSearchAllModel.Product;
import com.ruiyu.taozhuma.model.TzmSearchAllModel.Shop;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;

public class TzmSearchAdapter extends BaseAdapter {
	private String TAG = "TzmSearchAdapter";
	private LayoutInflater layoutInflater;
	private Context context;
	private ArrayList<TzmCustomSearchModel> list;
	private xUtilsImageLoader imageLoader;

	private List<Product> product_list;
	private List<Shop> shop_list;

	public TzmSearchAdapter(Context context,
			ArrayList<TzmCustomSearchModel> list, xUtilsImageLoader imageLoader) {
		layoutInflater = LayoutInflater.from(context);
		this.context = context;
		this.list = list;
		this.imageLoader = imageLoader;
		LogUtil.Log(TAG, "TzmSearchAdapter");
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
	public View getView(int position, View view, ViewGroup viweGroup) {
		ViewHolder viewHolder;
		
		if (view == null) {
			view = layoutInflater.inflate(R.layout.search_item, null);
			viewHolder = new ViewHolder();
			viewHolder.search_product = (RelativeLayout) view
					.findViewById(R.id.search_product);
			viewHolder.search_shop = (RelativeLayout) view
					.findViewById(R.id.search_shop);
			viewHolder.shopName = (TextView) view
					.findViewById(R.id.tv_shop_name);
			viewHolder.shopImage = (ImageView) view
					.findViewById(R.id.iv_shopImage);
			viewHolder.productName = (TextView) view
					.findViewById(R.id.tv_product_name);
			viewHolder.productPrice = (TextView) view
					.findViewById(R.id.tv_price);
			viewHolder.productSellNumber = (TextView) view
					.findViewById(R.id.tv_sellNumber);
			viewHolder.productPicture = (ImageView) view
					.findViewById(R.id.iv_picture);

			viewHolder.tv_mainCategory = (TextView) view
					.findViewById(R.id.tv_mainCategory);
			viewHolder.tv_address = (TextView) view
					.findViewById(R.id.tv_address);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final TzmCustomSearchModel info = this.list.get(position);
		if (info.type == 1) {
			viewHolder.search_product.setVisibility(View.VISIBLE);
			viewHolder.search_shop.setVisibility(View.GONE);
			viewHolder.productName.setText(info.productName);
			viewHolder.productPrice.setText("¥ " + info.distributionPrice);
			viewHolder.productSellNumber.setText("销量:" + info.sellNumber + "");
			if (StringUtils.isNotEmpty(info.productImage)) {
				imageLoader.display(viewHolder.productPicture, ""
						+ info.productImage);
			}
		}
		if (info.type == 2) {
			viewHolder.search_product.setVisibility(View.GONE);

			viewHolder.search_shop.setVisibility(View.VISIBLE);
			viewHolder.tv_mainCategory.setVisibility(View.VISIBLE);
			viewHolder.tv_address.setVisibility(View.VISIBLE);
			viewHolder.shopName.setText(info.shopName);
			viewHolder.tv_mainCategory.setText(info.mainCategory);

			viewHolder.tv_address.setText(info.address);
			Log.i("tag", ">>>>>>>>>>>" + info.mainCategory + info.address);
			if (StringUtils.isNotEmpty(info.shopImage)) {
				imageLoader.display(viewHolder.shopImage, "" + info.shopImage);
			}
		}
		return view;
	}

	private class ViewHolder {
		TextView shopName, productName, productPrice, productSellNumber,
				tv_mainCategory, tv_address;
		ImageView shopImage, productPicture;
		RelativeLayout search_product, search_shop;
	}
}
