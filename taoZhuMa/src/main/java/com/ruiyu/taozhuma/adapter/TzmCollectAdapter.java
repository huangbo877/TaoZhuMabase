package com.ruiyu.taozhuma.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.lidroid.xutils.BitmapUtils;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.TzmCollectActivity2;
import com.ruiyu.taozhuma.activity.TzmShopDetailActivity;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.TzmCollectModel;
import com.ruiyu.taozhuma.utils.StringUtils;

/*
 * fu
 * 
 * */
public class TzmCollectAdapter extends BaseAdapter {
	protected static final String TAG = "TzmCollectAdapter";
	private LayoutInflater layoutInflater;
	private List<TzmCollectModel> list;
	private Context c;
	private Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();
	private xUtilsImageLoader imageLoader;

	public TzmCollectAdapter(Context c, List<TzmCollectModel> list,
			xUtilsImageLoader imageLoader) {
		this.list = list;
		this.c = c;
		layoutInflater = LayoutInflater.from(c);
		this.imageLoader = imageLoader;
		configCheckMap(false);
	}

	public void configCheckMap(boolean bool) {

		for (int i = 0; i < list.size(); i++) {
			isCheckMap.put(i, bool);
		}
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
	public View getView(final int position, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;

		if (view == null) {
			view = layoutInflater.inflate(R.layout.tzm_collect_item, null);
			viewHolder = new ViewHolder();
			viewHolder.iv_picture = (ImageView) view
					.findViewById(R.id.iv_picture);
			viewHolder.tv_name = (TextView) view.findViewById(R.id.t1);
			viewHolder.c_shop = (RelativeLayout) view.findViewById(R.id.c_shop);
			viewHolder.layout = (FrameLayout) view.findViewById(R.id.layout);
			viewHolder.cb_sd = (CheckBox) view.findViewById(R.id.cb_sd);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final TzmCollectModel info = this.list.get(position);
		viewHolder.tv_name.setText(info.favName);
		BitmapUtils bitmapUtils = new BitmapUtils(c);
		if (TzmCollectActivity2.isBox) {
			viewHolder.cb_sd.setVisibility(View.VISIBLE);
		} else {
			viewHolder.cb_sd.setVisibility(View.GONE);
		}
		// 获得该item 是否允许删除
		boolean canRemove = info.isCanRemove();
		viewHolder.cb_sd
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						/*
						 * 将选择项加载到map里面寄存
						 */
						Log.i(TAG, "checkbox 状态变为" + isChecked);
						isCheckMap.put(position, isChecked);
					}

				});

		if (!canRemove) {
			// 隐藏单选按钮,因为是不可删除的
			// viewHolder.cb_sp.setVisibility(View.GONE);
			viewHolder.cb_sd.setChecked(false);
		} else {
			// viewHolder.cb_sp.setVisibility(View.VISIBLE);

			if (isCheckMap.get(position) == null) {
				isCheckMap.put(position, false);
			}
		}
		viewHolder.cb_sd.setChecked(isCheckMap.get(position));
		if (StringUtils.isNotEmpty(info.favImage)) {
			imageLoader.display(viewHolder.iv_picture, info.favImage);
		}
		if (info.status == 0) {// 是否下架 0为下架
			viewHolder.layout.setVisibility(View.VISIBLE);
		} else {
			viewHolder.layout.setVisibility(View.GONE);
		}

		return view;
	}

	// 删除操作
	public void remove(int position) {
		this.list.remove(position);

	}

	// 获取 多个checkbox 状态的 map
	public Map<Integer, Boolean> getCheckMap() {
		return this.isCheckMap;
	}

	/**
	 * 
	 * 
	 * @author Toby
	 * 
	 */
	private class ViewHolder {
		TextView tv_name;
		ImageView iv_picture;
		CheckBox cb_sd;
		RelativeLayout c_shop;
		FrameLayout layout;
	}

}
