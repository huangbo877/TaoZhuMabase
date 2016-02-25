package com.ruiyu.taozhuma.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.base.BaseApplication;
import com.ruiyu.taozhuma.model.ProductSkuLinkModel;
import com.ruiyu.taozhuma.model.ProductSkuListModel.SkuValue;

public class ProductSKUAFormatdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<SkuValue> list;
	private Context context;
	private DbUtils dbUtils;
	private Integer skuColorId;
	private Integer pid;// 商品id
	private Handler handler;

	public ProductSKUAFormatdapter(Context context, List<SkuValue> list,
			Integer pid,Handler handler) {
		this.list = list;
		this.context = context;
		this.dbUtils = BaseApplication.getDbUtils();
		this.pid = pid;
		this.handler = handler;
		layoutInflater = LayoutInflater.from(context);
	}

	public void setSkuColorId(Integer skuColorId) {
		this.skuColorId = skuColorId;
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

	@SuppressLint("InflateParams")
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
		viewHolder.btn_sku.setText(info.optionValue);
		try {
			if (skuColorId != null && skuColorId > 0) {
				// 颜色已选择状态下

				ProductSkuLinkModel linkModel = dbUtils.findFirst(Selector
						.from(ProductSkuLinkModel.class)
						.where("skuFormatId", "=", info.optionId)
						.and("skuColorId", "=", skuColorId)
						.and("pid", "=", pid));
				if (linkModel != null && linkModel.status == 1) {
					// 可选
					viewHolder.btn_sku.getPaint().setFlags(0);
					info.isclckabel = true;
					if (info.status == 1) {
						// 选中
						viewHolder.btn_sku
								.setBackgroundResource(R.drawable.pink_solid_corner);
						viewHolder.btn_sku.setTextColor(context.getResources()
								.getColor(R.color.white));
					} else {
						// 未选中
						viewHolder.btn_sku
								.setBackgroundResource(R.drawable.pink_stroke_corner);
						viewHolder.btn_sku.setTextColor(context.getResources()
								.getColor(R.color.base));
					}

				} else {
					// 不可选
					viewHolder.btn_sku
							.setBackgroundResource(R.drawable.gray_stroke_corner);
					info.isclckabel = false;
					viewHolder.btn_sku.setTextColor(context.getResources()
							.getColor(R.color.gray_line));
					viewHolder.btn_sku.getPaint().setFlags(
							Paint.STRIKE_THRU_TEXT_FLAG); // 中划线
					if (info.status == 1) {
						// 每次切换颜色，若选中不可选选择，清空
						info.status = 0;
						handler.sendEmptyMessage(0);
					}

				}
			} else {
				// 初始状态

				info.isclckabel = true;

				if (info.status == 1) {
					// 选中
					viewHolder.btn_sku
							.setBackgroundResource(R.drawable.pink_solid_corner);
					viewHolder.btn_sku.setTextColor(context.getResources()
							.getColor(R.color.white));
				} else {
					// 未选中
					viewHolder.btn_sku.setTextColor(context.getResources()
							.getColor(R.color.base));
					viewHolder.btn_sku
							.setBackgroundResource(R.drawable.pink_stroke_corner);
				}
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return view;
	}

	private class ViewHolder {
		Button btn_sku;
	}

}
