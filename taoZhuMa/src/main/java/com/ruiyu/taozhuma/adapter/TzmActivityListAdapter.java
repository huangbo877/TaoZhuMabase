package com.ruiyu.taozhuma.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.TzmShopDetailActivity;

import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.TzmAppActivityListModel;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TzmActivityListAdapter extends BaseAdapter {
	private String TAG = "TzmActivityListAdapter";
	private LayoutInflater layoutInflater;
	private List<TzmAppActivityListModel> list;
	private Context c;
	private xUtilsImageLoader loader;

	public TzmActivityListAdapter(Context c, List<TzmAppActivityListModel> list) {
		Log.i(TAG, "------>>>TzmActivityListAdapter");
		this.list = list;
		this.c = c;
		loader = new xUtilsImageLoader(c);
		loader.configDefaultLoadFailedImage(R.drawable.loading_long);
		loader.configDefaultLoadingImage(R.drawable.loading_long);
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

	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;

		if (view == null) {
			view = layoutInflater
					.inflate(R.layout.tzm_activity_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_tittle = (TextView) view.findViewById(R.id.tv_tittle);
			viewHolder.tv_j = (ImageView) view.findViewById(R.id.tv_j);
			viewHolder.tv_mj = (TextView) view.findViewById(R.id.tv_mj);
			viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			viewHolder.ll_mj = (LinearLayout) view.findViewById(R.id.ll_mj);
			viewHolder.iv_picture = (ImageView) view
					.findViewById(R.id.iv_picture);
			viewHolder.tv_discount = (TextView) view
					.findViewById(R.id.tv_discount);

			viewHolder.ll_main = (LinearLayout) view.findViewById(R.id.ll_main);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final TzmAppActivityListModel info = this.list.get(position);
		try {
			viewHolder.tv_tittle.setText(info.name);
			if (info.discount.equals("")) {
				viewHolder.tv_discount.setText(info.discount + " ");
				viewHolder.tv_discount.setVisibility(View.GONE);
			} else {
				viewHolder.tv_discount.setText(info.discount+ " ");
				viewHolder.tv_discount.setVisibility(View.VISIBLE);
			}

			if (info.discountType.equals("1")) {
				viewHolder.tv_j.setVisibility(View.VISIBLE);
				
				viewHolder.tv_j.setBackgroundDrawable(c.getResources().getDrawable(R.drawable.tam502));
			} else if (info.discountType.equals("2")) {
				viewHolder.tv_j.setVisibility(View.VISIBLE);
				viewHolder.tv_j.setBackgroundDrawable(c.getResources().getDrawable(R.drawable.tzm503));
			} else {
				viewHolder.ll_mj.setVisibility(View.GONE);
			}
			viewHolder.tv_mj.setText(info.discountText);
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			int interval = 0;

			Date currentTime = dateFormat.parse(info.currentTime);
			Date beginTime = dateFormat.parse(info.endTime2);
			interval = (int) ((beginTime.getTime() - currentTime.getTime()) / (1000));
			if (interval >= 0) {
				long day = interval / (24 * 3600);// 天
				long hour = interval % (24 * 3600) / 3600;// 小时
				long minute = interval % 3600 / 60;// 分钟
				long second = interval % 60;// 秒

				if (day != 0) {

					viewHolder.tv_time.setText("剩" + day + "天");
					viewHolder.tv_time
							.setTextColor(Color.parseColor("#9fa0a0"));

				} else if (hour != 0) {
					if (hour <= 12) {
						viewHolder.tv_time.setTextColor(Color
								.parseColor("#E73C7B"));
					} else {
						viewHolder.tv_time.setTextColor(Color
								.parseColor("#9fa0a0"));
					}
					viewHolder.tv_time.setText("剩" + hour + "小时");
				} else if (minute != 0) {
					viewHolder.tv_time.setText("剩" + minute + "分钟");
					viewHolder.tv_time
							.setTextColor(Color.parseColor("#E73C7B"));
				}
			} else {
				viewHolder.tv_time.setText("即将结束");
				viewHolder.tv_time.setTextColor(Color.parseColor("#9fa0a0"));
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		loader.display(viewHolder.iv_picture, info.banner);

		viewHolder.ll_main.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(c, TzmShopDetailActivity.class);
				// Intent intent = new Intent(c, TzmToySetMealActivity.class);
				intent.putExtra("activityId", info.activityId);
				intent.putExtra("name", info.name);
				c.startActivity(intent);

			}
		});
		return view;
	}

	/**
	 * 
	 * 
	 * @author Toby
	 * 
	 */
	private class ViewHolder {
		TextView tv_tittle, tv_mj, tv_time, tv_discount;//
		ImageView iv_picture, tv_j;
		LinearLayout ll_main,ll_mj;
	}

}
