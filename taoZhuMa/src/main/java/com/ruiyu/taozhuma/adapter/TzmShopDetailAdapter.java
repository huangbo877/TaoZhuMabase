package com.ruiyu.taozhuma.adapter;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.GroupAdapter.ViewHolder;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.TzmActivityModel;
import com.ruiyu.taozhuma.model.TzmShopDetailModel;
import com.ruiyu.taozhuma.utils.LogUtil;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author LinJianhong
 * @version 创建时间：2015年4月13日 下午3:29:45 类说明：店铺详情Adapter类
 */
public class TzmShopDetailAdapter extends BaseAdapter {

	private String TAG = "TzmShopDetailAdapter";
	private LayoutInflater layoutInflater;
	private ArrayList<TzmShopDetailModel.Product> list;
	private Context c;
	private xUtilsImageLoader bitmapUtils;

	public TzmShopDetailAdapter(Context c,
			ArrayList<TzmShopDetailModel.Product> list) {
		LogUtil.Log(TAG, TAG + "---->>TzmShopDetailAdapter");
		this.list = list;
		this.c = c;
		bitmapUtils = new xUtilsImageLoader(c);
		layoutInflater = LayoutInflater.from(c);
	}

	public void setList(ArrayList<TzmShopDetailModel.Product> list) {
		this.list = list;

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
		final ViewHolder viewHolder;
		if (view == null) {
			view = layoutInflater.inflate(R.layout.tzm_shop_detail_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
			viewHolder.iv_picture = (ImageView) view
					.findViewById(R.id.iv_picture);
			viewHolder.tv_yuan_price = (TextView) view
					.findViewById(R.id.tv_yuan_price);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final TzmShopDetailModel.Product info = this.list.get(position);
		try {

			viewHolder.tv_name.setText(info.productName);
			viewHolder.tv_price.setText("¥ " + info.activePrice);
			viewHolder.tv_yuan_price.setText("¥ " + info.sellPrice);
			viewHolder.tv_yuan_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG ); 
			bitmapUtils.display(viewHolder.iv_picture, "" + info.image);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return view;
	}

	private class ViewHolder {
		TextView tv_name, tv_price, tv_sellNumber,tv_yuan_price;
		ImageView iv_picture;

	}
}
