package com.ruiyu.taozhuma.adapter;

import java.util.ArrayList;
import java.util.List;


import com.lidroid.xutils.BitmapUtils;
import com.ruiyu.taozhuma.R.color;
import com.ruiyu.taozhuma.R.id;
import com.ruiyu.taozhuma.R.layout;
import com.ruiyu.taozhuma.R;


import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.GetFocusActivityProductModel;
import com.ruiyu.taozhuma.model.TzmActivityModel;
import com.ruiyu.taozhuma.model.TzmShopDetailModel;
import com.ruiyu.taozhuma.model.GetFocusActivityProductModel.Products;

import android.content.Context;
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
public class GetFocusActivityProductAdapter extends BaseAdapter {
    
	private String TAG="TzmShopDetailAdapter";
	private LayoutInflater layoutInflater;
	private ArrayList<Products> list;
	private Context c;
	private xUtilsImageLoader bitmapUtils;

	public GetFocusActivityProductAdapter(Context c, ArrayList<Products> list) {
		Log.i(TAG, TAG+"---->>TzmShopDetailAdapter");
		this.list = list;
		this.c = c;
		bitmapUtils = new xUtilsImageLoader(c);
		layoutInflater = LayoutInflater.from(c);
	}

	public void setList(ArrayList<Products> list) {
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
		ViewHolder viewHolder;
		if (view == null) {
			view = layoutInflater.inflate(R.layout.tzm_shop_detail_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
			viewHolder.iv_picture = (ImageView) view
					.findViewById(R.id.iv_picture);
			viewHolder.tv_sellNumber= (TextView) view.findViewById(R.id.tv_sellNumber);
			// viewHolder.llv_product = (LinearLayoutForListView)
			// view.findViewById(R.id.llv_product);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		ArrayList<Products> inf = this.list;
		final Products info = inf.get(position);
		viewHolder.tv_name.setText(info.productName);
		viewHolder.tv_price.setText("¥ " + info.price);
		viewHolder.tv_price.setTextColor(c.getResources().getColor(
				R.color.tit_pink));
		bitmapUtils.display(viewHolder.iv_picture, "" + info.image);
		viewHolder.tv_sellNumber.setText("销量:"+info.sellNumber);
		return view;
	}

	private class ViewHolder {
		TextView tv_name, tv_price,tv_sellNumber;
		ImageView iv_picture;
		// LinearLayoutForListView llv_product;
	}
}
