package com.ruiyu.taozhuma.adapter;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.TzmProductTypeModel;
import com.ruiyu.taozhuma.model.TzmProductTypeModel.Type;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.widget.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import com.ruiyu.taozhuma.widget.GridViewForScrollView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TzmProductTypeExAdapter extends AnimatedExpandableListAdapter {
	private LayoutInflater layoutInflater;
	private List<TzmProductTypeModel> list;
	private Context c;
	private BitmapUtils imageLoader;

	public TzmProductTypeExAdapter(Context c, List<TzmProductTypeModel> list) {
		this.list = list;
		this.c = c;
		layoutInflater = LayoutInflater.from(c);
		imageLoader = new BitmapUtils(c);
		imageLoader.configDefaultLoadFailedImage(R.drawable.loding_defult);// 加载失败图片
		imageLoader.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
		imageLoader.configMemoryCacheEnabled(true);
		imageLoader.configDiskCacheEnabled(true);
	}

	@Override
	public int getGroupCount() {
		return this.list.size();
	}

	@Override
	public int getRealChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.list.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return this.list.get(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(
					R.layout.tzm_product_type_group_item, null);
			viewHolder = new ViewHolder();
			viewHolder.im_type_logo = (ImageView) convertView
					.findViewById(R.id.im_type_logo);
			viewHolder.tv_type_tittle = (TextView) convertView
					.findViewById(R.id.tv_type_tittle);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_type_tittle.setText(list.get(groupPosition).name);
		try {
			imageLoader.display(viewHolder.im_type_logo,
					list.get(groupPosition).image);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 判断实例可以展开，如果可以则改变右侧的图标
		// if (isExpanded)
		// image.setBackgroundResource(R.drawable.btn_browser2);
		// else
		// image.setBackgroundResource(R.drawable.btn_browser);

		return convertView;

	}

	@Override
	public View getRealChildView(final int groupPosition,
			final int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(
					R.layout.tzm_product_type_child_item, null);
			viewHolder = new ViewHolder();
			viewHolder.gridView = (GridViewForScrollView) convertView
					.findViewById(R.id.gv_child);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ArrayList<Type> info = list.get(groupPosition).type;
		ProductClassifyAdapter adapter = new ProductClassifyAdapter(c, info);
		viewHolder.gridView.setAdapter(adapter);
		// viewHolder.tv_child_name.setText(info.sonName);
		// viewHolder.tv_child_name.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent intent = new Intent(c, TzmHelpListActivity.class);
		// intent.putExtra("id", info.sonId);
		// intent.putExtra("title", info.sonName);
		// c.startActivity(intent);
		//
		// }
		// });
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// ToastUtils.showShortToast(c, groupPosition+":"+childPosition);
		return false;
	}

	/**
	 * 
	 * 
	 * @author
	 * 
	 */
	private class ViewHolder {
		TextView tv_type_tittle;
		ImageView im_type_logo;
		GridViewForScrollView gridView;
	}

}
