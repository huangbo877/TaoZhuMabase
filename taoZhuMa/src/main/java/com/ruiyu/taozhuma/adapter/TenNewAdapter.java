package com.ruiyu.taozhuma.adapter;

import java.util.List;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.GetActivityProductByTypeModel;
import com.ruiyu.taozhuma.utils.LogUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 每日十件
 * 
 * @author Fu
 * 
 */
public class TenNewAdapter extends BaseAdapter {
	private String TAG = "TenNewAdapter";
	private LayoutInflater layoutInflater;
	private List<GetActivityProductByTypeModel> list;
	private Context c;
	private xUtilsImageLoader imageLoader;

	public TenNewAdapter(Context c, List<GetActivityProductByTypeModel> list,
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;
		LogUtil.Log(TAG, "getView");
		if (view == null) {
			view = layoutInflater.inflate(R.layout.ten_new_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
			viewHolder.iv_picture = (ImageView) view
					.findViewById(R.id.iv_picture);
			viewHolder.tv_recommendation = (TextView) view
					.findViewById(R.id.tv_recommendation);
			viewHolder.tv_sellNumber = (TextView) view
					.findViewById(R.id.tv_sellNumber);
			viewHolder.sell_price = (TextView) view
					.findViewById(R.id.sell_price);
			viewHolder.iv_tag = (ImageView) view.findViewById(R.id.iv_tag);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final GetActivityProductByTypeModel info = this.list.get(position);
		viewHolder.tv_name.setText(info.productName);
		viewHolder.tv_price.setText("¥ " + info.price);
		viewHolder.sell_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		viewHolder.sell_price.setText("¥ " + info.sellingPrice);
		imageLoader.display(viewHolder.iv_picture, info.image);
		viewHolder.tv_recommendation.setText(info.recommendation);
		viewHolder.tv_sellNumber.setText("已售" + info.sellNumber + "件");
		if (info.gid < 11) {
			viewHolder.iv_tag.setVisibility(View.VISIBLE);
			setTag(info.gid, viewHolder.iv_tag);
		} else {
			viewHolder.iv_tag.setVisibility(View.INVISIBLE);
		}
		LogUtil.Log("TAG", "--产品ID:" + info.productId + "--标志ID:" + info.gid
				+ "--活动ID:" + info.activityId.toString());
		return view;
	}

	private void setTag(int id, ImageView imageView) {
		switch (id) {
		case 1:
			imageView.setImageResource(R.drawable.tn1);
			break;
		case 2:
			imageView.setImageResource(R.drawable.tn2);
			break;
		case 3:
			imageView.setImageResource(R.drawable.tn3);
			break;
		case 4:
			imageView.setImageResource(R.drawable.tn4);
			break;
		case 5:
			imageView.setImageResource(R.drawable.tn5);
			break;
		case 6:
			imageView.setImageResource(R.drawable.tn6);
			break;
		case 7:
			imageView.setImageResource(R.drawable.tn7);
			break;
		case 8:
			imageView.setImageResource(R.drawable.tn8);
			break;
		case 9:
			imageView.setImageResource(R.drawable.tn9);
			break;
		case 10:
			imageView.setImageResource(R.drawable.tn10);
			break;
		}
	}

	private class ViewHolder {
		TextView tv_name, tv_price, tv_recommendation, tv_sellNumber,
				sell_price;
		ImageView iv_picture, iv_tag;
	}
}