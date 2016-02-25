package com.ruiyu.taozhuma.adapter;

import java.util.List;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.TzmShopListModel;
import com.ruiyu.taozhuma.utils.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * fu
 * 
 * */
public class TzmShopListAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<TzmShopListModel> list;
	private Context c;
	private xUtilsImageLoader imageLoader;

	public TzmShopListAdapter(Context c, List<TzmShopListModel> list,
			xUtilsImageLoader imageLoader) {
		this.list = list;
		this.c = c;
		this.imageLoader = imageLoader;
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
			view = layoutInflater.inflate(R.layout.tzm_shop_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			viewHolder.tv_address = (TextView) view
					.findViewById(R.id.tv_address);
			viewHolder.tv_mainCategory = (TextView) view
					.findViewById(R.id.tv_mainCategory);
			viewHolder.im_shoplist = (ImageView) view
					.findViewById(R.id.im_shoplist);
			// viewHolder.llv_product = (LinearLayoutForListView)
			// view.findViewById(R.id.llv_product);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final TzmShopListModel info = this.list.get(position);
		viewHolder.tv_name.setText(info.name);
		viewHolder.tv_address.setText(info.address);
		viewHolder.tv_mainCategory.setText(info.mainCategory);
		if (!StringUtils.isEmpty(info.image)) {
			imageLoader.display(viewHolder.im_shoplist, info.image);
			// BaseApplication.getInstance().getImageWorker()
			// .loadBitmap(info.image, viewHolder.im_shoplist);
		}
		// BitmapUtils bitmapUtils = new BitmapUtils(c);
		// bitmapUtils.display(viewHolder.im_shoplist, info.image);
		// final OrderDetailModel info = this.list.get(position);

		// LinearListOrderAffirmAdapter adapter = new
		// LinearListOrderAffirmAdapter(c, info.products);
		// viewHolder.llv_product.setAdapter(adapter);
		// viewHolder.hp_product_image.setOnClickListener(new OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View paramView) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(c, GoodsDetaliActivity.class);
		// intent.putExtra("id", info.id);
		// c.startActivity(intent);
		//
		// }
		// });

		return view;
	}

	/**
	 * 
	 * 
	 * @author Toby
	 * 
	 */
	private class ViewHolder {
		TextView tv_name, tv_mainCategory, tv_address;
		ImageView im_shoplist;
		// LinearLayoutForListView llv_product;
	}
}
