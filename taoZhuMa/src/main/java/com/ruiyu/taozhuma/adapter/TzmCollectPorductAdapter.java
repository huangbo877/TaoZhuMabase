package com.ruiyu.taozhuma.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.TzmCollectActivity2;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.TzmCollectModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;

/**
 * 收藏的宝贝
 * 
 * @author LinJianhong
 * 
 */
public class TzmCollectPorductAdapter extends BaseAdapter {
	private String TAG = "TzmCollectPorductAdapter";
	private LayoutInflater layoutInflater;
	private List<TzmCollectModel> list;
	// private ArrayList<String> list;
	private Context c;
	private xUtilsImageLoader imageLoader;

	// 保存 checkbox 的状态 ,用于list 的全选 ,全不选
	private Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();
	public Map<TzmCollectModel, Integer> final_choose = new HashMap<TzmCollectModel, Integer>();

	public TzmCollectPorductAdapter(Context c, List<TzmCollectModel> list,
			xUtilsImageLoader imageLoader) {
		Log.i(TAG, TAG);
		this.list = list;
		this.c = c;
		layoutInflater = LayoutInflater.from(c);
		this.imageLoader = imageLoader;

		// 初始化,默认都没有选中
		configCheckMap(false);
	}

	/**
	 * 首先,默认情况下,所有子项都是没有选中的.这里进行初始化
	 */
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
		LogUtil.Log(TAG, "getView");
		if (view == null) {
			view = layoutInflater.inflate(R.layout.tzm_collect_product_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.iv_picture = (ImageView) view
					.findViewById(R.id.iv_picture);
			viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			viewHolder.tv_yincan = (TextView) view.findViewById(R.id.tv_yincan);
			viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);

			viewHolder.cb_sp = (CheckBox) view.findViewById(R.id.cb_sp);

			viewHolder.c_product = (RelativeLayout) view
					.findViewById(R.id.c_product);
			viewHolder.layout = (RelativeLayout) view.findViewById(R.id.layout);
			viewHolder.iv_product_status = (ImageView) view
					.findViewById(R.id.iv_product_status);

			view.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		final TzmCollectModel info = this.list.get(position);
		viewHolder.tv_name.setText(info.favName);
		viewHolder.tv_price.setText("¥ " + info.favPrice);
		//每次点击编辑后 isBox 的值会改变,并刷新适配器
		// 首次加载隐藏掉所有的选择框
		if (TzmCollectActivity2.isBox) {
			viewHolder.cb_sp.setVisibility(View.VISIBLE);
		} else {
			viewHolder.cb_sp.setVisibility(View.GONE);
		}
		// 选择框 的事件
		viewHolder.cb_sp
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						/*
						 * 将选择项加载到map里面寄存
						 */
						LogUtil.Log(TAG, "getView----onCheckedChanged");
						Log.i(TAG, "checkbox 状态变为" + isChecked);
						isCheckMap.put(position, isChecked);
						if (isChecked) {
							final_choose.put(info, info.favId);
							LogUtil.Log(TAG,
									"getView-----onCheckedChanged 选中的总个数"
											+ final_choose.size() + "");
						}
						if (!isChecked) {
							final_choose.remove(info);
							LogUtil.Log(TAG,
									"getView-----onCheckedChanged 选择的总个数 "
											+ final_choose.size() + "");
						}
					}

				});
		LogUtil.Log(TAG, "单选按钮的状态:" + isCheckMap.get(position));
		viewHolder.cb_sp.setChecked(isCheckMap.get(position));

		// 未开始
		if (info.status == 0) {
			imageLoader.display(viewHolder.iv_picture, info.favImage);
			viewHolder.tv_yincan.setText("未开始");
			viewHolder.iv_product_status.setVisibility(View.GONE);
		}
		// 进行中
		if (info.status == 1) {
			imageLoader.display(viewHolder.iv_picture, info.favImage);
			viewHolder.tv_yincan.setText("进行中");
			viewHolder.iv_product_status.setVisibility(View.GONE);
		}
		// 已结束
		if (info.status == 2) {
			imageLoader.display(viewHolder.iv_picture, info.favImage);
			viewHolder.tv_yincan.setText("已结束");
			viewHolder.iv_product_status.setVisibility(View.VISIBLE);

		}

		return view;
	}

	/**
	 * 
	 * 
	 * @author Toby
	 * 
	 */
	private class ViewHolder {
		TextView tv_name, tv_price, tv_yincan;
		ImageView iv_picture;
		ImageView iv_product_status;
		CheckBox cb_sp;
		RelativeLayout c_product;
		RelativeLayout layout;
	}
}
