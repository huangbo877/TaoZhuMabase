package com.ruiyu.taozhuma.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.TzmBrandModel;
import com.ruiyu.taozhuma.model.TzmBrandModel.brandlist;
import com.ruiyu.taozhuma.model.TzmShopDetailModel;
import com.ruiyu.taozhuma.model.TzmShopListModel;

public class BrandAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private ArrayList<TzmBrandModel.brandlist> list;
	private Context c;
	BitmapUtils imageLoader;

	public BrandAdapter(Context c, ArrayList<TzmBrandModel.brandlist> list) {
		this.list = list;
		this.c = c;
		layoutInflater = LayoutInflater.from(c);
		imageLoader = new BitmapUtils(c);
		imageLoader.configDefaultBitmapConfig(Bitmap.Config.ARGB_4444);
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
			view = layoutInflater.inflate(R.layout.big_cart_list_item, null);
			viewHolder = new ViewHolder();

			viewHolder.im_shoplist = (ImageView) view
					.findViewById(R.id.im_shoplist);
			viewHolder.im_shoplist1 = (ImageView) view
					.findViewById(R.id.im_shoplist1);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final brandlist info = this.list.get(position);
		if (position <= 2) {
			imageLoader.display(viewHolder.im_shoplist, info.image);
			viewHolder.im_shoplist.setVisibility(View.VISIBLE);
			viewHolder.im_shoplist1.setVisibility(View.GONE);
		} else {
			imageLoader.display(viewHolder.im_shoplist1, info.image);
			viewHolder.im_shoplist1.setVisibility(View.VISIBLE);
			viewHolder.im_shoplist.setVisibility(View.GONE);
		}

		return view;
	}

	private class ViewHolder {
		TextView tv_name, tv_mainCategory, tv_address;
		ImageView im_shoplist, im_shoplist1;
	}

}
