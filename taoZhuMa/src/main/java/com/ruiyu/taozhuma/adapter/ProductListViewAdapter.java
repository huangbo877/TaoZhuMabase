package com.ruiyu.taozhuma.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.ProductDetailActivity;
import com.ruiyu.taozhuma.activity.TzmProductListActivity;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.HomePageFloorListModel;

public class ProductListViewAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<HomePageFloorListModel> list;
	private Context context;
	private HomePageFloorListModel floorListModel;
	private xUtilsImageLoader imageLoader;

	public ProductListViewAdapter(Context context,
			List<HomePageFloorListModel> list,xUtilsImageLoader imageLoader) {
		this.list = list;
		this.context = context;
		this.imageLoader=imageLoader;
		imageLoader.configDefaultLoadFailedImage(R.drawable.loading_long);
		imageLoader.configDefaultLoadingImage(R.drawable.loading_long);
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
			view = layoutInflater.inflate(R.layout.product_listview_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_titleName = (TextView) view
					.findViewById(R.id.tv_titleName);
			viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
			viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			viewHolder.img_ioc = (ImageView) view.findViewById(R.id.img_ioc);
			viewHolder.img_content = (ImageView) view
					.findViewById(R.id.img_content);
			viewHolder.img_left_top = (ImageView) view
					.findViewById(R.id.img_left_top);
			viewHolder.img_left_bottom = (ImageView) view
					.findViewById(R.id.img_left_bottom);
			viewHolder.img_right = (ImageView) view
					.findViewById(R.id.img_right);
			viewHolder.ll_subTypeId = (LinearLayout) view
					.findViewById(R.id.ll_subTypeId);
			viewHolder.layout1 = (LinearLayout) view.findViewById(R.id.layout1);
			viewHolder.layout2 = (LinearLayout) view.findViewById(R.id.layout2);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		floorListModel = this.list.get(position);
		if (floorListModel.type.equals("1")) {
			viewHolder.img_ioc.setImageResource(R.drawable.icon_control);
			viewHolder.tv_titleName.setText(R.string.yaokongdiandongToys);
			viewHolder.ll_subTypeId.setTag(floorListModel.type);
		} else if (floorListModel.type.equals("2")) {
			viewHolder.img_ioc.setImageResource(R.drawable.icon_pen);
			viewHolder.tv_titleName.setText(R.string.zaojiaoyinyueToys);
			viewHolder.ll_subTypeId.setTag(floorListModel.type);
		} else if (floorListModel.type.equals("3")) {
			viewHolder.img_ioc.setImageResource(R.drawable.icon_cup);
			viewHolder.tv_titleName.setText(R.string.guojiajiaToys);
			viewHolder.ll_subTypeId.setTag(floorListModel.type);
		} else if (floorListModel.type.equals("4")) {
			viewHolder.img_ioc.setImageResource(R.drawable.icon_car);
			viewHolder.tv_titleName.setText(R.string.tongCheToys);
			viewHolder.ll_subTypeId.setTag(floorListModel.type);
		} else if (floorListModel.type.equals("5")) {
			viewHolder.img_ioc.setImageResource(R.drawable.icon_alpini);
			viewHolder.tv_titleName.setText(R.string.yiZhiToys);
			viewHolder.ll_subTypeId.setTag(floorListModel.type);
		} else if (floorListModel.type.equals("6")) {
			viewHolder.img_ioc.setImageResource(R.drawable.icon_other);
			viewHolder.tv_titleName.setText(R.string.ortherToys);
			viewHolder.ll_subTypeId.setTag(floorListModel.type);
		}
		if (floorListModel.arr.size() == 3) {
			viewHolder.layout1.setVisibility(View.GONE);
			viewHolder.layout2.setVisibility(View.VISIBLE);
			imageLoader.display(viewHolder.img_right,
					floorListModel.arr.get(0).image);
			viewHolder.img_right.setTag(floorListModel.arr.get(0).typeId);
			imageLoader.display(viewHolder.img_left_bottom,
					floorListModel.arr.get(1).image);
			viewHolder.img_left_bottom.setTag(floorListModel.arr.get(1).typeId);
			imageLoader.display(viewHolder.img_left_top,
					floorListModel.arr.get(2).image);
			viewHolder.img_left_top.setTag(floorListModel.arr.get(2).typeId);
		} else {
			viewHolder.layout1.setVisibility(View.VISIBLE);
			viewHolder.layout2.setVisibility(View.GONE);

			imageLoader.display(viewHolder.img_content,
					floorListModel.arr.get(0).image);
			viewHolder.img_content.setTag(floorListModel.arr.get(0).typeId);

			viewHolder.tv_price.setText(floorListModel.arr.get(0).price + "元");
			viewHolder.tv_title.setText(floorListModel.arr.get(0).title);

		}
		View.OnClickListener clickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.ll_subTypeId:
					if (v.getTag() != null) {
						Intent llintent = new Intent(context,
								TzmProductListActivity.class);
						llintent.putExtra("subTypeId",
								Integer.parseInt(v.getTag().toString()));
						context.startActivity(llintent);
					}
					break;
				case R.id.img_content:
					if (v.getTag() != null) {
						Intent intent1 = new Intent(context,
								ProductDetailActivity.class);
						intent1.putExtra("id",
								Integer.parseInt(v.getTag().toString()));
						context.startActivity(intent1);
					}
					break;
				case R.id.img_left_top:
					if (v.getTag() != null) {
						Intent intent2 = new Intent(context,
								ProductDetailActivity.class);
						intent2.putExtra("id",
								Integer.parseInt(v.getTag().toString()));
						context.startActivity(intent2);
					}
					break;
				case R.id.img_left_bottom:
					if (v.getTag() != null) {
						Intent intent3 = new Intent(context,
								ProductDetailActivity.class);
						intent3.putExtra("id",
								Integer.parseInt(v.getTag().toString()));
						context.startActivity(intent3);
					}
					break;
				case R.id.img_right:
					if (v.getTag() != null) {
						Intent intent4 = new Intent(context,
								ProductDetailActivity.class);
						intent4.putExtra("id",
								Integer.parseInt(v.getTag().toString()));
						context.startActivity(intent4);
					}
					break;

				}
			}
		};

		viewHolder.ll_subTypeId.setOnClickListener(clickListener);
		viewHolder.img_content.setOnClickListener(clickListener);
		viewHolder.img_left_top.setOnClickListener(clickListener);
		viewHolder.img_left_bottom.setOnClickListener(clickListener);
		viewHolder.img_right.setOnClickListener(clickListener);

		return view;
	}

	private class ViewHolder {
		TextView tv_titleName, tv_price, tv_title;
		ImageView img_ioc, img_content, img_left_top, img_left_bottom,
				img_right;
		LinearLayout ll_subTypeId, layout1, layout2;
	}

}
