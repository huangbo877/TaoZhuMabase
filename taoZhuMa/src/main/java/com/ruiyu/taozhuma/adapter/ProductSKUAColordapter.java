package com.ruiyu.taozhuma.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.model.ProductSkuListModel.SkuValue;

public class ProductSKUAColordapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<SkuValue> list;
	private Context context;

	public ProductSKUAColordapter(Context context, List<SkuValue> list) {
		this.list = list;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
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
			view = layoutInflater.inflate(R.layout.test_sku_item, null);
			viewHolder = new ViewHolder();
			viewHolder.btn_sku = (Button) view.findViewById(R.id.btn_sku);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		SkuValue info = this.list.get(position);
		if (info.status == 1) {
			viewHolder.btn_sku
					.setBackgroundResource(R.drawable.pink_solid_corner);
			viewHolder.btn_sku.setTextColor(context.getResources().getColor(
					R.color.white));
		} else {
			viewHolder.btn_sku
					.setBackgroundResource(R.drawable.pink_stroke_corner);
			viewHolder.btn_sku.setTextColor(context.getResources().getColor(
					R.color.base));
		}
		viewHolder.btn_sku.setText(info.optionValue);
		return view;
	}

	private class ViewHolder {
		Button btn_sku;
	}

}
