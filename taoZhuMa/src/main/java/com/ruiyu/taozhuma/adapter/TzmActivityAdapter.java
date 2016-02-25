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
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.TzmActivityModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;

/*
 * fu
 * 
 * */
public class TzmActivityAdapter extends BaseAdapter {
	private String TAG = "TzmActivityAdapter";
	private LayoutInflater layoutInflater;
	private List<TzmActivityModel> list;
	private Context c;
	private xUtilsImageLoader imageLoader;

	public TzmActivityAdapter(Context c, List<TzmActivityModel> list,
			xUtilsImageLoader imageLoader) {
		this.list = list;
		this.c = c;
		layoutInflater = LayoutInflater.from(c);
		this.imageLoader = imageLoader;
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
		if (view == null) {
			view = layoutInflater.inflate(R.layout.tzm_hot_product_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
			viewHolder.tv_sellNumber = (TextView) view
					.findViewById(R.id.tv_sellNumber);
			viewHolder.iv_picture = (ImageView) view
					.findViewById(R.id.iv_picture);
			// viewHolder.llv_product = (LinearLayoutForListView)
			// view.findViewById(R.id.llv_product);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final TzmActivityModel info = this.list.get(position);
		viewHolder.tv_name.setText(info.name);
		viewHolder.tv_price.setText("¥ " + info.price);
		viewHolder.tv_sellNumber.setText("销售:" + info.sellNumber);
		if (StringUtils.isNotEmpty(info.image)) {
			imageLoader.display(viewHolder.iv_picture, info.image);
		}
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

		LogUtil.Log("TAG", "-专场活动ID:" +info.activityId);
		return view;
	}

	/**
	 * 
	 * 
	 * @author Toby
	 * 
	 */
	private class ViewHolder {
		TextView tv_name, tv_price, tv_sellNumber;
		ImageView iv_picture;
		// LinearLayoutForListView llv_product;
	}
}
