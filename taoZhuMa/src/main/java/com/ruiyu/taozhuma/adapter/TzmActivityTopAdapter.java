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
public class TzmActivityTopAdapter extends BaseAdapter {
	private String TAG="TzmActivityTopAdapter";
	private LayoutInflater layoutInflater;
	private List<TzmActivityModel> list;
	private Context c;
	private xUtilsImageLoader imageLoader;

	public TzmActivityTopAdapter(Context c, List<TzmActivityModel> list,
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
			viewHolder.iv_toptag = (ImageView) view
					.findViewById(R.id.iv_toptag);
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
		imageLoader.display(viewHolder.iv_picture, info.image);
		if (info.rowid < 11) {
			viewHolder.iv_toptag.setVisibility(View.VISIBLE);
			setTag(info.rowid, viewHolder.iv_toptag);
		} else {
			viewHolder.iv_toptag.setVisibility(View.GONE);
		}
        LogUtil.Log(TAG, "活动ID"+info.activityId.toString());
		return view;
	}

	private void setTag(int id, ImageView imageView) {
		switch (id) {
		case 1:
			imageView.setImageResource(R.drawable.toplogo1);
			break;
		case 2:
			imageView.setImageResource(R.drawable.toplogo2);
			break;
		case 3:
			imageView.setImageResource(R.drawable.toplogo3);
			break;
		case 4:
			imageView.setImageResource(R.drawable.toplogo4);
			break;
		case 5:
			imageView.setImageResource(R.drawable.toplogo5);
			break;
		case 6:
			imageView.setImageResource(R.drawable.toplogo6);
			break;
		case 7:
			imageView.setImageResource(R.drawable.toplogo7);
			break;
		case 8:
			imageView.setImageResource(R.drawable.toplogo8);
			break;
		case 9:
			imageView.setImageResource(R.drawable.toplogo9);
			break;
		case 10:
			imageView.setImageResource(R.drawable.toplogo10);
			break;
		}
	}

	/**
	 * 
	 * 
	 * @author Toby
	 * 
	 */
	private class ViewHolder {
		TextView tv_name, tv_price, tv_sellNumber;
		ImageView iv_picture, iv_toptag;
		// LinearLayoutForListView llv_product;
	}
}
