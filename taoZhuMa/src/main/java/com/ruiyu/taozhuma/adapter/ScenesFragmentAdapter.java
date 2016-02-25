/**
 * 
 */
package com.ruiyu.taozhuma.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.ScenesDetailWebActivity;
import com.ruiyu.taozhuma.activity.TzmShopDetailActivity;

import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.ScenesFragmentModel;

/**
 * @author 林尧 2015-12-23
 */
public class ScenesFragmentAdapter extends BaseAdapter {

	private List<ScenesFragmentModel> list;
	private Context c;
	private xUtilsImageLoader imageLoader;
	private LayoutInflater layoutInflater;

	public ScenesFragmentAdapter(Context c, List<ScenesFragmentModel> list) {
		this.list = list;
		this.c = c;
		layoutInflater = LayoutInflater.from(c);
		imageLoader = new xUtilsImageLoader(c);
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
		final ViewHolder viewHolder;

		if (view == null) {
			view = layoutInflater.inflate(R.layout.scenes_fragment_list_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.tv_tittle_scenes_fragment = (TextView) view
					.findViewById(R.id.tv_tittle_scenes_fragment);
			viewHolder.ll_main_scenes_fragment = (LinearLayout) view
					.findViewById(R.id.ll_main_scenes_fragment);

			viewHolder.tv_time_scenes_fragment = (TextView) view
					.findViewById(R.id.tv_time_scenes_fragment);
			viewHolder.iv_picture__scenes_fragment = (ImageView) view
					.findViewById(R.id.iv_picture__scenes_fragment);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final ScenesFragmentModel info = this.list.get(position);
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		int interval = 0;
		try {
			viewHolder.tv_tittle_scenes_fragment.setText(info.name);

			imageLoader.display(viewHolder.iv_picture__scenes_fragment,
					info.image);
			Date currentTime = dateFormat.parse(info.currentTime);// 获取现在的时间
			Date beginTime = dateFormat.parse(info.endTime);
			interval = (int) ((beginTime.getTime() - currentTime.getTime()) / (1000));
			if (interval >= 0) {
				long day = interval / (24 * 3600);// 天
				long hour = interval % (24 * 3600) / 3600;// 小时
				long minute = interval % 3600 / 60;// 分钟
				long second = interval % 60;// 秒

				if (day != 0) {

					viewHolder.tv_time_scenes_fragment.setText("剩" + day + "天");
				} else if (hour != 0) {
					if (hour <= 12) {
						viewHolder.tv_time_scenes_fragment.setTextColor(Color
								.parseColor("#E73C7B"));
					}
					viewHolder.tv_time_scenes_fragment.setText("剩" + hour
							+ "小时");
				} else if (minute != 0) {
					viewHolder.tv_time_scenes_fragment.setText("剩" + minute
							+ "分钟");
					viewHolder.tv_time_scenes_fragment.setTextColor(Color
							.parseColor("#E73C7B"));
				}
			} else {
				viewHolder.tv_time_scenes_fragment.setText("即将结束");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		android.view.View.OnClickListener clickListener = new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.ll_main_scenes_fragment:
					Intent intent = new Intent(c, ScenesDetailWebActivity.class);
					intent.putExtra("id", info.sceneId.toString());
					intent.putExtra("activityID", info.activityId);
					intent.putExtra("type", 1);
					c.startActivity(intent);
					break;

				}

			}
		};
		viewHolder.ll_main_scenes_fragment.setOnClickListener(clickListener);
		return view;
	}

	/**
	 * 
	 * 
	 * @author Toby
	 * 
	 */
	private class ViewHolder {
		LinearLayout ll_main_scenes_fragment;
		TextView tv_tittle_scenes_fragment, tv_time_scenes_fragment;
		ImageView iv_picture__scenes_fragment;
	}
}